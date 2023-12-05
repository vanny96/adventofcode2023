package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.TransitionMap
import io.vanny96.adventofcode.shared.TransitionMapRange
import io.vanny96.adventofcode.util.textFromResource
import io.vanny96.adventofcode.util.*

fun main() {
    val exerciseData = textFromResource("inputs/exercise_5.txt") ?: return

    val dataBlocks = exerciseData.split("\\n\\n".toRegex())

    val seedRanges = getSeedRanges(dataBlocks[0])
    val transitionMaps = getTransitionMaps(dataBlocks.subList(1, dataBlocks.size))

    val result = seedRanges.minOf { findLowestDestinationInSeedRange(it, transitionMaps) }

    println(result)

}

private fun getSeedRanges(seedsLine: String): Set<LongRange> = seedsLine.removePrefix("seeds:").trim()
    .let { "\\d+ \\d+".toRegex().findAll(it) }
    .map { it.value }
    .map { seedsRange -> seedsRange.split(" ").map { it.toLong() } }
    .map { it[0]..it[0] + it[1] }
    .toSet()

private fun getTransitionMaps(dataBlocks: List<String>): List<TransitionMap> = dataBlocks
    .map { it.trim() }
    .map { it.lines().subList(1, it.lines().size) }
    .map { groupLines ->
        groupLines
            .map { it.split(" ").map { it.toLong() } }
            .map { TransitionMapRange(it[0], it[1], it[2]) }
            .toSet()
    }
    .map { TransitionMap(it) }

private fun findLowestDestinationInSeedRange(seedRange: LongRange, transitionMaps: List<TransitionMap>): Long =
    transitionMaps.fold(listOf(seedRange)) { sourceRanges, transitionMap ->
        sourceRanges.flatMap { applyTransition(it, transitionMap) }
    }.minOf { it.first }

private fun applyTransition(range: LongRange, transitionMap: TransitionMap): List<LongRange> {
    val intersections = getShiftedIntersections(transitionMap, range)
    val unchanged = getComplements(transitionMap, range)

    return intersections + unchanged
}

private fun getComplements(transitionMap: TransitionMap, range: LongRange): List<LongRange> =
    transitionMap.mapRanges
        .fold(listOf(range)) { acc, map ->
            acc.flatMap { it.subtract(map.sourceRange) }
        }

private fun getShiftedIntersections(transitionMap: TransitionMap, range: LongRange): List<LongRange> =
    transitionMap.mapRanges
        .mapNotNull { range.intersect(it.sourceRange)?.shift(it.sourceDestinationModifier) }
        .fold(listOf()) { acc, destination ->
            if (acc.isEmpty()) {
                listOf(destination)
            } else {
                acc.flatMap { it.union(destination) }
            }
        }


