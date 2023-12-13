package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.util.textFromResource
import org.apache.commons.lang3.StringUtils
import kotlin.math.min

fun main() {
    val exerciseData = textFromResource("inputs/exercise_12.txt") ?: return

    val part1 = exerciseData.lines().sumOf { getPossibleCombinations(it) }
    println("Part 1: $part1") //7163

    val part2 = exerciseData.lines().map { expandLine(it) }.sumOf { getPossibleCombinations(it) }
    println("Part 2: $part2") //17788038834112

}

private fun expandLine(line: String): String {
    val (springsData, combinationsData) = line.split(" ").let { it[0] to it[1] }
    val multipliedSpringData = (1..5).joinToString("?") { springsData }
    val multipliedCombinationData = (1..5).joinToString(",") { combinationsData }
    return "$multipliedSpringData $multipliedCombinationData"
}

private fun getPossibleCombinations(line: String): Long {
    val cache = mutableMapOf<String, Long>()
    return recursionLogic(line, cache)
}

private fun recursionLogic(line: String, cache: MutableMap<String, Long>): Long {
    val (springLine, combinationLine) = line.split(" ").let { StringUtils.strip(it[0], ".") to it[1] }
    val cacheKey = "$springLine $combinationLine"

    // Exit conditions
    cache[cacheKey]?.let { return it }

    val combinations = combinationLine.split(",").mapNotNull { it.toIntOrNull() }
    if (combinations.isEmpty()) {
        val empty = springLine.isEmpty()
        val noMandatory = springLine.none { it == '#' }
        return if (empty || noMandatory) 1 else 0
    }

    if (combinations.first() > springLine.length) return 0

    // Process
    val requiredLength = combinations.first()

    val springFirstBlock = springLine.split(".")[0]
    val firstMandatorySpring = springFirstBlock.withIndex().find { it.value == '#' }?.index

    val availableFirstSpringSlots = firstMandatorySpring ?: (springFirstBlock.length - 1)
    val possibleCombinations = min(availableFirstSpringSlots, springFirstBlock.length - requiredLength) + 1

    val newLines = (0..<possibleCombinations)
        .map { it..<it + requiredLength }
        .filterNot { springFirstBlock.checkIfCharacter('#', it.last + 1) }
        .map { springLine.substring(min(it.last + 2, springLine.length)) }

    val newCombinations = combinations.subList(1, combinations.size).joinToString(",")

    val allQuestionMarksResult = firstMandatorySpring?.let { 0 } ?: run {
        val newSpring = springLine.substring(springFirstBlock.length)
        recursionLogic("$newSpring $combinationLine", cache)
    }

    val result = newLines.map { "$it $newCombinations" }.sumOf { recursionLogic(it, cache) } + allQuestionMarksResult

    cache[cacheKey] = result

    return result
}

private fun String.checkIfCharacter(char: Char, index: Int): Boolean {
    return if (length > index) this[index] == char else false
}
