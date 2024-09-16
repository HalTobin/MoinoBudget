package feature.savings.feature.add_edit_savings.data

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalDate
import feature.savings.data.SavingsType

data class AddEditSavings(
    val id: Int?,
    val title: String,
    val iconId: Int?,
    val type: SavingsType,
    val subtitle: String,
    val amount: Int,
    val goal: Int?,
    val autoIncrement: Int,
    val lastMonthAutoIncrement: LocalDate?,
    val color: Color?
)