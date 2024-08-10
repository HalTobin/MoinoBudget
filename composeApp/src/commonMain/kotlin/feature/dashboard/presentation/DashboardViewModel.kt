package feature.dashboard.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import feature.dashboard.data.MonthYearPair
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
import presentation.data.BudgetStyle
import presentation.data.BudgetUI
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.ExpenseUI
import presentation.data.IncomeOrOutcome
import presentation.data.LabelUI

class DashboardViewModel(): ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        // Load expenses and payments...
        val expenses = getExpenses()
        val labels = getLabels()
        val yearIncomes = expenses
            .filter { it.type == IncomeOrOutcome.Income }
            .sumOf { it.amount * it.frequency.multiplier.toDouble() }
        val yearOutcomes = expenses
            .filter { it.type == IncomeOrOutcome.Outcome }
            .sumOf { it.amount * it.frequency.multiplier.toDouble() }
        val monthPayments = expenses
            .filter { it.frequency == ExpenseFrequency.Monthly }
            .sumOf { it.amount.toDouble() }
        val toPutAside = expenses
            .filter { it.frequency != ExpenseFrequency.Monthly }
            .sumOf { it.amount * it.frequency.multiplier.toDouble() }

        val dummyDashboard = DashboardState(
            budgets = listOf(BudgetUI(
                id = 0,
                name = "My budget",
                labels = labels,
                expenses = expenses,
                style = BudgetStyle.CitrusJuice,
                rawIncomes = MonthYearPair(annual = yearIncomes),
                toPutAside = MonthYearPair(annual = toPutAside),
                monthPayments = MonthYearPair(annual = monthPayments),
                disposableIncomes = MonthYearPair(annual = yearIncomes - yearOutcomes),
                upcomingPayments = MonthYearPair(annual = yearOutcomes),
            ), BudgetUI(
                id = 0,
                name = "My secondary budget",
                labels = labels.subList(1, labels.size),
                expenses = expenses,
                style = BudgetStyle.Winter,
                rawIncomes = MonthYearPair(annual = yearIncomes / 2),
                toPutAside = MonthYearPair(annual = toPutAside / 2),
                monthPayments = MonthYearPair(annual = monthPayments / 2),
                disposableIncomes = MonthYearPair(annual = (yearIncomes - yearOutcomes) / 2),
                upcomingPayments = MonthYearPair(annual = yearOutcomes / 2),
            )
        ))

        _state.update { dummyDashboard }
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            else -> {}
        }
    }

    private fun getExpenses(): List<ExpenseUI> {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return listOf(
            ExpenseUI(1, 15f, IncomeOrOutcome.Outcome,"Netflix", ExpenseIcon.Film, ExpenseFrequency.Monthly, false, 6, now, now.minusMonthsCompat(1), getLabels()),
            ExpenseUI(2, 235f, IncomeOrOutcome.Outcome,"Electricity", ExpenseIcon.Electricity, ExpenseFrequency.Monthly, true, 2, now.plusDaysCompat(5), now.minusMonthsCompat(1), listOf(getLabels()[1])),
            ExpenseUI(3, 700f, IncomeOrOutcome.Outcome, "Rent", ExpenseIcon.Housing, ExpenseFrequency.Monthly, false, 17, now.plusDaysCompat(10), now.minusMonthsCompat(1), getLabels()),
            ExpenseUI(4, 20f, IncomeOrOutcome.Outcome, "Internet", ExpenseIcon.Internet, ExpenseFrequency.Monthly, true, 2, now.plusDaysCompat(2), now.minusMonthsCompat(1), listOf(getLabels()[0], getLabels()[2])),
            ExpenseUI(5, 150f, IncomeOrOutcome.Outcome, "Water", ExpenseIcon.Water, ExpenseFrequency.Monthly, false, 1, now.plusDaysCompat(7), now.minusMonthsCompat(1), getLabels().subList(0, 2)),
            ExpenseUI(6, 1656f, IncomeOrOutcome.Income, "Salary", ExpenseIcon.Incomes, ExpenseFrequency.Monthly, false, 17, now.plusDaysCompat(10), now.minusMonthsCompat(1), getLabels()),
            ExpenseUI(7, 185f, IncomeOrOutcome.Income, "CAF", ExpenseIcon.Help, ExpenseFrequency.Monthly, true, 2, now.plusDaysCompat(2), now.minusMonthsCompat(1), listOf(getLabels()[2])),
            ExpenseUI(8, 45f, IncomeOrOutcome.Income, "TR", ExpenseIcon.Store, ExpenseFrequency.Monthly, false, 1, now.plusDaysCompat(7), now.minusMonthsCompat(1), listOf(getLabels()[0])),
            ExpenseUI(9, 100f, IncomeOrOutcome.Outcome, "TR", ExpenseIcon.Cloud, ExpenseFrequency.Annually, false, 1, now.plusDaysCompat(7), now.minusMonthsCompat(1), listOf(getLabels()[0]))
        )
    }

    private fun getLabels(): List<LabelUI> {
        return listOf(
            LabelUI(2, "Famille", Color.Red),
            LabelUI(1, "Perso", Color.Green),
            LabelUI(3, "Entreprise", Color.Yellow),
        )
    }

    // Extension functions to handle date operations
    private fun LocalDate.plusDaysCompat(days: Int): LocalDate {
        return this.plus(DatePeriod(days = days))
    }

    private fun LocalDate.minusMonthsCompat(months: Int): LocalDate {
        return this.minus(DatePeriod(months = months))
    }

}