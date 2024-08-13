package feature.dashboard.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import data.repository.LabelRepository
import feature.dashboard.data.MonthYearPair
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

class DashboardViewModel(
    private val labelRepository: LabelRepository
): ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    private var labelJob: Job? = null

    init {
        // Load expenses and payments...
        setUpLabelJob()

        viewModelScope.launch(Dispatchers.IO) {
            val labels = labelRepository.getLabels()
            val expenses = getExpenses(labels)

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

            val dummyBudget = listOf(BudgetUI(
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
                labels = emptyList(),//labels.subList(1, labels.size),
                expenses = expenses,
                style = BudgetStyle.Winter,
                rawIncomes = MonthYearPair(annual = yearIncomes / 2),
                toPutAside = MonthYearPair(annual = toPutAside / 2),
                monthPayments = MonthYearPair(annual = monthPayments / 2),
                disposableIncomes = MonthYearPair(annual = (yearIncomes - yearOutcomes) / 2),
                upcomingPayments = MonthYearPair(annual = yearOutcomes / 2),
            )
            )

            _state.update { it.copy(budgets = dummyBudget, labels = labels) }
        }
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.UpsertLabel -> viewModelScope.launch(Dispatchers.IO) {
                labelRepository.upsertLabel(event.label)
            }
            is DashboardEvent.UpsertBudget -> viewModelScope.launch(Dispatchers.IO) {

            }
        }
    }

    private fun getExpenses(labels: List<LabelUI>): List<ExpenseUI> {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        /*return listOf(
            ExpenseUI(1, 15f, IncomeOrOutcome.Outcome,"Netflix", ExpenseIcon.Film, ExpenseFrequency.Monthly, false, 6, now, now.minusMonthsCompat(1), labels.subList(0, 2)),
            ExpenseUI(2, 235f, IncomeOrOutcome.Outcome,"Electricity", ExpenseIcon.Electricity, ExpenseFrequency.Monthly, true, 2, now.plusDaysCompat(5), now.minusMonthsCompat(1), listOf(labels[1])),
            ExpenseUI(3, 700f, IncomeOrOutcome.Outcome, "Rent", ExpenseIcon.Housing, ExpenseFrequency.Monthly, false, 17, now.plusDaysCompat(10), now.minusMonthsCompat(1), labels.subList(0, 2)),
            ExpenseUI(4, 20f, IncomeOrOutcome.Outcome, "Internet", ExpenseIcon.Internet, ExpenseFrequency.Monthly, true, 2, now.plusDaysCompat(2), now.minusMonthsCompat(1), listOf(labels[0], labels[2])),
            ExpenseUI(5, 150f, IncomeOrOutcome.Outcome, "Water", ExpenseIcon.Water, ExpenseFrequency.Monthly, false, 1, now.plusDaysCompat(7), now.minusMonthsCompat(1), labels.subList(0, 2)),
            ExpenseUI(6, 1656f, IncomeOrOutcome.Income, "Salary", ExpenseIcon.Incomes, ExpenseFrequency.Monthly, false, 17, now.plusDaysCompat(10), now.minusMonthsCompat(1), labels.subList(3, 5)),
            ExpenseUI(7, 185f, IncomeOrOutcome.Income, "CAF", ExpenseIcon.Help, ExpenseFrequency.Monthly, true, 2, now.plusDaysCompat(2), now.minusMonthsCompat(1), listOf(labels[2])),
            ExpenseUI(8, 45f, IncomeOrOutcome.Income, "TR", ExpenseIcon.Store, ExpenseFrequency.Monthly, false, 1, now.plusDaysCompat(7), now.minusMonthsCompat(1), listOf(labels[0])),
            ExpenseUI(9, 100f, IncomeOrOutcome.Outcome, "TR", ExpenseIcon.Cloud, ExpenseFrequency.Annually, false, 1, now.plusDaysCompat(7), now.minusMonthsCompat(1), listOf(labels[0]))
        )*/
        return emptyList()
    }

    private fun setUpLabelJob() {
        labelJob?.cancel()
        labelJob = viewModelScope.launch(Dispatchers.IO) {
            labelRepository.getLabelsFlow().collect { labels ->
                _state.update { it.copy(labels = labels) }
            }
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