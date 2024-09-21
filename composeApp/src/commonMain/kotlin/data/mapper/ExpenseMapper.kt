package data.mapper

import data.db.table.Expense
import feature.expenses.data.ExpenseUI
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