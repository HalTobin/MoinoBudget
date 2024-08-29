package feature.savings.data

import kotlinx.datetime.LocalDate

data class AddEditSavings(
    val id: Int?,
    val title: String,
    val subtitle: String,
    val amount: Int,
    val goal: Int,
    val autoIncrement: Int,
    val lastMonthAutoIncrement: LocalDate?,
    val labelId: Int?
)