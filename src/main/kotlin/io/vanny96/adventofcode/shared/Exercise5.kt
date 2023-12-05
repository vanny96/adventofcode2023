package io.vanny96.adventofcode.shared

data class TransitionMapRange(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long) {
    val sourceRange = sourceRangeStart..sourceRangeStart + rangeLength
    val sourceDestinationModifier = destinationRangeStart - sourceRangeStart
}

data class TransitionMap(val mapRanges: Set<TransitionMapRange>) {
    fun transitionValue(value: Long): Long {
        val matchingMap = mapRanges.find { it.sourceRange.contains(value) } ?: return value
        val gap = value - matchingMap.sourceRangeStart
        return matchingMap.destinationRangeStart + gap
    }
}