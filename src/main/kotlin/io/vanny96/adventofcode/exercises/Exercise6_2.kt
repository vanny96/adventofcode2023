package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.RaceData
import io.vanny96.adventofcode.util.textFromResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun main() {
    val exerciseData = textFromResource("inputs/exercise_6.txt") ?: return
    val raceData = RaceData.fromInputs(exerciseData)

    val result = findBetterTimes(raceData)
        .size

    println(result)
}


private fun findBetterTimes(raceData: RaceData): List<Pair<Long, Long>> {
    return (1..raceData.time)
        .map { it to raceData.distance / it }
        .filter { it.second + it.first < raceData.time }
}


