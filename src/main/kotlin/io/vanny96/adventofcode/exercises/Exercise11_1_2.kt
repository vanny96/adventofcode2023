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
            .filter { (row, col) -> map[row][col] == '#' }
            .map { (row, col) ->
                val rowsToAdd = emptyRows.filter { it < row }.size * 999999L
                val colsToAdd = emptyCols.filter { it < col }.size * 999999L

                row + rowsToAdd to col + colsToAdd
            }
    }

    val galaxiesPermutations = galaxiesCoordinates.flatMapIndexed { index, galaxy ->
        galaxiesCoordinates.subList(index + 1, galaxiesCoordinates.size).map { galaxy to it }
    }

    val result = galaxiesPermutations.sumOf { (galaxyOne, galaxyTwo) -> calculateDistance(galaxyTwo, galaxyOne) }

    println(result)
}

private fun calculateDistance(
    galaxyTwo: Pair<Long, Long>,
    galaxyOne: Pair<Long, Long>
) = abs(galaxyTwo.first - galaxyOne.first) + abs(galaxyTwo.second - galaxyOne.second)