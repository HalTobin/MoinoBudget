package presentation.data

import androidx.compose.ui.graphics.Color

data class LabelUI(
    val id: Int,
    val name: String,
    val color: Color,
    val style: LabelStyle
)
