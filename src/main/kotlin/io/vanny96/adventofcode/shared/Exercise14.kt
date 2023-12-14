enum class RockShape(val value: Char) {
    Round('O'), Cube('#');

    companion object {
        fun fromValue(value: Char): RockShape? {
            return entries.find { it.value == value }
        }
    }
}