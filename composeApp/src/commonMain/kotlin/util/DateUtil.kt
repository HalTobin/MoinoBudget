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