package feature.expenses.feature.add_edit_envelope.presentation

import androidx.compose.ui.graphics.Color
import feature.expenses.data.EnvelopeUI
import feature.expenses.feature.add_edit_envelope.data.AddEditEnvelope
import presentation.data.ExpenseFrequency

data class AddEditEnvelopeState(
    val envelopeId: Int? = null,
    val title: String = "",
    val max: String = "",
    val iconId: Int? = null,
    val frequency: Int = ExpenseFrequency.Monthly.id,
    val color: Color? = null
) {

    fun getAddEditEnvelope(): AddEditEnvelope? {
        return if (max.isNotEmpty() && max.toIntOrNull() == null) null
        else AddEditEnvelope(
            id = envelopeId,
            title = title,
            max = max.toIntOrNull(),
            iconId = iconId,
            frequency = frequency,
            color = color
        )
    }

    companion object {
        fun generateFromEnvelopeUI(envelope: EnvelopeUI) = AddEditEnvelopeState(
            envelopeId = envelope.id,
            title = envelope.title,
            max = envelope.max?.toString() ?: "",
            iconId = envelope.icon?.id,
            frequency = envelope.frequency.id,
            color = envelope.color
        )
    }

}