package feature.budgets.feature.add_edit_budgets.presentation

import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.IncomeOrOutcome
import presentation.data.MonthOption

sealed class AddEditExpenseEvent {
    data class Init(val expenseId: Int?, val labels: List<Int>): AddEditExpenseEvent()
    data class UpdateTitle(val title: String): AddEditExpenseEvent()
    data class UpdateAmount(val amount: String): AddEditExpenseEvent()
    data class UpdateFrequency(val frequency: ExpenseFrequency): AddEditExpenseEvent()
    data class UpdateMonthOffset(val month: MonthOption): AddEditExpenseEvent()
    data class UpdateIncomeOrOutcome(val incomeOrOutcome: IncomeOrOutcome): AddEditExpenseEvent()
    data class UpdateDay(val day: Int): AddEditExpenseEvent()
    data class UpdateLabel(val labelId: Int): AddEditExpenseEvent()
    data class UpdateIcon(val icon: ExpenseIcon): AddEditExpenseEvent()
    data object UpsertExpense: AddEditExpenseEvent()
    data object DeleteExpense: AddEditExpenseEvent()
}