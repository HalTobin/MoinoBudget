package data.mapper

import data.db.table.Expense
import feature.dashboard.presentation.data.AddEditExpense

fun AddEditExpense.toExpenseEntity(): Expense =
    Expense(
        id = this.id ?: 0,
        title = this.title,
        amount = this.amount,
        iconId = this.icon.id,
        isIncome = this.incomeOrOutcome.dbId,
        frequency = this.frequency.id,
        monthOffset = this.monthOffset,
        day = this.day,
        lastPayment = this.lastPayment
    )