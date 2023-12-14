package io.vanny96.adventofcode.exercises

import RockShape
import io.vanny96.adventofcode.util.textFromResource

fun main() {
    val exerciseData = textFromResource("inputs/exercise_14.txt") ?: return
    val sizes = exerciseData.lines().size to exerciseData.lines().first().length
    val map = dataToColumns(exerciseData)

    val tiltedMap = tiltMap(map, sizes, 1000000000)

    val result = calculateNorthWeight(tiltedMap, sizes)

    println(result)
}

private fun calculateNorthWeight(
    tiltedMap: Map<Pair<Int, Int>, RockShape>,
    sizes: Pair<Int, Int>
) = tiltedMap.entries
    .filter { it.value == RockShape.Round }
    .sumOf { sizes.second - it.key.first }

private fun tiltMap(
    map: Map<Pair<Int, Int>, RockShape>,
    sizes: Pair<Int, Int>,
    times: Int
): Map<Pair<Int, Int>, RockShape> {
    val tiltFunctions = arrayOf(::tiltNorth, ::tiltWest, ::tiltSouth, ::tiltEast)

    var tiltedMap = map
    val cache = mutableMapOf<Map<Pair<Int, Int>, RockShape>, Map<Pair<Int, Int>, RockShape>>()

    repeat(times) { time ->
        println("Processing $time Weight ${calculateNorthWeight(tiltedMap, sizes)}")
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

private fun tilt(
    map: Map<Pair<Int, Int>, RockShape>,
    floor: Int,
    reversed: Boolean,
    pick: Test
): Map<Pair<Int, Int>, RockShape> {
    val orientationModifier = if (reversed) -1 else 1
    return map.entries
        .groupBy({ pick.groupingKey(it.key) }, { pick.orderKey(it.key) to it.value })
        .map { rocksGrouped ->
            val tiltedRocks: MutableList<Pair<Int, RockShape>> = mutableListOf()

            rocksGrouped.value.sortedBy { it.first * orientationModifier }.map { rockCoordinatesPair ->
                if (rockCoordinatesPair.second == RockShape.Cube) {
                    tiltedRocks.add(rockCoordinatesPair)
                } else {
                    tiltedRocks.lastOrNull()?.let { lastItem ->
                        tiltedRocks.add((lastItem.first + orientationModifier) to rockCoordinatesPair.second)
                    } ?: tiltedRocks.add(floor to rockCoordinatesPair.second)
                }
            }

            rocksGrouped.key to tiltedRocks
        }
        .map { rocksGrouped ->
            rocksGrouped.second
                .associate { pick.pairWithValue(rocksGrouped.first, it.first) to it.second }
        }
        .reduce { acc, map -> acc + map }
}

private enum class Test {
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

private fun tiltNorth(map: Map<Pair<Int, Int>, RockShape>, sizes: Pair<Int, Int>): Map<Pair<Int, Int>, RockShape> {
    return map.entries
        .groupBy({ it.key.second }, { it.key.first to it.value })
        .map { rocksByCol ->
            val tiltedRocks: MutableList<Pair<Int, RockShape>> = mutableListOf()

            rocksByCol.value.sortedBy { it.first }.map { rockCoordinatesPair ->
                if (rockCoordinatesPair.second == RockShape.Cube) {
                    tiltedRocks.add(rockCoordinatesPair)
                } else {
                    tiltedRocks.lastOrNull()?.let { lastItem ->
                        tiltedRocks.add((lastItem.first + 1) to rockCoordinatesPair.second)
                    } ?: tiltedRocks.add(0 to rockCoordinatesPair.second)
                }
            }

            rocksByCol.key to tiltedRocks
        }
        .map { rocksByCol -> rocksByCol.second.associate { (it.first to rocksByCol.first) to it.second } }
        .reduce { acc, map -> acc + map }
}

private fun tiltSouth(map: Map<Pair<Int, Int>, RockShape>, sizes: Pair<Int, Int>): Map<Pair<Int, Int>, RockShape> {
    return map.entries
        .groupBy({ it.key.second }, { it.key.first to it.value })
        .map { rocksByCol ->
            val tiltedRocks: MutableList<Pair<Int, RockShape>> = mutableListOf()

            rocksByCol.value.sortedByDescending { it.first }.map { rockCoordinatesPair ->
                if (rockCoordinatesPair.second == RockShape.Cube) {
                    tiltedRocks.add(rockCoordinatesPair)
                } else {
                    tiltedRocks.lastOrNull()?.let { lastItem ->
                        tiltedRocks.add((lastItem.first - 1) to rockCoordinatesPair.second)
                    } ?: tiltedRocks.add(sizes.first - 1 to rockCoordinatesPair.second)
                }
            }

            rocksByCol.key to tiltedRocks
        }
        .map { rocksByCol -> rocksByCol.second.associate { (it.first to rocksByCol.first) to it.second } }
        .reduce { acc, map -> acc + map }
}

private fun tiltWest(map: Map<Pair<Int, Int>, RockShape>, sizes: Pair<Int, Int>): Map<Pair<Int, Int>, RockShape> {
    return map.entries
        .groupBy({ it.key.first }, { it.key.second to it.value })
        .map { rocksByRow ->
            val tiltedRocks: MutableList<Pair<Int, RockShape>> = mutableListOf()

            rocksByRow.value.sortedBy { it.first }.map { rockCoordinatesPair ->
                if (rockCoordinatesPair.second == RockShape.Cube) {
                    tiltedRocks.add(rockCoordinatesPair)
                } else {
                    tiltedRocks.lastOrNull()?.let { lastItem ->
                        tiltedRocks.add((lastItem.first + 1) to rockCoordinatesPair.second)
                    } ?: tiltedRocks.add(0 to rockCoordinatesPair.second)
                }
            }

            rocksByRow.key to tiltedRocks
        }
        .map { rocksByRow -> rocksByRow.second.associate { (rocksByRow.first to it.first) to it.second } }
        .reduce { acc, map -> acc + map }
}

private fun tiltEast(map: Map<Pair<Int, Int>, RockShape>, sizes: Pair<Int, Int>): Map<Pair<Int, Int>, RockShape> {
    return map.entries
        .groupBy({ it.key.first }, { it.key.second to it.value })
        .map { rocksByRow ->
            val tiltedRocks: MutableList<Pair<Int, RockShape>> = mutableListOf()

            rocksByRow.value.sortedByDescending { it.first }.map { rockCoordinatesPair ->
                if (rockCoordinatesPair.second == RockShape.Cube) {
                    tiltedRocks.add(rockCoordinatesPair)
                } else {
                    tiltedRocks.lastOrNull()?.let { lastItem ->
                        tiltedRocks.add((lastItem.first - 1) to rockCoordinatesPair.second)
                    } ?: tiltedRocks.add(sizes.second - 1 to rockCoordinatesPair.second)
                }
            }

            rocksByRow.key to tiltedRocks
        }
        .map { rocksByRow -> rocksByRow.second.associate { (rocksByRow.first to it.first) to it.second } }
        .reduce { acc, map -> acc + map }
}

