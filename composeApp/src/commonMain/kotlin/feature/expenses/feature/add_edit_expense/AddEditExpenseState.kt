package feature.expenses.feature.add_edit_expense

import feature.expenses.data.ExpenseUI
import feature.expenses.feature.add_edit_expense.data.AddEditExpense
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import util.toEpochMillisecond
import util.toLocalDate

data class AddEditExpenseState(
    val id: Int? = null,
    val title: String = "",
    val envelopeId: Int,
    val amount: String = "",
    val iconId: Int? = null,
    val date: LocalDate = Clock.System.now().toEpochMilliseconds().toLocalDate()
) {

    fun generateAddEditExpense(): AddEditExpense? =
        this.amount.toFloatOrNull()?.let { amount ->
            AddEditExpense(
                id = this.id,
                title = this.title,
                envelopeId = this.envelopeId,
                amount = amount,
                iconId = this.iconId,
                timestamp = this.date.toEpochMillisecond()
            )
        }

    companion object {
        fun generateFromExpenseUI(expense: ExpenseUI): AddEditExpenseState =
            AddEditExpenseState(
                id = expense.id,
                title = expense.title,
                envelopeId = expense.envelopeId,
                amount = expense.amount.toString(),
                iconId = expense.icon?.id,
                date = expense.date
            )
    }
}