package io.vanny96.adventofcode.exercises

import RockShape
import io.vanny96.adventofcode.util.textFromResource

fun main() {
    val exerciseData = textFromResource("inputs/exercise_14.txt") ?: return
    val lines = exerciseData.lines()
    val result = lines.first().indices
        .map { index ->
            lines
                .map { it[index] }
                .map { RockShape.fromValue(it) }
                .withIndex()
                .filter { it.value != null }
        }.map { tiltNorth(it) }
        .map { rocks -> rocks.filter { it.value == RockShape.Round }.map { lines.size - it.index } }
        .sumOf { it.sum() }


    println(result)
}

private fun tiltNorth(rocks: List<IndexedValue<RockShape?>>): List<IndexedValue<RockShape?>> {
    val tiltedRocks: MutableList<IndexedValue<RockShape?>> = mutableListOf()

    rocks.forEach {rock ->
        if (rock.value == RockShape.Cube) {
            tiltedRocks.add(rock)
        } else {
            tiltedRocks.lastOrNull()?.let { lastItem ->
                tiltedRocks.add(IndexedValue(lastItem.index+1, rock.value))
            } ?: tiltedRocks.add(IndexedValue(0, rock.value))
        }
    }

    return tiltedRocks
}
