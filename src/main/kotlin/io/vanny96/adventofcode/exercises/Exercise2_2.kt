package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.Game
import io.vanny96.adventofcode.shared.GameSet
import io.vanny96.adventofcode.util.textFromResource
import kotlin.math.max

private val limits = GameSet(blue = 14, red = 12, green = 13)

fun main() {
    val exerciseData = textFromResource("inputs/exercise_2.txt") ?: return
    val result = exerciseData.lines()
        .map { Game.fromGameInfo(it) }
        .map { getMinimumViableSet(it) }
        .sumOf { it.blue * it.red * it.green }

    println(result)
}

fun getMinimumViableSet(game: Game) = game.revealedSets.reduce { acc, gameSet ->
        GameSet(
            blue = max(acc.blue, gameSet.blue),
            red = max(acc.red, gameSet.red),
            green = max(acc.green, gameSet.green)
        )
    }






