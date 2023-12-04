package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.Game4
import io.vanny96.adventofcode.util.textFromResource
import kotlin.math.pow

fun main() {
    val exerciseData = textFromResource("inputs/exercise_4.txt") ?: return
    val result = exerciseData.lines()
        .map { Game4.fromGameInfo(it) }
        .map { it.winningPlayerNumbers }
        .sumOf { if (it.isEmpty()) 0 else 2.0.pow(it.size-1).toInt() }

    println(result)
}
