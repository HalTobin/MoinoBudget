package util

import data.db.table.BudgetOperation
import kotlinx.datetime.*
import presentation.data.ExpenseFrequency

fun calculateNextPayment(budgetOperation: BudgetOperation): LocalDate {
    val currentDate = Clock.System.now().toEpochMilliseconds().toLocalDate()

    return when (budgetOperation.frequency) {
        ExpenseFrequency.Monthly.id -> { // Monthly
            // Determine the next occurrence this month or next month
            val thisMonthPayment = safeLocalDate(currentDate.year, currentDate.monthNumber, budgetOperation.day)
            if (thisMonthPayment >= currentDate) {
                thisMonthPayment
            } else {
                thisMonthPayment.plus(DatePeriod(months = 1))
            }
        }
        ExpenseFrequency.Quarterly.id -> { // Quarterly
            val quarterMonths = listOf(1, 4, 7, 10).map { it + budgetOperation.monthOffset!! }
            return calculateNextPaymentForMultipleMonths(quarterMonths, budgetOperation, currentDate)
        }
        ExpenseFrequency.Biannually.id -> { // Biannual
            val biannualMonths = listOf(1, 7).map { it + budgetOperation.monthOffset!! }
            return calculateNextPaymentForMultipleMonths(biannualMonths, budgetOperation, currentDate)
        }
        ExpenseFrequency.Annually.id -> { // Annually
            // Determine the month from the monthOffset
            val targetMonth = budgetOperation.monthOffset?.plus(1) ?: 1 // Default to January if no monthOffset is provided
            var annualPayment = safeLocalDate(currentDate.year, targetMonth, budgetOperation.day)

            // If this year's payment has passed, move to the next year
            if (annualPayment < currentDate) {
                annualPayment = annualPayment.plus(DatePeriod(years = 1))
            }

            annualPayment
        }

        else -> throw IllegalArgumentException("Unsupported frequency: ${budgetOperation.frequency}")
    }
}

fun calculateNextPaymentForMultipleMonths(targetMonths: List<Int>, budgetOperation: BudgetOperation, currentDate: LocalDate): LocalDate {
    val year = currentDate.year
    for (month in targetMonths) {
        val adjustedMonth = (month - 1) % 12 + 1 // Adjust month to valid range (1-12)
        val adjustedYear = year + (month - 1) / 12 // Adjust year if month exceeds December
        val validDay = getValidDayOfMonth(adjustedYear, adjustedMonth, budgetOperation.day)
        val paymentDate = LocalDate(adjustedYear, adjustedMonth, validDay)

        if (paymentDate >= currentDate) {
            return paymentDate
        }
    }

    // If all target months for this year have passed, calculate for the next year's first target month
    val firstMonthNextYear = targetMonths.first()
    val adjustedMonth = (firstMonthNextYear - 1) % 12 + 1
    val adjustedYear = year + 1 + (firstMonthNextYear - 1) / 12
    val validDay = getValidDayOfMonth(adjustedYear, adjustedMonth, budgetOperation.day)
    return LocalDate(adjustedYear, adjustedMonth, validDay)
}