package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.util.textFromResource

private val digitParsing = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)

fun main() {
    val exerciseData = textFromResource("inputs/exercise_1.txt") ?: return
    val result = exerciseData.lines().sumOf { chainFirstAndLastDigit(it) }

    println(result)
}

private fun chainFirstAndLastDigit(input: String): Int {
    val lookaheadRegex = "((?=one)|(?=two)|(?=three)|(?=four)|(?=five)|(?=six)|(?=seven)|(?=eight)|(?=nine)|\\d)".toRegex()
    val numbersRegex = "(one|two|three|four|five|six|seven|eight|nine)".toRegex()

    fun getNumberFromLookAhead(matchResult: MatchResult) =
        numbersRegex.find(input.substring(matchResult.range.first))!!.value.let { digitParsing[it] }

    val matches = lookaheadRegex.findAll(input).map { it.value.ifEmpty { getNumberFromLookAhead(it) } }

    val firstDigit = matches.first()
    val lastDigit = matches.last()
    return "$firstDigit$lastDigit".toInt()
}



