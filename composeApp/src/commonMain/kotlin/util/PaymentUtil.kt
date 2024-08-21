package util

import data.db.table.Expense
import kotlinx.datetime.*
import presentation.data.ExpenseFrequency

fun calculateNextPayment(expense: Expense): LocalDate {
    val currentDate = Clock.System.now().epochSeconds.toLocalDate()

    return when (expense.frequency) {
        ExpenseFrequency.Monthly.id -> { // Monthly
            // Determine the next occurrence this month or next month
            val thisMonthPayment = safeLocalDate(currentDate.year, currentDate.monthNumber, expense.day)
            if (thisMonthPayment >= currentDate) {
                thisMonthPayment
            } else {
                thisMonthPayment.plus(DatePeriod(months = 1))
            }
        }
        ExpenseFrequency.Annually.id -> { // Annually
            // Determine the month from the monthOffset
            val targetMonth = expense.monthOffset ?: 1 // Default to January if no monthOffset is provided
            var annualPayment = safeLocalDate(currentDate.year, targetMonth, expense.day)

            // If this year's payment has passed, move to the next year
            if (annualPayment < currentDate) {
                annualPayment = annualPayment.plus(DatePeriod(years = 1))
            }

            annualPayment
        }

        else -> throw IllegalArgumentException("Unsupported frequency: ${expense.frequency}")
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

fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}