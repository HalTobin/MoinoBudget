package presentation.data

import kotlinx.datetime.LocalDate

data class ExpenseUI(
    val id: Int,
    val amount: Float,
    val type: IncomeOrOutcome,
    val title: String,
    val icon: ExpenseIcon,
    val frequency: ExpenseFrequency,
    val payed: Boolean,
    val dueIn: Int,
    val nextPayment: LocalDate,
    val labels: List<LabelUI>
)