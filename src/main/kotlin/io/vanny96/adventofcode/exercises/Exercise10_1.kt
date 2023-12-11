package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.Pipe
import io.vanny96.adventofcode.shared.PositionAndDistance
import io.vanny96.adventofcode.util.textFromResource
import kotlin.math.max

fun main() {
    val exerciseData = textFromResource("inputs/exercise_10.txt") ?: return

    val pipesMap = exerciseData.lines()
        .map { it.toCharArray().map { Pipe.fromSign(it) } }

    val startingPointY = pipesMap.indexOfFirst { it.contains(Pipe.Starting) }
    val startingPointX = pipesMap[startingPointY].indexOf(Pipe.Starting)

    val initialPositionAndDistance = PositionAndDistance(startingPointX to startingPointY, 0)

    val positionsQueue = ArrayDeque<PositionAndDistance>()
    positionsQueue.add(initialPositionAndDistance)

    val cache = mutableSetOf(initialPositionAndDistance.position)

    var furthestDistance = 0L

    while (positionsQueue.isNotEmpty()) {
        val currPosition = positionsQueue.removeFirst()
        furthestDistance = max(furthestDistance, currPosition.distance)

        val currPipe = pipesMap[currPosition.position.second][currPosition.position.first]

        val possibleAdjacentPipes = currPipe.allowedDirections
            .asSequence()
            .map { it to currPosition.position + it.modifier }
            .filter { isIndexContainedInMap(pipesMap, it.second) }
            .map { it to pipesMap[it.second.second][it.second.first] }
            .filter { it.second.allowedDirections.contains(it.first.first.opposite()) }
            .map { PositionAndDistance(it.first.second, currPosition.distance + 1) }
            .filterNot { cache.contains(it.position) }
            .toList()

        positionsQueue.addAll(possibleAdjacentPipes)
        cache.addAll(possibleAdjacentPipes.map { it.position })
    }

    println(furthestDistance)
}

private fun isIndexContainedInMap(
    pipesMap: List<List<Pipe>>,
    it: Pair<Int, Int>
) = (0..pipesMap.first().size).contains(it.first) && (0..pipesMap.size).contains(it.second)


operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) =
    (this.first + other.first) to (this.second + other.second)

