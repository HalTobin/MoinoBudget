package util

import androidx.compose.runtime.Composable
import data.repository.AppPreferences
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.april_abbreviation
import moinobudget.composeapp.generated.resources.august_abbreviation
import moinobudget.composeapp.generated.resources.december_abbreviation
import moinobudget.composeapp.generated.resources.february_abbreviation
import moinobudget.composeapp.generated.resources.january_abbreviation
import moinobudget.composeapp.generated.resources.july_abbreviation
import moinobudget.composeapp.generated.resources.june_abbreviation
import moinobudget.composeapp.generated.resources.march_abbreviation
import moinobudget.composeapp.generated.resources.november_abbreviation
import moinobudget.composeapp.generated.resources.october_abbreviation
import moinobudget.composeapp.generated.resources.september_abbreviation
import org.jetbrains.compose.resources.stringResource

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
        val endPeriod = safeLocalDate(timestamp.year, timestamp.monthNumber, maxDay)
        Pair(startPeriod, endPeriod)
    }
}

fun safeLocalDate(year: Int, month: Int, day: Int): LocalDate {
    return runCatching {
        LocalDate(year, month, day)
    }.getOrElse {
        val lastDay = getValidDayOfMonth(year, month, day)
        LocalDate(year, month, lastDay)
    }
}

fun getLastDayOfMonth(year: Int, month: Int): Int =
    when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> throw IllegalArgumentException("Invalid month: $month")
    }

fun getValidDayOfMonth(year: Int, month: Int, day: Int): Int {
    // Check the number of days in the month, considering leap years for February
    val lastDayOfMonth = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> throw IllegalArgumentException("Invalid month: $month")
    }
    return if (day > lastDayOfMonth) lastDayOfMonth else day
}

fun isLeapYear(year: Int): Boolean =
    (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

@Composable
fun formatDate(date: LocalDate, preferences: AppPreferences): String {
    //val monthAbbreviation = getMonthAbbreviationByMonthNumber(date.monthNumber)
    /*val customFormat = LocalDate.Format {
        // TODO - Take into account different date formats
        //dayOfMonth(); char('/'); monthNumber(); char('/'); year()
        monthAbbreviation; char(' '); dayOfMonth()
    }*/
    return "${getMonthAbbreviationByMonthNumber(date.monthNumber)} ${date.dayOfMonth}"
}

@Composable
fun getMonthAbbreviationByMonthNumber(monthNumber: Int) = when (monthNumber) {
    1 -> stringResource(Res.string.january_abbreviation)
    2 -> stringResource(Res.string.february_abbreviation)
    3 -> stringResource(Res.string.march_abbreviation)
    4 -> stringResource(Res.string.april_abbreviation)
    5 -> stringResource(Res.string.march_abbreviation)
    6 -> stringResource(Res.string.june_abbreviation)
    7 -> stringResource(Res.string.july_abbreviation)
    8 -> stringResource(Res.string.august_abbreviation)
    9 -> stringResource(Res.string.september_abbreviation)
    10 -> stringResource(Res.string.october_abbreviation)
    11 -> stringResource(Res.string.november_abbreviation)
    12 -> stringResource(Res.string.december_abbreviation)
    else -> throw IllegalArgumentException("monthNumber should be in the interval: [1, 12], entered: $monthNumber")
}