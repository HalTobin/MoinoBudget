package data.mapper

import data.db.table.Budget
import feature.dashboard.presentation.data.AddEditBudget

fun AddEditBudget.toBudgetEntity(): Budget =
    Budget(
        id = this.id ?: 0,
        title = this.title,
        bgId = style.id
    )