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
            is AddEditExpenseEvent.UpdateIconId -> _state.update {
                it.copy(iconId = if (_state.value.iconId == event.iconId) null else event.iconId)
            }
            is AddEditExpenseEvent.UpdateDate -> _state.update { it.copy(date = event.date) }
            is AddEditExpenseEvent.UpsertExpense -> viewModelScope.launch(Dispatchers.IO) {
                _state.value.generateAddEditExpense()?.let {
                    expenseRepository.upsertExpense(it) }
            }
            is AddEditExpenseEvent.DeleteExpense -> viewModelScope.launch(Dispatchers.IO) {
                _state.value.id?.let { expenseRepository.deleteExpense(it) }
            }
        }
    }

}