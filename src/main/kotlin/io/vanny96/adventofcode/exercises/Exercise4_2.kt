package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.Game4
import io.vanny96.adventofcode.util.textFromResource
import kotlin.math.max

fun main() {
    val exerciseData = textFromResource("inputs/exercise_4.txt") ?: return
    val games = exerciseData.lines()
        .map { Game4.fromGameInfo(it) }

    val copiesCounter: MutableMap<Int, Int> = mutableMapOf()
    var highestCopyId = 1
    for (game in games) {
        val numberOfCopies = copiesCounter[game.id] ?: 0
        val winningNumbers = game.playerNumbers.intersect(game.winningNumbers).size
        val affectedGames = game.id+1..game.id+winningNumbers

        affectedGames.forEach { copiesCounter[it] = (copiesCounter[it] ?: 0) + (1 + numberOfCopies) }
        highestCopyId = max(highestCopyId, affectedGames.last)
    }

    val result = copiesCounter
        .map{ it.value }
        .sum() + games.size

    println(result)
}
