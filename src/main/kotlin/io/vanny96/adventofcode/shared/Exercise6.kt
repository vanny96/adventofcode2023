package io.vanny96.adventofcode.shared

data class RaceData(val time: Long, val distance: Long) {
    companion object {
        fun multipleFromInputs(inputs: String): List<RaceData> {
            val timesValuesInputs = inputs.lines()[0].removePrefix("Time:").trim()
            val distancesValuesInputs = inputs.lines()[1].removePrefix("Distance:").trim()

            val times = timesValuesInputs.split(" +".toRegex()).map { it.toLong() }
            val distances = distancesValuesInputs.split(" +".toRegex()).map { it.toLong() }

            return times.indices
                .map { RaceData(times[it], distances[it]) }
        }

        fun fromInputs(inputs: String): RaceData {
            val timesValuesInputs = inputs.lines()[0].removePrefix("Time:").trim()
            val distancesValuesInputs = inputs.lines()[1].removePrefix("Distance:").trim()

            val times = timesValuesInputs.split(" +".toRegex())
            val distances = distancesValuesInputs.split(" +".toRegex())

            return RaceData(
                times.reduce{acc, s -> "$acc$s"}.toLong(),
                distances.reduce{acc, s -> "$acc$s"}.toLong(),
            )
        }
    }
}
