package feature.dashboard.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.ExpenseUI
import presentation.data.IncomeOrOutcome

class DashboardViewModel(): ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        // Load expenses and payments...
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val dummyDashboard = DashboardState(
            disposableIncomesMonthly = 1275.0f,
            disposableIncomesAnnual = 14250.0f,
            upcomingPaymentsMonthly = 500.0f,
            upcomingPaymentsAnnual = 6750.0f,
            expenses = listOf(
                ExpenseUI(1, 15f, IncomeOrOutcome.Outcome,"Subscription A", ExpenseIcon.Transport, ExpenseFrequency.Monthly, false, 6, now, now.minusMonthsCompat(1), listOf(
                    Color.Red, Color.Green)),
                ExpenseUI(2, 235f, IncomeOrOutcome.Outcome,"Electricity", ExpenseIcon.Transport, ExpenseFrequency.Monthly, true, 2, now.plusDaysCompat(5), now.minusMonthsCompat(1), listOf(Color.Yellow)),
                ExpenseUI(3, 700f, IncomeOrOutcome.Outcome, "Rent", ExpenseIcon.Transport, ExpenseFrequency.Monthly, false, 17, now.plusDaysCompat(10), now.minusMonthsCompat(1), listOf(
                    Color.Green)),
                ExpenseUI(4, 20f, IncomeOrOutcome.Outcome, "Internet", ExpenseIcon.Transport, ExpenseFrequency.Monthly, true, 2, now.plusDaysCompat(2), now.minusMonthsCompat(1), listOf(Color.Red)),
                ExpenseUI(5, 150f, IncomeOrOutcome.Outcome, "Water", ExpenseIcon.Transport, ExpenseFrequency.Monthly, false, 1, now.plusDaysCompat(7), now.minusMonthsCompat(1), listOf(
                    Color.Yellow))
            )
        )

        _state.update { dummyDashboard }
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