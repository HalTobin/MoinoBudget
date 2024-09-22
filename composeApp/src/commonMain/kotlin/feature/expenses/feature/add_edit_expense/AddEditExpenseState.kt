package feature.expenses.feature.add_edit_expense

import feature.expenses.data.ExpenseUI

data class AddEditExpenseState(
    val id: Int? = null,
    val title: String = "",
    val envelopeId: Int,
    val amount: String = "",
    val iconId: Int? = null,
    val day: Int = 1,
    val month: Int = 1
) {

    fun generateAddEditExpense() {
        TODO()
    }

    companion object {
        fun generateFromExpenseUI(expense: ExpenseUI): AddEditExpenseState =
            AddEditExpenseState(
                id = expense.id,
                title = expense.title,
                envelopeId = expense.envelopeId,
                amount = expense.amount.toString(),
                iconId = expense.icon?.id,
                day = expense.date.dayOfMonth,
                month = expense.date.monthNumber
            )
    }
}