package feature.expenses.feature.envelope_details.data

import kotlinx.datetime.LocalDate
import util.getLastDayOfMonth

sealed class ExpensePeriod {
    abstract fun getPeriods(): Pair<LocalDate, LocalDate>
    data class Year(val year: Int): ExpensePeriod() {
        override fun getPeriods(): Pair<LocalDate, LocalDate> {
            val startPeriod = LocalDate(year = year, monthNumber = 1, dayOfMonth = 1)
            val endPeriod = LocalDate(year = year, monthNumber = 12, dayOfMonth = getLastDayOfMonth(year, 12))
            return Pair(startPeriod, endPeriod)
        }
    }
    data class Month(val year: Int, val month: Int): ExpensePeriod() {
        override fun getPeriods(): Pair<LocalDate, LocalDate> {
            val startPeriod = LocalDate(year = year, monthNumber = month, dayOfMonth = 1)
            val endPeriod = LocalDate(year = year, monthNumber = month, dayOfMonth = getLastDayOfMonth(year, month))
            return Pair(startPeriod, endPeriod)
        }
    }
}