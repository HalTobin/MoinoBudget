package feature.budgets.feature.add_edit_budget_operation.presentation

import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.IncomeOrOutcome
import presentation.data.MonthOption

sealed class AddEditBudgetOperationEvent {
    data class UpdateTitle(val title: String): AddEditBudgetOperationEvent()
    data class UpdateAmount(val amount: String): AddEditBudgetOperationEvent()
    data class UpdateFrequency(val frequency: ExpenseFrequency): AddEditBudgetOperationEvent()
    data class UpdateMonthOffset(val month: MonthOption): AddEditBudgetOperationEvent()
    data class UpdateIncomeOrOutcome(val incomeOrOutcome: IncomeOrOutcome): AddEditBudgetOperationEvent()
    data class UpdateDay(val day: Int): AddEditBudgetOperationEvent()
    data class UpdateLabel(val labelId: Int): AddEditBudgetOperationEvent()
    data class UpdateIcon(val icon: ExpenseIcon): AddEditBudgetOperationEvent()
    data object UpsertBudgetOperation: AddEditBudgetOperationEvent()
    data object DeleteBudgetOperation: AddEditBudgetOperationEvent()
}