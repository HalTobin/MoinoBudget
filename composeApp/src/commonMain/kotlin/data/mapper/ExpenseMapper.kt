package data.mapper

import data.db.table.Expense
import feature.expenses.data.ExpenseUI
import feature.expenses.feature.add_edit_expense.data.AddEditExpense
import presentation.data.ExpenseIcon
import util.toLocalDate

fun Expense.toExpenseUI(): ExpenseUI =
    ExpenseUI(
        id = this.id,
        envelopeId = this.envelopeId,
        title = this.title,
        amount = this.amount,
        icon = this.iconId?.let { ExpenseIcon.findById(it) },
        date = this.timestamp.toLocalDate()
    )

fun AddEditExpense.toExpenseEntity(): Expense =
    Expense(
        id = this.id ?: 0,
        title = this.title,
        envelopeId = this.envelopeId,
        amount = this.amount,
        iconId = this.iconId,
        timestamp = this.timestamp
    )