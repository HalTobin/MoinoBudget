package presentation.data

import androidx.compose.ui.graphics.Color

data class LabelUI(
    val id: Int,
    val name: String,
    val color: Color,
) {
    companion object {
        val defaults = listOf(
            LabelUI(1, "Royal Blue", Color(0xFF4169E1)),
            LabelUI(2, "Emerald Green", Color(0xFF50C878)),
            LabelUI(3, "Golden Amber", Color(0xFFFFBF00)),
            LabelUI(4, "Crimson Red", Color(0xFFDC143C)),
            LabelUI(5, "Deep Purple", Color(0xFF800080)),
            LabelUI(6, "Midnight Black", Color(0xFF191970))
        )
    }
}
