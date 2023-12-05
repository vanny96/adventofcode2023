package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.TransitionMap
import io.vanny96.adventofcode.shared.TransitionMapRange
import io.vanny96.adventofcode.util.textFromResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun main() {
    val exerciseData = textFromResource("inputs/exercise_5.txt") ?: return

    val dataBlocks = exerciseData.split("\\n\\n".toRegex())

    val seeds = getSeeds(dataBlocks[0])
    val transitionMaps = getTransitionMaps(dataBlocks.subList(1, dataBlocks.size))

    runBlocking {
        val result = seeds
            .map { async(Dispatchers.IO) { getSeedLocation(it, transitionMaps) } }
            .awaitAll()
            .min()

        println(result)
    }

}

private fun getSeeds(seedLine: String) = seedLine.removePrefix("seeds:").trim()
    .split(" ")
    .map { it.toLong() }
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

private fun getSeedLocation(seed: Long, transitionMaps: List<TransitionMap>) = transitionMaps
    .fold(seed) {source, transitionMap -> transitionMap.transitionValue(source)}

