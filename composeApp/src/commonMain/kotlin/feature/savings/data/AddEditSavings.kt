package feature.savings.data

import kotlinx.datetime.LocalDateTime

data class AddEditSavings(
    val id: Int,
    val amount: Int,
    val goal: Int,
    val autoIncrement: Int,
    val lastMonthAutoIncrement: LocalDateTime?
)