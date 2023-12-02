package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.Game
import io.vanny96.adventofcode.shared.GameSet
import io.vanny96.adventofcode.util.textFromResource

private val limits = GameSet(blue = 14, red = 12, green = 13)

fun main() {
    val exerciseData = textFromResource("inputs/exercise_2.txt") ?: return
    val result = exerciseData.lines()
        .map { Game.fromGameInfo(it) }
        .filter { possibleWithLimits(it, limits) }
        .sumOf { it.id }

    println(result)
}

private fun possibleWithLimits(game: Game, limits: GameSet) =
    game.revealedSets.all { it.blue <= limits.blue && it.red <= limits.red && it.green <= limits.green }





