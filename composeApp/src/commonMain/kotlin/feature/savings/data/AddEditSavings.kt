package feature.savings.data

import kotlinx.datetime.LocalDate
import presentation.data.SavingsType

data class AddEditSavings(
    val id: Int?,
    val title: String,
    val type: SavingsType,
    val subtitle: String,
    val amount: Int,
    val goal: Int?,
    val autoIncrement: Int,
    val lastMonthAutoIncrement: LocalDate?,
    val labelId: Int?
)