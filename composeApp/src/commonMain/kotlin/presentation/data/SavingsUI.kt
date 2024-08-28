package presentation.data

import kotlinx.datetime.LocalDate

data class SavingsUI(
    val id: Int,
    val amount: Int,
    val goal: Int,
    val autoIncrement: Int,
    val lastMonthAutoIncrement: LocalDate
)