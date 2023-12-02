package io.vanny96.adventofcode.util

fun textFromResource(resourcePath: String): String? =
    object {}::class.java.classLoader.getResource(resourcePath)?.readText()