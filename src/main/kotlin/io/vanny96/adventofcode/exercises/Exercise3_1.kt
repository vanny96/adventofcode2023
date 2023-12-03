package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.util.textFromResource

fun main() {
    val exerciseData = textFromResource("inputs/exercise_3.txt") ?: return

    val symbolsCoordinates = exerciseData.lines()
        .mapIndexed { index, line -> lineToGearCoordinates(line, index) }
        .reduce { acc, set -> acc + set }

    val result = exerciseData.lines()
        .flatMapIndexed { index, line -> lineToNumberCoordinates(line, index) }
        .filter { isAdjacentToSymbol(it.first, symbolsCoordinates) }
        .sumOf { it.second }

    println(result)
}

private val coordinatesModifiers = listOf(
    -1 to -1, -1 to 0, -1 to 1,
    0 to -1, 0 to 0, 0 to 1,
    1 to -1, 1 to 0, 1 to 1
)

private fun isAdjacentToSymbol(coordinatesRange: Pair<Int, IntRange>, symbolsCoordinates: Set<Pair<Int, Int>>) =
    coordinatesRange.second
        .map { coordinatesRange.first to it }
        .flatMap { coordinates ->
            coordinatesModifiers.map {
                (it.first + coordinates.first) to (it.second + coordinates.second)
            }
        }
        .any { symbolsCoordinates.contains(it) }

private fun lineToNumberCoordinates(line: String, lineIndex: Int) =
    "\\d+".toRegex().findAll(line)
        .map { match -> (lineIndex to match.range) to match.value.toInt() }
        .toList()

private fun lineToGearCoordinates(line: String, lineIndex: Int) =
    "[^.\\d \\n]".toRegex().findAll(line)
        .map { match -> (lineIndex to match.range.last) }
        .toSet()
