package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.util.textFromResource

fun main() {
    val exerciseData = textFromResource("inputs/exercise_3.txt") ?: return

    val numbersCoordinates = exerciseData.lines()
        .mapIndexed { index, line -> index to lineToNumberCoordinates(line) }
        .toMap()

    val result = exerciseData.lines()
        .flatMapIndexed { index, line -> lineToGearCoordinates(line, index) }
        .map { findGearNeighbours(it, numbersCoordinates) }
        .filter { it.size == 2 }
        .sumOf { it[0] * it[1] }

    println(result)
}

private fun findGearNeighbours(
    gearCoordinates: Pair<Int, Int>,
    numbersCoordinates: Map<Int, List<Pair<IntRange, Int>>>
): List<Int> {
    val adjacentRows = gearCoordinates.first - 1..gearCoordinates.first + 1
    val adjacentColumns = gearCoordinates.second - 1..gearCoordinates.second + 1

    return adjacentRows.mapNotNull { row ->
        numbersCoordinates[row]
            ?.filter { adjacentColumns.intersect(it.first).isNotEmpty() }
            ?.map { it.second }
    }.flatten().toList()
}


private fun lineToNumberCoordinates(line: String) =
    "\\d+".toRegex().findAll(line)
        .map { match -> match.range to match.value.toInt() }
        .toList()

private fun lineToGearCoordinates(line: String, lineIndex: Int) =
    "\\*".toRegex().findAll(line)
        .map { match -> (lineIndex to match.range.last) }
        .toSet()
