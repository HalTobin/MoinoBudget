package feature.expenses.feature.add_edit_expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import util.nullIfMinus

class AddEditExpenseViewModel(
    expenseId: Int,
    envelopeId: Int,
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    private val _state = MutableStateFlow(AddEditExpenseState(envelopeId = envelopeId))
    val state = _state.asStateFlow()

    init {
        expenseId.nullIfMinus()?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                val expense = expenseRepository.getExpenseById(id)
                _state.update { AddEditExpenseState.generateFromExpenseUI(expense) }
            }
        }
    }

    fun onEvent(event: AddEditExpenseEvent) {
        when (event) {
            is AddEditExpenseEvent.UpdateTitle -> _state.update { it.copy(title = event.title) }
            is AddEditExpenseEvent.UpdateAmount -> _state.update { it.copy(amount = event.amount) }
            is AddEditExpenseEvent.UpdateIconId -> _state.update { it.copy(iconId = event.iconId) }
            is AddEditExpenseEvent.UpdateDay -> _state.update { it.copy(day = event.day) }
            is AddEditExpenseEvent.UpdateMonth -> _state.update { it.copy(month = event.month) }
        }
    }

}