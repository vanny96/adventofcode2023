package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.Game4
import io.vanny96.adventofcode.util.textFromResource

fun main() {
    val exerciseData = textFromResource("inputs/exercise_4.txt") ?: return
    val games = exerciseData.lines()
        .map { Game4.fromGameInfo(it) }

    val copiesMap = games
        .asSequence()
        .map { it.winningPlayerNumbers.size }
        .mapIndexed { index, winningResult -> (index + 1)..(index + winningResult) }
        .map { it.associateWith { 1 } }
        .reduceIndexed { index, acc, map -> mergeCardMaps(index, acc, map) }

    val result = copiesMap
        .map { it.value }
        .sum() + games.size

    println(result)
}

private fun mergeCardMaps(gameId: Int, accumulationMap: Map<Int, Int>, cardResults: Map<Int, Int>): Map<Int, Int> {
    val cardCopies = accumulationMap[gameId] ?: 0
    val modifiedMap = cardResults.map { it.key to it.value * (1 + cardCopies) }.toMap()

    return (accumulationMap.toList() + modifiedMap.toList())
        .groupBy({ it.first }, { it.second })
        .map { entryKey -> entryKey.key to entryKey.value.sum() }
        .toMap()
}

private fun firstApproach() {
    val exerciseData = textFromResource("inputs/exercise_4.txt") ?: return
    val games = exerciseData.lines()
        .map { Game4.fromGameInfo(it) }

    val copiesCounter: MutableMap<Int, Int> = mutableMapOf()
    for (game in games) {
        val numberOfCopies = copiesCounter[game.id] ?: 0
        val winningNumbers = game.winningPlayerNumbers.size
        val affectedGames = (game.id + 1)..(game.id + winningNumbers)

        affectedGames.forEach { copiesCounter[it] = (copiesCounter[it] ?: 0) + (1 + numberOfCopies) }
    }

    val result = copiesCounter
        .map { it.value }
        .sum() + games.size



    println(result)
}
