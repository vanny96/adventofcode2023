package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.Game2
import io.vanny96.adventofcode.shared.GameSet2
import io.vanny96.adventofcode.util.textFromResource
import kotlin.math.max

fun main() {
    val exerciseData = textFromResource("inputs/exercise_2.txt") ?: return
    val result = exerciseData.lines()
        .map { Game2.fromGameInfo(it) }
        .map { getMinimumViableSet(it) }
        .sumOf { it.blue * it.red * it.green }

    println(result)
}

fun getMinimumViableSet(game: Game2) = game.revealedSets.reduce { acc, gameSet ->
        GameSet2(
            blue = max(acc.blue, gameSet.blue),
            red = max(acc.red, gameSet.red),
            green = max(acc.green, gameSet.green)
        )
    }






