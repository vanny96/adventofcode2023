package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.util.textFromResource

fun main() {
    val exerciseData = textFromResource("inputs/exercise_1.txt") ?: return
    val result = exerciseData.lines().sumOf { chaiFirstAndLastDigit(it) }

    println(result)
}

fun chaiFirstAndLastDigit(input: String): Int {
    val firstDigit = input.toCharArray().first { it.isDigit() }
    val lastDigit = input.toCharArray().last { it.isDigit() }
    return "$firstDigit$lastDigit".toInt()
}


