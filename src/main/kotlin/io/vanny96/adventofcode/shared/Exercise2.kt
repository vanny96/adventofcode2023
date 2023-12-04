package io.vanny96.adventofcode.shared


data class Game2(val id: Int, val revealedSets: List<GameSet2>) {
    companion object {
        fun fromGameInfo(info: String): Game2 {
            val (gameInfo, gameSetsInfo) = info.split(":").let { it[0] to it[1] }

            val gameId = "Game (.*)".toRegex().find(gameInfo)!!.groupValues[1].toInt()
            val gameSets = gameSetsInfo.split(";").map { GameSet2.parseGameSetInfo(it) }

            return Game2(gameId, gameSets)
        }
    }
}

data class GameSet2(val blue: Int, val red: Int, val green: Int) {
    companion object {
        fun parseGameSetInfo(info: String): GameSet2 {
            fun getColorValue(color: String) = "([\\d]+) $color".toRegex().find(info)?.groupValues?.get(1)?.toInt() ?: 0

            return GameSet2(getColorValue("blue"), getColorValue("red"), getColorValue("green"))
        }
    }
}