package feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.BudgetRepository
import data.repository.ExpenseRepository
import data.repository.LabelRepository
import data.repository.NeedOneBudget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class DashboardViewModel(
    private val labelRepository: LabelRepository,
    private val budgetRepository: BudgetRepository,
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    private var budgetJob: Job? = null
    private var labelJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        // Load expenses and payments...
        setUpLabelJob()
        setUpBudgetJob()

        viewModelScope.launch(Dispatchers.IO) {
            val labels = labelRepository.getLabels()
            _state.update { it.copy(labels = labels) }
        }
    }

    fun onEvent(event: DashboardEvent) = viewModelScope.launch(Dispatchers.IO) {
        when (event) {
            is DashboardEvent.UpsertLabel -> labelRepository.upsertLabel(event.label)
            is DashboardEvent.UpsertBudget -> budgetRepository.upsertBudget(event.budget)
            is DashboardEvent.DeleteBudget -> {
                try {
                    budgetRepository.deleteBudget(event.budgetId)
                } catch (e: NeedOneBudget) {
                    _eventFlow.emit(UiEvent.OneBudgetIsNeeded)
                }
            }
            is DashboardEvent.DeleteExpense -> TODO()
            is DashboardEvent.UpsertExpense -> TODO()
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

    sealed class UiEvent {
        data object OneBudgetIsNeeded: UiEvent()
    }

}