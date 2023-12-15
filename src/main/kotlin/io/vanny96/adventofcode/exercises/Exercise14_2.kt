package io.vanny96.adventofcode.exercises

import RockShape
import io.vanny96.adventofcode.util.textFromResource

fun main() {
    val exerciseData = textFromResource("inputs/exercise_14.txt") ?: return
    val sizes = exerciseData.lines().size to exerciseData.lines().first().length
    val map = dataToColumns(exerciseData)

    val tiltedMap = spinMap(map, sizes, 1000000000)

    val result = calculateNorthWeight(tiltedMap, sizes)

    println(result)
}

private fun calculateNorthWeight(
    tiltedMap: Map<Pair<Int, Int>, RockShape>,
    sizes: Pair<Int, Int>
) = tiltedMap.entries
    .filter { it.value == RockShape.Round }
    .sumOf { sizes.second - it.key.first }

private fun spinMap(
    map: Map<Pair<Int, Int>, RockShape>,
    sizes: Pair<Int, Int>,
    times: Int
): Map<Pair<Int, Int>, RockShape> {
    val tiltFunctions = arrayOf(::tiltNorth, ::tiltWest, ::tiltSouth, ::tiltEast)

    var tiltedMap = map
    val cache = mutableMapOf<Map<Pair<Int, Int>, RockShape>, Map<Pair<Int, Int>, RockShape>>()

    repeat(times) { time ->
        val initialMap = tiltedMap
        cache[initialMap]?.let {
            var loopLength = 1
            var currItem = it
            while (currItem != initialMap) {
                loopLength++
                currItem = cache[currItem]!!
            }

            val skipAhead = (times - time) % loopLength
            var result = initialMap
            repeat(skipAhead) { result = cache[result]!! }
            return result
        }

        tiltFunctions.forEach { tiltFunction ->
            tiltedMap = tiltFunction(tiltedMap, sizes)
        }
        cache[initialMap] = tiltedMap
    }

    return tiltedMap
}

private fun dataToColumns(data: String): Map<Pair<Int, Int>, RockShape> = data.lines().first().indices
    .map { colIndex ->
        data.lines()
            .map { it[colIndex] }
            .mapIndexed { rowIndex, rock -> (rowIndex to colIndex) to RockShape.fromValue(rock) }
            .filter { it.second != null }
            .associate { it.first to it.second!! }
    }.reduce { acc, map -> acc + map }


private fun tiltNorth(map: Map<Pair<Int, Int>, RockShape>, sizes: Pair<Int, Int>) =
    tilt(map, 0, false, RocksOrientation.Col)


private fun tiltSouth(map: Map<Pair<Int, Int>, RockShape>, sizes: Pair<Int, Int>) =
    tilt(map, sizes.first - 1, true, RocksOrientation.Col)

private fun tiltWest(map: Map<Pair<Int, Int>, RockShape>, sizes: Pair<Int, Int>) =
    tilt(map, 0, false, RocksOrientation.Row)

private fun tiltEast(map: Map<Pair<Int, Int>, RockShape>, sizes: Pair<Int, Int>) =
    tilt(map, sizes.second - 1, true, RocksOrientation.Row)

private fun tilt(
    map: Map<Pair<Int, Int>, RockShape>,
    floor: Int,
    reversed: Boolean,
    groupingBy: RocksOrientation
): Map<Pair<Int, Int>, RockShape> {
    return map.entries
        .groupBy({ groupingBy.groupingKey(it.key) }, { groupingBy.orderKey(it.key) to it.value })
        .map { rocksGrouped -> rocksGrouped.key to tiltGroup(rocksGrouped.value, reversed, floor) }
        .map { rocksGrouped ->
            rocksGrouped.second
                .associate { groupingBy.pairWithValue(rocksGrouped.first, it.first) to it.second }
        }
        .reduce { acc, groupedRocks -> acc + groupedRocks }
}

private fun tiltGroup(
    rocksGrouped: List<Pair<Int, RockShape>>,
    reversed: Boolean,
    floor: Int
): List<Pair<Int, RockShape>> {
    val orientationModifier = if (reversed) -1 else 1
    val tiltedRocks: MutableList<Pair<Int, RockShape>> = mutableListOf()

    rocksGrouped.sortedBy { it.first * orientationModifier }.map { rockCoordinatesPair ->
        if (rockCoordinatesPair.second == RockShape.Cube) {
            tiltedRocks.add(rockCoordinatesPair)
        } else {
            tiltedRocks.lastOrNull()?.let { lastItem ->
                tiltedRocks.add((lastItem.first + orientationModifier) to rockCoordinatesPair.second)
            } ?: tiltedRocks.add(floor to rockCoordinatesPair.second)
        }
    }

    return tiltedRocks
}

private enum class RocksOrientation {
    Col, Row;

    fun groupingKey(key: Pair<Int, Int>) = when (this) {
        Col -> key.second
        Row -> key.first
    }

    fun orderKey(key: Pair<Int, Int>) = when (this) {
        Col -> key.first
        Row -> key.second
    }

    fun pairWithValue(groupValue: Int, orderValue: Int) = when (this) {
        Col -> orderValue to groupValue
        Row -> groupValue to orderValue
    }
}
