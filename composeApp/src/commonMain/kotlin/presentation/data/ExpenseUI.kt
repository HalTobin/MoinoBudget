package presentation.data

import kotlinx.datetime.LocalDate

data class ExpenseUI(
    val id: Int,
    val title: String,
    val icon: ExpenseIcon,
    val frequency: ExpenseFrequency,
    val payed: Boolean,
    val nextPayment: LocalDate,
    val lastPayment: LocalDate
)