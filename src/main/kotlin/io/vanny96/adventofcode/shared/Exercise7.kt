package io.vanny96.adventofcode.shared

data class CardGame(val cards: List<Card>, val bet: Int)

interface Card {
    val stringRepr: String
    val value: Int
    val isJoker: Boolean
}

enum class WinningPosition() {
    FiveOfAKind,
    FourOfAKind,
    FullHouse,
    ThreeOfAKind,
    DoublePair,
    Pair,
    HighCard;

    companion object {
        fun getFromCards(cards: List<Card>, jokers: Int = 0): WinningPosition {
            if (jokers == 5) return FiveOfAKind

            if (isFiveOfAKind(cards, jokers)) return FiveOfAKind
            if (isFourOfAKind(cards, jokers)) return FourOfAKind
            if (isFullHouse(cards)) return FullHouse
            if (isThreeOfAKind(cards, jokers)) return ThreeOfAKind
            if (isDoublePair(cards, jokers)) return DoublePair
            if (isPair(cards, jokers)) return Pair
            return HighCard
        }

        private fun isFiveOfAKind(cards: List<Card>, jokers: Int) = cardsMatchSize(cards, jokers, 5)

        private fun isFourOfAKind(cards: List<Card>, jokers: Int) = cardsMatchSize(cards, jokers, 4)

        private fun isFullHouse(cards: List<Card>): Boolean {
            return cards.groupBy { it }.size == 2
        }

        private fun isThreeOfAKind(cards: List<Card>, jokers: Int) = cardsMatchSize(cards, jokers, 3)

        private fun isDoublePair(cards: List<Card>, jokers: Int): Boolean {
            val pairs = cards.groupBy { it }.filter { it.value.size == 2 }

            return pairs.size + jokers == 2
        }

        private fun isPair(cards: List<Card>, jokers: Int) =
            if (jokers != 0) true else cardsMatchSize(cards, jokers, 2)


        private fun cardsMatchSize(cards: List<Card>, jokers: Int, size: Int) = cards
            .groupBy { it }
            .filter { it.value.size + jokers == size }
            .isNotEmpty()
    }
}