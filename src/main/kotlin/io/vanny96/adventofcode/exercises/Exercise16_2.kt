package io.vanny96.adventofcode.exercises

import BeamDirection
import io.vanny96.adventofcode.util.textFromResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun main() {
    val exerciseData = textFromResource("inputs/exercise_16.txt") ?: return
    val sizes = exerciseData.lines().let { it.size to it.first().length }

    val map = exerciseData.lines().withIndex()
        .flatMap { line ->
            line.value.toCharArray().withIndex()
                .map { (line.index to it.index) to it.value }
        }
        .associateBy({ it.first }, { it.second })

    runBlocking {
        val downWards = (0..<sizes.second)
            .map { async(Dispatchers.IO) { getEnergizedBeams(0 to it, BeamDirection.Down, map, sizes) } }

        val upWards = (0..<sizes.second)
            .map { async(Dispatchers.IO) { getEnergizedBeams(sizes.first - 1 to it, BeamDirection.Up, map, sizes) } }

        val rightWard = (0..<sizes.first)
            .map { async(Dispatchers.IO) { getEnergizedBeams(it to 0, BeamDirection.Right, map, sizes) } }

        val leftWard = (0..<sizes.first)
            .map { async(Dispatchers.IO) { getEnergizedBeams(it to sizes.second - 1, BeamDirection.Left, map, sizes) } }

        val result = listOf(downWards, upWards, rightWard, leftWard)
            .flatten()
            .awaitAll()
            .max()

        println(result)
    }

}

private fun getEnergizedBeams(
    position: Pair<Int, Int>,
    direction: BeamDirection,
    map: Map<Pair<Int, Int>, Char>,
    sizes: Pair<Int, Int>
): Int {
    val cache = mutableSetOf<Pair<Pair<Int, Int>, BeamDirection>>()
    navigateBeam(position, direction, map, sizes, cache)
    return cache.distinctBy { it.first }.size
}

private fun navigateBeam(
    position: Pair<Int, Int>,
    direction: BeamDirection,
    map: Map<Pair<Int, Int>, Char>,
    sizes: Pair<Int, Int>,
    cache: MutableSet<Pair<Pair<Int, Int>, BeamDirection>>
) {
    if (position.first < 0 || position.first >= sizes.first) return
    if (position.second < 0 || position.second >= sizes.second) return

    if (cache.contains(position to direction)) return
    cache.add(position to direction)

    val mapPosition = map[position]!!
    when (mapPosition) {
        '.' -> {
            val newPosition =
                (position.first + direction.modifier.first) to (position.second + direction.modifier.second)
            navigateBeam(newPosition, direction, map, sizes, cache)
        }

        '|' -> {
            if (direction == BeamDirection.Up || direction == BeamDirection.Down) {
                val newPosition = (position.first + direction.modifier.first) to position.second
                navigateBeam(newPosition, direction, map, sizes, cache)
            } else {
                val upPosition = (position.first - 1) to position.second
                val downPosition = (position.first + 1) to position.second
                navigateBeam(upPosition, BeamDirection.Up, map, sizes, cache)
                navigateBeam(downPosition, BeamDirection.Down, map, sizes, cache)
            }
        }

        '-' -> {
            if (direction == BeamDirection.Left || direction == BeamDirection.Right) {
                val newPosition = position.first to (position.second + direction.modifier.second)
                navigateBeam(newPosition, direction, map, sizes, cache)
            } else {
                val leftPosition = position.first to (position.second - 1)
                val rightPosition = position.first to (position.second + 1)
                navigateBeam(leftPosition, BeamDirection.Left, map, sizes, cache)
                navigateBeam(rightPosition, BeamDirection.Right, map, sizes, cache)
            }
        }

        '/' -> {
            val newDirection = direction.rightMirrorShift()
            val newPosition =
                (position.first + newDirection.modifier.first) to (position.second + newDirection.modifier.second)
            navigateBeam(newPosition, newDirection, map, sizes, cache)
        }

        '\\' -> {
            val newDirection = direction.lefttMirrorShift()
            val newPosition =
                (position.first + newDirection.modifier.first) to (position.second + newDirection.modifier.second)
            navigateBeam(newPosition, newDirection, map, sizes, cache)
        }
    }
}

