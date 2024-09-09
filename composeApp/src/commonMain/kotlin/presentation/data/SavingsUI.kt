package presentation.data

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalDate

data class SavingsUI(
    val id: Int,
    val title: String,
    val type: SavingsType,
    val subtitle: String,
    val amount: Int,
    val goal: Int?,
    val autoIncrement: Int,
    val lastMonthAutoIncrement: LocalDate,
    val color: Color?
)