package presentation.data

import androidx.compose.ui.graphics.Color

data class LabelUI(
    val id: Int,
    val name: String,
    val color: Color,
) {
    companion object {
        val defaults = listOf(
            LabelUI(1, "", Color(0xFF50C878)),
            LabelUI(2, "", Color(0xFFFFBF00)),
            LabelUI(3, "", Color(0xFFEE6A50)),
            LabelUI(4, "", Color(0xFFDC143C)),

            LabelUI(5, "", Color(0xFF800080)),
            LabelUI(6, "", Color(0xFF191970)),
            LabelUI(7, "", Color(0xFF0F52BA)),
            LabelUI(8, "", Color(0xFF4169E1)),
        )
    }
}
