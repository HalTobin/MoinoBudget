package feature.expenses.feature.add_edit_expense

import kotlinx.datetime.LocalDate

sealed class AddEditExpenseEvent {
    data object UpsertExpense: AddEditExpenseEvent()
    data object DeleteExpense: AddEditExpenseEvent()

    data class UpdateTitle(val title: String): AddEditExpenseEvent()
    data class UpdateAmount(val amount: String): AddEditExpenseEvent()
    data class UpdateIconId(val iconId: Int?): AddEditExpenseEvent()
    data class UpdateDate(val date: LocalDate): AddEditExpenseEvent()
}