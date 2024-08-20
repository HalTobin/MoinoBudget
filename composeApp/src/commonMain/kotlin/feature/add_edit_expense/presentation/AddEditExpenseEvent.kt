package feature.add_edit_expense.presentation

import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.IncomeOrOutcome

sealed class AddEditExpenseEvent {
    data class UpdateTitle(val title: String): AddEditExpenseEvent()
    data class UpdateAmount(val amount: String): AddEditExpenseEvent()
    data class UpdateFrequency(val frequency: ExpenseFrequency): AddEditExpenseEvent()
    data class UpdateIncomeOrOutcome(val incomeOrOutcome: IncomeOrOutcome): AddEditExpenseEvent()
    data class UpdateDay(val day: Int): AddEditExpenseEvent()
    data class UpdateLabel(val labelId: Int): AddEditExpenseEvent()
    data class UpdateIcon(val icon: ExpenseIcon): AddEditExpenseEvent()
}