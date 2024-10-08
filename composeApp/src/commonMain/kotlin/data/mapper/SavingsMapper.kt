package data.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import data.db.table.Savings
import feature.savings.feature.add_edit_savings.data.AddEditSavings
import kotlinx.datetime.Clock
import presentation.data.ExpenseIcon
import feature.savings.data.SavingsType
import feature.savings.data.SavingsUI
import util.toLocalDate

fun AddEditSavings.toSavingsEntity(): Savings {
    val currentDate = Clock.System.now().toEpochMilliseconds()
    val lastAutoIncrement = (this.lastMonthAutoIncrement?.toEpochDays()?.times(24L)?.times(3600L)?.times(1000L))
    return Savings(
        id = this.id ?: 0,
        title = this.title,
        type = this.type.id,
        subtitle = this.subtitle,
        iconId = this.iconId,
        amount = this.amount,
        goal = this.goal,
        autoIncrement = this.autoIncrement,
        lastMonthAutoIncrement = lastAutoIncrement ?: currentDate,
        color = this.color?.toArgb()
    )
}

fun Savings.toSavingsUI(): SavingsUI {
    return SavingsUI(
        id = this.id,
        title = this.title,
        type = SavingsType.findById(this.type),
        subtitle = this.subtitle,
        icon = this.iconId?.let { ExpenseIcon.findById(it) },
        amount = this.amount,
        goal = this.goal,
        autoIncrement = this.autoIncrement,
        lastMonthAutoIncrement = this.lastMonthAutoIncrement.toLocalDate(),
        color = this.color?.let { Color(it) }
    )
}