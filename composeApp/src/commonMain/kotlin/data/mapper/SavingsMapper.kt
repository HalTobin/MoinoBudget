package data.mapper

import data.db.table.Savings
import feature.savings.data.AddEditSavings

fun AddEditSavings.toSavingsEntity(): Savings {
    val lastAutoIncrement = this.lastMonthAutoIncrement?.
    return Savings(
        id = this.id ?: 0,

    )
}