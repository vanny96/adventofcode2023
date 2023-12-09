package io.vanny96.adventofcode.shared

data class MapEntry(val key: String, val left: String, val right: String) {
    companion object {
        fun parseFromLine(line: String): MapEntry {
            val (keyInfo, destinationsInfo) = line.split("=").let { it[0] to it[1] }

            val key = keyInfo.trim()
            val (left, right) = destinationsInfo.trim()
                .removeSurrounding("(", ")")
                .split(", ").let { it[0] to it[1] }

            return MapEntry(key, left, right)
        }
    }
}