package io.vanny96.adventofcode.util

fun textFromResource(resourcePath: String): String? =
    object {}::class.java.classLoader.getResource(resourcePath)?.readText()

fun LongRange.contains(other: LongRange) =
    this.contains(other.first) || this.contains(other.last)

fun LongRange.union(other: LongRange) =
    if (this.contains(other) || other.contains(this)) {
        listOf(kotlin.math.min(first, other.first)..kotlin.math.max(last, other.last))
    } else {
        listOf(this, other)
    }

fun LongRange.intersect(other: LongRange) =
    if (this.contains(other) || other.contains(this)) {
        kotlin.math.max(first, other.first)..kotlin.math.min(last, other.last)
    } else {
        null
    }

fun LongRange.subtract(other: LongRange) =
    if (this.contains(other) || other.contains(this)) {
        listOf(
            this.first..<other.first,
            other.last + 1..this.last
        ).filterNot { it.isEmpty() }
    } else {
        listOf(this)
    }

fun LongRange.shift(shift: Long) = (this.first + shift)..(this.last + shift)