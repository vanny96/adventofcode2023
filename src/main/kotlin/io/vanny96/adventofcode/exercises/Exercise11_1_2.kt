package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.util.textFromResource
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val exerciseData = textFromResource("inputs/exercise_11.txt") ?: return

    val map = exerciseData.lines().map { it.toCharArray().toMutableList() }.toMutableList()
    val rowSize = map.first().size

    val emptyRows = map
        .mapIndexed { index, chars -> index to chars }
        .filterNot { it.second.contains('#') }
        .map { it.first }
        .toSet()

    val emptyCols = (0..<rowSize).filterNot { index -> map.map { it[index] }.contains('#') }.toSet()

    val galaxiesCoordinates = map.flatMapIndexed { rowIndex, row ->
        List(row.size) { colIndex -> rowIndex to colIndex }
            .filter { map[it.first][it.second] == '#' }
    }

    val galaxiesPermutations = galaxiesCoordinates.flatMapIndexed { index, galaxy ->
        galaxiesCoordinates.subList(index + 1, galaxiesCoordinates.size).map { galaxy to it }
    }

    val resultPart1 = galaxiesPermutations
        .map { (galaxyOne, galaxyTwo) -> adjustSize(galaxyOne, galaxyTwo, emptyRows, emptyCols, 2) }
        .sumOf { (galaxyOne, galaxyTwo) -> calculateDistance(galaxyTwo, galaxyOne) }

    val resultPart2 = galaxiesPermutations
        .map { (galaxyOne, galaxyTwo) -> adjustSize(galaxyOne, galaxyTwo, emptyRows, emptyCols, 1000000) }
        .sumOf { (galaxyOne, galaxyTwo) -> calculateDistance(galaxyTwo, galaxyOne) }

    println(resultPart1)
    println(resultPart2)
}

private fun calculateDistance(
    galaxyTwo: Pair<Int, Int>,
    galaxyOne: Pair<Int, Int>
) = abs(galaxyTwo.first - galaxyOne.first) + abs(galaxyTwo.second - galaxyOne.second)

private fun adjustSize(
    galaxyOne: Pair<Int, Int>,
    galaxyTwo: Pair<Int, Int>,
    emptyRows: Set<Int>,
    emptyCols: Set<Int>,
    emptyRowModifier: Int
): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    // Inserts empty rows
    val rowsRange = min(galaxyOne.first, galaxyTwo.first)..max(galaxyOne.first, galaxyTwo.first)
    val rowsModifier = emptyRows.filter { rowsRange.contains(it) }.size * (emptyRowModifier - 1)

    val (rowAdjustedOne, rowAdjustedTwo) = if (galaxyOne.first > galaxyTwo.first) {
        (galaxyOne.first + rowsModifier to galaxyOne.second) to galaxyTwo
    } else {
        galaxyOne to (galaxyTwo.first + rowsModifier to galaxyTwo.second)
    }

    // Insert empty cols
    val colsRange = min(galaxyOne.second, galaxyTwo.second)..max(galaxyOne.second, galaxyTwo.second)
    val colsModifier = emptyCols.filter { colsRange.contains(it) }.size * (emptyRowModifier - 1)

    val (colAdjustedOne, colAdjustedTwo) = if (rowAdjustedOne.second > rowAdjustedTwo.second) {
        (rowAdjustedOne.first to rowAdjustedOne.second + colsModifier) to rowAdjustedTwo
    } else {
        rowAdjustedOne to (rowAdjustedTwo.first to rowAdjustedTwo.second + colsModifier)
    }

    return colAdjustedOne to colAdjustedTwo
}