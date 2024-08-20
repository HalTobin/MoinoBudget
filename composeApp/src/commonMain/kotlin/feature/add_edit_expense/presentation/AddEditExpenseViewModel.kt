package feature.add_edit_expense.presentation

import androidx.lifecycle.SavedStateHandle
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

class AddEditExpenseViewModel(
    //savedStateHandle: SavedStateHandle,
    private val labelRepository: LabelRepository,
    private val expenseRepository: ExpenseRepository
): ViewModel() {

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
            is AddEditExpenseEvent.UpdateTitle -> _state.update { it.copy(expenseTitle = event.title) }
            is AddEditExpenseEvent.UpdateAmount -> _state.update { it.copy(expenseAmount = event.amount) }
            is AddEditExpenseEvent.UpdateDay -> _state.update { it.copy(expenseDay = event.day) }
            is AddEditExpenseEvent.UpdateFrequency -> _state.update { it.copy(expenseFrequency = event.frequency) }
            is AddEditExpenseEvent.UpdateIncomeOrOutcome -> _state.update { it.copy(expenseIncomeOrOutcome = event.incomeOrOutcome) }
            is AddEditExpenseEvent.UpdateLabel -> {
                val currentLabels = _state.value.expenseLabels.toMutableList()
                if (currentLabels.contains(event.labelId)) currentLabels.remove(event.labelId)
                else currentLabels.add(event.labelId)
                _state.update { it.copy(expenseLabels = currentLabels.toList()) }
            }
            is AddEditExpenseEvent.UpdateIcon -> _state.update { it.copy(expenseIcon = event.icon) }
        }
    }

    sealed class UiEvent {

    }

}