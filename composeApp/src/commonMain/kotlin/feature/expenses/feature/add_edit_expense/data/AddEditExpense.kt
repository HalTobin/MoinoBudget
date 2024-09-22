package feature.expenses.feature.add_edit_expense.data

data class AddEditExpense(
    val id: Int?,
    val title: String,
    val envelopeId: Int,
    val amount: Float,
    val iconId: Int?,
    val timestamp: Long
)