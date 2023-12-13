package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.util.textFromResource
import kotlinx.coroutines.runBlocking

fun main() {
    val exerciseData = textFromResource("inputs/exercise_13.txt") ?: return
    runBlocking {
        val patterns = exerciseData.split("\n\n")

        val result = patterns.sumOf { analyzePattern(it) }

        println(result)
    }
}

private fun analyzePattern(pattern: String): Long {
    val lines = pattern.lines()
    val rowsMap = lines

    val colsMap = lines.first().indices
        .map { index -> lines.map { it[index] }.toCharArray().concatToString() }

    return analyzeDirection(rowsMap)?.let {  it * 100L}
        ?: analyzeDirection(colsMap)!!
}

private fun analyzeDirection(map: List<String>): Long? {
    map.subList(0, map.size - 1).indices.forEach { index ->

        var before = index
        var after = index + 1
        while (before >= 0 && after < map.size) {
            val beforeVal = map[before]
            val afterVal = map[after]

            if (afterVal != beforeVal) return@forEach

            before--
            after++
        }

        return index + 1L
    }

    return null
}
