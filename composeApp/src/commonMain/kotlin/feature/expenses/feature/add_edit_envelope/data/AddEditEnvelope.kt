package feature.expenses.feature.add_edit_envelope.data

data class AddEditEnvelope(
    val id: Int?,
    val title: String,
    val subtitle: String,
    val max: Int?,
    val iconId: Int?,
    val frequency: Int
)