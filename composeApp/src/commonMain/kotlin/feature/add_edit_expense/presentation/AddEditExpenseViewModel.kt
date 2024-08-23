package feature.add_edit_expense.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.ExpenseRepository
import data.repository.LabelRepository
import feature.dashboard.presentation.DashboardEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import util.getMaxDay
import kotlin.math.min

class AddEditExpenseViewModel(
    private val labelRepository: LabelRepository,
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    private var _initiated = false

    private val _state = MutableStateFlow(AddEditExpenseState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val labels = labelRepository.getLabels()
            _state.update { it.copy(labels = labels) }
        }
    }

    fun onEvent(event: AddEditExpenseEvent) = viewModelScope.launch(Dispatchers.IO) {
        when (event) {
            is AddEditExpenseEvent.Init -> {
                if (!_initiated) {
                    event.expenseId?.let {
                        _state.update { AddEditExpenseState.generateStateFromExpenseUI(
                            labels = labelRepository.getLabels(),
                            expense = expenseRepository.getExpense(event.expenseId),
                        ) }
                    } ?: _state.update { it.copy(expenseLabels = event.labels) }
                    _initiated = true
                }
            }
            is AddEditExpenseEvent.UpdateTitle -> _state.update { it.copy(expenseTitle = event.title) }
            is AddEditExpenseEvent.UpdateAmount -> _state.update { it.copy(expenseAmount = event.amount) }
            is AddEditExpenseEvent.UpdateDay -> _state.update { it.copy(expenseDay = event.day) }
            is AddEditExpenseEvent.UpdateFrequency -> {
                _state.update { it.copy(expenseFrequency = event.frequency, expenseMonth = event.frequency.options.firstOrNull()) }
            }
            is AddEditExpenseEvent.UpdateMonthOffset -> {
                val maxMonthDay = event.month.offset.plus(1).getMaxDay()
                val day = min(_state.value.expenseDay, maxMonthDay)
                _state.update { it.copy(expenseMonth = event.month, expenseDay = day) }
            }
            is AddEditExpenseEvent.UpdateIncomeOrOutcome -> _state.update { it.copy(expenseIncomeOrOutcome = event.incomeOrOutcome) }
            is AddEditExpenseEvent.UpdateLabel -> {
                val currentLabels = _state.value.expenseLabels.toMutableList()
                if (currentLabels.contains(event.labelId)) currentLabels.remove(event.labelId)
                else currentLabels.add(event.labelId)
                _state.update { it.copy(expenseLabels = currentLabels.toList()) }
            }
            is AddEditExpenseEvent.UpdateIcon -> _state.update { it.copy(expenseIcon = event.icon) }
            is AddEditExpenseEvent.UpsertExpense -> {

            }
            is AddEditExpenseEvent.DeleteExpense -> {
                _state.value.expenseId?.let { expenseRepository.deleteExpense(it) }
            }
        }
    }

    sealed class UiEvent {

    }

}