package io.vanny96.adventofcode.exercises

import io.vanny96.adventofcode.shared.Card
import io.vanny96.adventofcode.shared.CardGame
import io.vanny96.adventofcode.shared.WinningPosition
import io.vanny96.adventofcode.util.textFromResource

fun main() {
    val exerciseData = textFromResource("inputs/exercise_7.txt") ?: return
    val games = exerciseData.lines()
        .map { lineToBet(it) }
        .map { WinningPosition.getFromCards(it.cards) to it }

    val rankedGames = games
        .sortedWith(
            compareByDescending<Pair<WinningPosition, CardGame>> { it.first.ordinal }
                .thenComparing({ it.second.cards }, handComparator)
        ).map { it.second }

    val result = rankedGames
        .mapIndexed { index, game -> (index + 1) * game.bet }
        .sum()

    println(result)
}

private val handComparator: Comparator<List<Card>> =
    compareBy(
        { it[0].value },
        { it[1].value },
        { it[2].value },
        { it[3].value },
        { it[4].value }
    )

private fun lineToBet(line: String): CardGame {
    val (cardInfo, betInfo) = line.split(" ").let { it[0] to it[1] }

    val cards = cardInfo.toCharArray()
        .map { BaseCard.fromValue(it.toString()) }

    val bet = betInfo.toInt()

    return CardGame(cards, bet)
}


enum class BaseCard(
    override val stringRepr: String,
    override val value: Int
) : Card {
    Ace("A", 14),
    King("K", 13),
    Queen("Q", 12),
    Jack("J", 11),
    Ten("T", 10),
    Nine("9", 9),
    Eight("8", 8),
    Seven("7", 7),
    Six("6", 6),
    Five("5", 5),
    Four("4", 4),
    Three("3", 3),
    Two("2", 2);

    override val isJoker = false

    companion object {
        fun fromValue(value: String): BaseCard {
            return BaseCard.entries.find { it.stringRepr == value }!!
        }
    }
}


