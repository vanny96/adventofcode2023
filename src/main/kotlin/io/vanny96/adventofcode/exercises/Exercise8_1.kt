package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.MapEntry
import io.vanny96.adventofcode.util.textFromResource

fun main() {
    val exerciseData = textFromResource("inputs/exercise_8.txt") ?: return
    val dataBlocks = exerciseData.split("\n\n")

    val instructions = dataBlocks[0].toCharArray()

    val map = dataBlocks[1].lines()
        .map { MapEntry.parseFromLine(it) }
        .associateBy { it.key }

    println(findDistanceToEnd("AAA", instructions, map))
}

private fun findDistanceToEnd(
    startingPoint: String,
    instructions: CharArray,
    map: Map<String, MapEntry>,
): Long {
    var counter = 0L
    var position = startingPoint

    while (position != "ZZZ") {
        val direction = instructions[(counter % instructions.size).toInt()]

        position = if (direction == 'L') {
            map[position]!!.left
        } else {
            map[position]!!.right
        }

        counter++
    }

    return counter
}
