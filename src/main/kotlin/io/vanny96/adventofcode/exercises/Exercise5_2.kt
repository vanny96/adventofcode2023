package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.TransitionMap
import io.vanny96.adventofcode.shared.TransitionMapRange
import io.vanny96.adventofcode.util.textFromResource
import kotlinx.coroutines.*
import kotlin.math.min

fun main() {
    val exerciseData = textFromResource("inputs/exercise_5.txt") ?: return

    val dataBlocks = exerciseData.split("\\n\\n".toRegex())

    val seedRanges = getSeedRanges(dataBlocks[0])
    val transitionMaps = getTransitionMaps(dataBlocks.subList(1, dataBlocks.size))

    runBlocking {
        val result = seedRanges
            .map { async(Dispatchers.IO) { findLowestDestinationInSeedRange(it, transitionMaps) } }
            .awaitAll()
            .min()

        println(result)

    }
}

private fun getSeedRanges(seedsLine: String) = seedsLine.removePrefix("seeds:").trim()
    .let { "\\d+ \\d+".toRegex().findAll(it) }
    .map { it.value }
    .map { seedsRange -> seedsRange.split(" ").map { it.toLong() } }
    .map { it[0]..it[0] + it[1] }
    .toSet()

private fun getTransitionMaps(dataBlocks: List<String>) = dataBlocks
    .map { it.trim() }
    .map { it.lines().subList(1, it.lines().size) }
    .map { groupLines ->
        groupLines
            .map { it.split(" ").map { it.toLong() } }
            .map { TransitionMapRange(it[0], it[1], it[2]) }
            .toSet()
    }
    .map { TransitionMap(it) }

private fun findLowestDestinationInSeedRange(seedRange: LongRange, transitionMaps: List<TransitionMap>): Long {
    println("Starting processing of $seedRange")
    val minValue = seedRange.minOf { seed ->
        transitionMaps
            .fold(seed) { source, transitionMap -> transitionMap.transitionValue(source) }
    }
    println("Processing of $seedRange ended with minimum value $minValue")
    return minValue
}
