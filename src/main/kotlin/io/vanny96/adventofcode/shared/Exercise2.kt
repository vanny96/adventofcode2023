package io.vanny96.adventofcode.shared


data class Game(val id: Int, val revealedSets: List<GameSet>) {
    companion object {
        fun fromGameInfo(info: String): Game {
            val gameInfo = info.split(":")[0]
            val gameId = "Game (.*)".toRegex().find(gameInfo)?.groupValues?.get(1)?.toInt()!!

            val gameSetsInfo = info.split(":")[1]
            val gameSets = gameSetsInfo.split(";").map { GameSet.parseGameSetInfo(it) }

            return Game(gameId, gameSets)
        }
    }
}

data class GameSet(val blue: Int, val red: Int, val green: Int) {
    companion object {
        fun parseGameSetInfo(info: String): GameSet {
            fun getColorValue(color: String) = "([\\d]+) $color".toRegex().find(info)?.groupValues?.get(1)?.toInt() ?: 0

            return GameSet(getColorValue("blue"), getColorValue("red"), getColorValue("green"))
        }
    }
}