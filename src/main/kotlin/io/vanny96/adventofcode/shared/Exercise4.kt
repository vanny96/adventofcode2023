package io.vanny96.adventofcode.shared


data class Game4(val id: Int, val playerNumbers: Set<Int>, val winningNumbers: Set<Int>) {

    val winningPlayerNumbers = playerNumbers.intersect(winningNumbers)

    companion object {
        fun fromGameInfo(info: String): Game4 {
            val (gameInfo, gameSetsInfo) = info.split(":").let { it[0] to it[1] }
            val (playerNumbersInfo, winningNumbersInfo) = gameSetsInfo.split("|").let { it[0] to it[1] }

            val gameId = "\\d+".toRegex().find(gameInfo)!!.value.toInt()
            val playerNumbers = playerNumbersInfo.trim().split(" +".toRegex()).map { it.toInt() }.toSet()
            val winningNumbers = winningNumbersInfo.trim().split(" +".toRegex()).map { it.toInt() }.toSet()

            return Game4(gameId, playerNumbers, winningNumbers)
        }
    }
}
