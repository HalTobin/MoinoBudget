package feature.dashboard.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import feature.dashboard.data.DueExpense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.ExpenseUI

class DashboardViewModel(): ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        // Load expenses and payments...
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val _dummyDashboard = DashboardState(
            upcomingPayments = 1275.0f,
            disposableIncomes = 500.0f,
            dueExpenses = listOf(
                DueExpense("Subscription A", 3, 15.0, Color.Red),
                DueExpense("Electricity", 5, 60.0, Color.Blue),
                DueExpense("Rent", 10, 1200.0, Color.Green),
                DueExpense("Internet", 2, 45.0, Color.Yellow),
                DueExpense("Water", 7, 30.0, Color.Cyan)
            ),
            expenses = listOf(
                ExpenseUI(1, "Subscription A", ExpenseIcon.Transport, ExpenseFrequency.Monthly, false, now, now.minusMonthsCompat(1)),
                ExpenseUI(2, "Electricity", ExpenseIcon.Transport, ExpenseFrequency.Monthly, true, now.plusDaysCompat(5), now.minusMonthsCompat(1)),
                ExpenseUI(3, "Rent", ExpenseIcon.Transport, ExpenseFrequency.Monthly, false, now.plusDaysCompat(10), now.minusMonthsCompat(1)),
                ExpenseUI(4, "Internet", ExpenseIcon.Transport, ExpenseFrequency.Monthly, true, now.plusDaysCompat(2), now.minusMonthsCompat(1)),
                ExpenseUI(5, "Water", ExpenseIcon.Transport, ExpenseFrequency.Monthly, false, now.plusDaysCompat(7), now.minusMonthsCompat(1))
            )
        )

        _state.update { _dummyDashboard }
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            else -> {}
        }
    }

    // Extension functions to handle date operations
    private fun LocalDate.plusDaysCompat(days: Int): LocalDate {
        return this.plus(DatePeriod(days = days))
    }

    private fun LocalDate.minusMonthsCompat(months: Int): LocalDate {
        return this.minus(DatePeriod(months = months))
    }

}