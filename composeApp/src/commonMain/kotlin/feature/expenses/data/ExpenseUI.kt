package feature.expenses.data

import kotlinx.datetime.LocalDate
import presentation.data.ExpenseIcon

data class ExpenseUI(
    val id: Int,
    val title: String,
    val envelopeId: Int,
    val amount: Float,
    val icon: ExpenseIcon?,
    val date: LocalDate
)