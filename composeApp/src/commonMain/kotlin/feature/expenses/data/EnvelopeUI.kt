package feature.expenses.data

import kotlinx.datetime.LocalDate
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon

data class EnvelopeUI(
    val id: Int,
    val title: String,
    val current: Double,
    val max: Int?,
    val icon: ExpenseIcon?,
    val frequency: ExpenseFrequency,
    val startPeriod: LocalDate,
    val endPeriod: LocalDate
)
