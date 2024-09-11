package data.mapper

import data.db.table.Budget
import feature.expenses.expenses_list.presentation.data.AddEditBudget

fun AddEditBudget.toBudgetEntity(nextOrder: Int): Budget =
    Budget(
        id = this.id ?: 0,
        orderIndex = this.orderIndex ?: nextOrder,
        title = this.title,
        bgId = style.id
    )