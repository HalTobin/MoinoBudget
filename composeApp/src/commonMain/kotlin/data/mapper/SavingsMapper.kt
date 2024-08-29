package data.mapper

import data.db.table.Savings
import feature.savings.data.AddEditSavings
import kotlinx.datetime.Clock
import presentation.data.LabelUI
import presentation.data.SavingsUI
import util.toLocalDate

fun AddEditSavings.toSavingsEntity(): Savings {
    val currentDate = Clock.System.now().toEpochMilliseconds()
    val lastAutoIncrement = (this.lastMonthAutoIncrement?.toEpochDays()?.times(24L)?.times(3600L)?.times(1000L))
    return Savings(
        id = this.id ?: 0,
        title = this.title,
        subtitle = this.subtitle,
        amount = this.amount,
        goal = this.goal,
        autoIncrement = this.autoIncrement,
        lastMonthAutoIncrement = lastAutoIncrement ?: currentDate,
        labelId = this.labelId ?: 0
    )
}

fun Savings.toSavingsUI(labels: List<LabelUI>): SavingsUI {
    return SavingsUI(
        id = this.id,
        title = this.title,
        subtitle = this.subtitle,
        amount = this.amount,
        goal = this.goal,
        autoIncrement = this.autoIncrement,
        lastMonthAutoIncrement = this.lastMonthAutoIncrement.toLocalDate(),
        label = if (this.labelId != 0) labels.find { it.id == this.labelId } else null
    )
}