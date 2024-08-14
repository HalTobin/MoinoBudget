package feature.dashboard.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import data.repository.BudgetRepository
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
    private val labelRepository: LabelRepository,
    private val budgetRepository: BudgetRepository
): ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    private var budgetJob: Job? = null
    private var labelJob: Job? = null

    init {
        // Load expenses and payments...
        setUpLabelJob()
        setUpBudgetJob()

        viewModelScope.launch(Dispatchers.IO) {
            val labels = labelRepository.getLabels()

            /*val expenses = getExpenses(labels)
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
                title = "My budget",
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
                title = "My secondary budget",
                labels = emptyList(),//labels.subList(1, labels.size),
                expenses = expenses,
                style = BudgetStyle.Winter,
                rawIncomes = MonthYearPair(annual = yearIncomes / 2),
                toPutAside = MonthYearPair(annual = toPutAside / 2),
                monthPayments = MonthYearPair(annual = monthPayments / 2),
                disposableIncomes = MonthYearPair(annual = (yearIncomes - yearOutcomes) / 2),
                upcomingPayments = MonthYearPair(annual = yearOutcomes / 2),
            )
            )*/

            _state.update { it.copy(labels = labels) }
        }
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.UpsertLabel -> viewModelScope.launch(Dispatchers.IO) {
                labelRepository.upsertLabel(event.label)
            }
            is DashboardEvent.UpsertBudget -> viewModelScope.launch(Dispatchers.IO) {
                budgetRepository.upsertBudget(event.budget)
            }
        }
    }

    private fun setUpBudgetJob() {
        budgetJob?.cancel()
        budgetJob = viewModelScope.launch(Dispatchers.IO) {
            budgetRepository.getBudgetsFlow().collect { budgets ->
                _state.update { it.copy(budgets = budgets) }
            }
        }
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