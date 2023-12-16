enum class BeamDirection(val modifier: Pair<Int, Int>) {
    Up(-1 to 0),
    Left(0 to -1),
    Down(1 to 0),
    Right(0 to 1);

    fun rightMirrorShift() = when (this) {
        Up -> Right
        Left -> Down
        Down -> Left
        Right -> Up
    }

    fun lefttMirrorShift() = when (this) {
        Up -> Left
        Left -> Up
        Down -> Right
        Right -> Down
    }
}