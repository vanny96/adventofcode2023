package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.util.textFromResource

private val limits = GameSet(blue = 14, red = 12, green = 13)

fun main() {
    val exerciseData = textFromResource("inputs/exercise_2.txt") ?: return
    val result = exerciseData.lines()
        .map { parseGameInfo(it) }
        .filter { possibleWithLimits(it, limits) }
        .sumOf { it.id }

    println(result)
}

private fun possibleWithLimits(game: Game, limits: GameSet) =
    game.revealedSets.all { it.blue <= limits.blue && it.red <= limits.red && it.green <= limits.green }

private fun parseGameInfo(info: String): Game {
    val gameInfo = info.split(":")[0]
    val gameId = "Game (.*)".toRegex().find(gameInfo)?.groupValues?.get(1)?.toInt()!!

    val gameSetsInfo = info.split(":")[1]
    val gameSets = gameSetsInfo.split(";").map { parseGameSetInfo(it) }

    return Game(gameId, gameSets)
}

private fun parseGameSetInfo(info: String): GameSet {
    fun getColorValue(color: String) = "([\\d]+) $color".toRegex().find(info)?.groupValues?.get(1)?.toInt() ?: 0

    return GameSet(getColorValue("blue"), getColorValue("red"), getColorValue("green"))
}

private data class Game(val id: Int, val revealedSets: List<GameSet>)

private data class GameSet(val blue: Int, val red: Int, val green: Int)



