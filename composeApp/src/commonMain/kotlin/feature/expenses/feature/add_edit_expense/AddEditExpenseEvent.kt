package feature.expenses.feature.add_edit_expense

sealed class AddEditExpenseEvent {
    data class UpdateTitle(val title: String): AddEditExpenseEvent()
    data class UpdateAmount(val amount: String): AddEditExpenseEvent()
    data class UpdateIconId(val iconId: Int?): AddEditExpenseEvent()
    data class UpdateDay(val day: Int): AddEditExpenseEvent()
    data class UpdateMonth(val month: Int): AddEditExpenseEvent()
}