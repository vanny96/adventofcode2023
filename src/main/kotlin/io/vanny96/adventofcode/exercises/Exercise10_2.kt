package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.Direction
import io.vanny96.adventofcode.shared.Pipe
import io.vanny96.adventofcode.util.textFromResource

fun main() {
    val start = System.nanoTime()
    val exerciseData = textFromResource("inputs/exercise_10.txt") ?: return

    val pipesMap = exerciseData.lines()
        .map { it.toCharArray().map { Pipe.fromSign(it) } }

    var rightCounter = 0
    var leftCounter = 0

    val loopPositions = navigatePipes(pipesMap) { direction, pipe, _ ->
        if (pipe.allowedDirections.contains(direction.turnRight())) rightCounter++
        if (pipe.allowedDirections.contains(direction.turnLeft())) leftCounter++
    }


    val internalPoints = mutableSetOf<Pair<Int, Int>>()
    navigatePipes(pipesMap) { direction, pipe, position ->
        val insideModifiers = if (rightCounter > leftCounter) pipe.checkRight(direction) else pipe.checkLeft(direction)
        val insidePositions = insideModifiers.map { it + position }

        val positionsQueue = ArrayDeque<Pair<Int, Int>>()
        positionsQueue.addAll(insidePositions)

        while (positionsQueue.isNotEmpty()) {
            val currPosition = positionsQueue.removeFirst()

            if (internalPoints.contains(currPosition)) continue
            if (loopPositions.contains(currPosition)) continue

            internalPoints.add(currPosition)

            val adjacentPositions = Direction.entries.map { it.modifier + currPosition }

            positionsQueue.addAll(adjacentPositions)
        }
    }

    println(internalPoints.size)
    println("Part 2: ${System.nanoTime() - start} nanos")
}

private fun navigatePipes(
    pipesMap: List<List<Pipe>>,
    action: (Direction, Pipe, Pair<Int, Int>) -> Unit
): Set<Pair<Int, Int>> {
    val initialPosition = getInitialPosition(pipesMap)

    val positionsQueue = ArrayDeque<Pair<Int, Int>>()
    positionsQueue.add(initialPosition)

    val cache = mutableSetOf(initialPosition)

    while (positionsQueue.isNotEmpty()) {
        val currPosition = positionsQueue.removeFirst()

        val currPipe = pipesMap[currPosition.second][currPosition.first]

        currPipe.allowedDirections
            .asSequence()
            .map { it to currPosition + it.modifier }
            .filter { isIndexContainedInMap(pipesMap, it.second) }
            .map { it to pipesMap[it.second.second][it.second.first] }
            .filter { it.second.allowedDirections.contains(it.first.first.opposite()) }
            .filterNot { cache.contains(it.first.second) }
            .firstOrNull()
            ?.let {
                action(it.first.first, it.second, it.first.second)

                positionsQueue.add(it.first.second)
                cache.add(it.first.second)
            }
    }

    return cache
}

private fun getInitialPosition(pipesMap: List<List<Pipe>>): Pair<Int, Int> {
    val startingPointY = pipesMap.indexOfFirst { it.contains(Pipe.Starting) }
    val startingPointX = pipesMap[startingPointY].indexOf(Pipe.Starting)

    val initialPosition = startingPointX to startingPointY
    return initialPosition
}

private fun isIndexContainedInMap(
    pipesMap: List<List<Pipe>>,
    it: Pair<Int, Int>
) = (0..pipesMap.first().size).contains(it.first) && (0..pipesMap.size).contains(it.second)


