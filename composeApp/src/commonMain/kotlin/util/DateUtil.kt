package util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
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