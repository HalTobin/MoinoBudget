package feature.budgets.feature.add_edit_budget_operation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.BudgetOperationRepository
import data.repository.LabelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import util.getMaxDay
import util.nullIfMinus
import kotlin.math.min

class AddEditBudgetOperationViewModel(
    budgetOperationId: Int,
    labelsInt: List<Int>,
    private val labelRepository: LabelRepository,
    private val budgetOperationRepository: BudgetOperationRepository
): ViewModel() {

    private val _state = MutableStateFlow(AddEditBudgetOperationState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val labels = labelRepository.getLabels()
            _state.update { it.copy(availableLabels = labels) }
        }
        initState(budgetOperationId.nullIfMinus(), labelsInt)
    }

    fun onEvent(event: AddEditBudgetOperationEvent) = viewModelScope.launch(Dispatchers.IO) {
        when (event) {
            is AddEditBudgetOperationEvent.UpdateTitle -> _state.update { it.copy(title = event.title) }
            is AddEditBudgetOperationEvent.UpdateAmount -> _state.update { it.copy(amount = event.amount) }
            is AddEditBudgetOperationEvent.UpdateDay -> _state.update { it.copy(day = event.day) }
            is AddEditBudgetOperationEvent.UpdateFrequency -> {
                _state.update { it.copy(frequency = event.frequency, month = event.frequency.options.firstOrNull()) }
            }
            is AddEditBudgetOperationEvent.UpdateMonthOffset -> {
                val maxMonthDay = event.month.offset.plus(1).getMaxDay()
                val day = min(_state.value.day, maxMonthDay)
                _state.update { it.copy(month = event.month, day = day) }
            }
            is AddEditBudgetOperationEvent.UpdateIncomeOrOutcome -> _state.update { it.copy(incomeOrOutcome = event.incomeOrOutcome) }
            is AddEditBudgetOperationEvent.UpdateLabel -> {
                val currentLabels = _state.value.selectedLabels.toMutableList()
                if (currentLabels.contains(event.labelId)) currentLabels.remove(event.labelId)
                else currentLabels.add(event.labelId)
                _state.update { it.copy(selectedLabels = currentLabels.toList()) }
            }
            is AddEditBudgetOperationEvent.UpdateIcon -> _state.update { it.copy(icon = event.icon) }
            is AddEditBudgetOperationEvent.UpsertBudgetOperation -> {
                _state.value.generateAddEditExpense()?.let { expense ->
                    budgetOperationRepository.upsertBudgetOperation(expense)
                }
            }
            is AddEditBudgetOperationEvent.DeleteBudgetOperation -> {
                _state.value.budgetOperationId?.let { budgetOperationRepository.deleteBudgetOperation(it) }
            }
        }
    }

    private fun initState(budgetOperationId: Int?, labels: List<Int>) = viewModelScope.launch(Dispatchers.IO) {
        budgetOperationId?.let {
            _state.update {
                AddEditBudgetOperationState.generateStateFromExpenseUI(
                    labels = labelRepository.getLabels(),
                    expense = budgetOperationRepository.getBudgetOperation(budgetOperationId),
                )
            }
        } ?: _state.update { it.copy(selectedLabels = labels) }
    }

}