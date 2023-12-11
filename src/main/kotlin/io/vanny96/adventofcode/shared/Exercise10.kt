package io.vanny96.adventofcode.shared


enum class Direction(val modifier: Pair<Int, Int>) {
    Left(-1 to 0), Up(0 to -1), Right(1 to 0), Down(0 to 1);

    fun opposite() = when (this) {
        Left -> Right
        Up -> Down
        Right -> Left
        Down -> Up
    }

    fun turnRight() = when (this) {
        Left -> Up
        Up -> Right
        Right -> Down
        Down -> Left
    }

    fun turnLeft() = turnRight().opposite()
}

enum class Pipe(val sign: Char, val allowedDirections: Set<Direction>) {
    Vertical('|', setOf(Direction.Up, Direction.Down)),
    Horizontal('-', setOf(Direction.Left, Direction.Right)),
    NorthEast('L', setOf(Direction.Up, Direction.Right)),
    NorthWest('J', setOf(Direction.Up, Direction.Left)),
    SouthEast('F', setOf(Direction.Down, Direction.Right)),
    SouthWest('7', setOf(Direction.Down, Direction.Left)),
    Ground('.', emptySet()), Starting('S', Direction.entries.toSet());

    fun checkRight(direction: Direction): Set<Pair<Int, Int>> = when (this) {
        Vertical -> setOf(direction.turnRight().modifier)
        Horizontal -> setOf(direction.turnRight().modifier)
        NorthEast -> if (direction == Direction.Left) setOf(Direction.Right.modifier + Direction.Up.modifier)
        else setOf(Direction.Down.modifier, Direction.Left.modifier, Direction.Down.modifier + Direction.Left.modifier)

        NorthWest -> if (direction == Direction.Down) setOf(Direction.Left.modifier + Direction.Up.modifier)
        else setOf(
            Direction.Down.modifier, Direction.Right.modifier, Direction.Down.modifier + Direction.Right.modifier
        )

        SouthEast -> if (direction == Direction.Up) setOf(Direction.Right.modifier + Direction.Down.modifier)
        else setOf(Direction.Up.modifier, Direction.Left.modifier, Direction.Up.modifier + Direction.Left.modifier)

        SouthWest -> if (direction == Direction.Right) setOf(Direction.Left.modifier + Direction.Down.modifier)
        else setOf(Direction.Up.modifier, Direction.Right.modifier, Direction.Up.modifier + Direction.Right.modifier)

        Ground -> setOf()
        Starting -> setOf()
    }

    fun checkLeft(direction: Direction): Set<Pair<Int, Int>> = when (this) {
        Vertical -> setOf(direction.turnLeft().modifier)
        Horizontal -> setOf(direction.turnLeft().modifier)

        NorthEast -> if (direction == Direction.Down) setOf(Direction.Right.modifier + Direction.Up.modifier)
        else setOf(Direction.Down.modifier, Direction.Left.modifier, Direction.Down.modifier + Direction.Left.modifier)

        NorthWest -> if (direction == Direction.Right) setOf(Direction.Left.modifier + Direction.Up.modifier)
        else setOf(
            Direction.Down.modifier, Direction.Right.modifier, Direction.Down.modifier + Direction.Right.modifier
        )

        SouthEast -> if (direction == Direction.Left) setOf(Direction.Right.modifier + Direction.Down.modifier)
        else setOf(Direction.Up.modifier, Direction.Left.modifier, Direction.Up.modifier + Direction.Left.modifier)

        SouthWest -> if (direction == Direction.Up) setOf(Direction.Left.modifier + Direction.Down.modifier)
        else setOf(Direction.Up.modifier, Direction.Right.modifier, Direction.Up.modifier + Direction.Right.modifier)

        Ground -> setOf()
        Starting -> setOf()
    }

    companion object {
        fun fromSign(sigh: Char): Pipe {
            return Pipe.entries.find { it.sign == sigh }!!
        }
    }
}

data class PositionAndDistance(val position: Pair<Int, Int>, val distance: Long)

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = (this.first + other.first) to (this.second + other.second)