package feature.expenses.feature.add_edit_envelope.data

import androidx.compose.ui.graphics.Color

data class AddEditEnvelope(
    val id: Int?,
    val title: String,
    val max: Int?,
    val iconId: Int?,
    val frequency: Int,
    val color: Color?
)