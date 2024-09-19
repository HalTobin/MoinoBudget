package util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun Long.toLocalDate(): LocalDate {
    // Convert the timestamp to an Instant
    val instant = Instant.fromEpochMilliseconds(this)

    // Convert the Instant to a LocalDateTime in the system's default time zone
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    // Extract the LocalDate part
    return localDateTime.date
}

fun Int?.getMaxDay(): Int = when (this) {
    1, 3, 5, 7, 8, 10, 12, null -> 31
    4, 6, 9, 11 -> 30
    2 -> 29
    else -> 31
}

fun LocalDate.toEpochMillisecond(): Long =
    LocalDateTime(this, LocalTime(0,0,0,0))
        .toInstant(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()

fun getPeriodFromTimestamp(timestamp: LocalDate, isYear: Boolean): Pair<LocalDate, LocalDate> {
    return if (isYear) {
        // Start of the year
        val startPeriod = LocalDate(timestamp.year, 1, 1)
        // End of the year
        val endPeriod = LocalDate(timestamp.year, 12, 31)
        Pair(startPeriod, endPeriod)
    }
    else {
        // Start of the month
        val startPeriod = LocalDate(timestamp.year, timestamp.monthNumber, 1)
        // End of the month (use lengthOfMonth for the last day of the month)
        val maxDay = timestamp.monthNumber.getMaxDay()
        val endPeriod = try {
            LocalDate(timestamp.year, timestamp.monthNumber, maxDay)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            LocalDate(timestamp.year, timestamp.monthNumber, maxDay-1)
        }
        Pair(startPeriod, endPeriod)
    }
}