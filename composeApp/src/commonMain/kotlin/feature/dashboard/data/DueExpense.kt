package feature.dashboard.data

import androidx.compose.ui.graphics.Color

data class DueExpense(
    val title: String,
    val dueIn: Int,
    val amount: Double,
    val labelColor: Color
)