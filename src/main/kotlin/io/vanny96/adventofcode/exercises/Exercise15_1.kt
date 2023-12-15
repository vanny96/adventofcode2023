package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.util.textFromResource

fun main() {
    val exerciseData = textFromResource("inputs/exercise_15.txt") ?: return
    val steps = exerciseData.split(",")

    val part1 = steps.sumOf { hash(it) }
    println(part1) //519041

    val map = steps.fold(ManualHashMap()) { map, it ->
        executeOperationOnMap(it, map)
        map
    }

    val part2 = map.data.withIndex()
        .sumOf { box ->
            box.value.entries.withIndex().sumOf {
                val boxValue = box.index + 1
                val slotValue = it.index + 1
                val lensValue = it.value.value
                boxValue * slotValue * lensValue
            }
        }

    println(part2) //260530
}

private fun executeOperationOnMap(operation: String, map: ManualHashMap) {
    if (operation.endsWith("-")) {
        operation.trimEnd('-').let { map.remove(it) }
    } else {
        operation.split("=").let { map.add(it[0], it[1].toInt()) }
    }
}

private fun hash(value: String) = value.toCharArray()
    .fold(0) { acc, char -> ((acc + char.code) * 17) % 256 }

private class ManualHashMap {
    val data = Array<LinkedHashMap<String, Int>>(256) { LinkedHashMap() }

    fun add(label: String, value: Int) {
        val hash = hash(label)
        data[hash][label] = value
    }

    fun remove(label: String) {
        val hash = hash(label)
        data[hash].remove(label)
    }
}