package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.*
import io.vanny96.adventofcode.util.textFromResource

fun main() {
    val exerciseData = textFromResource("inputs/exercise_9.txt") ?: return
    val result = exerciseData.lines()
        .map { it.split(" ").map { it.toInt() } }
        .sumOf { getNextInSequence(it) }

    println(result) //1757008019
}

private fun getNextInSequence(sequence: List<Int>): Long {
    if(sequence.all { it == 0 }) return 0

    val nextSequence = sequence.subList(0, sequence.size - 1)
            .mapIndexed { index, value -> sequence[index + 1] - value }

    return sequence.last() + getNextInSequence(nextSequence)
}
