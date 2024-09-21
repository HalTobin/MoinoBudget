package feature.expenses.feature.add_edit_envelope.presentation

import androidx.compose.ui.graphics.Color

sealed class AddEditEnvelopeEvent {
    data object UpsertEnvelope: AddEditEnvelopeEvent()
    data object DeleteEnvelope: AddEditEnvelopeEvent()

    data class UpdateTitle(val title: String): AddEditEnvelopeEvent()
    data class UpdateMax(val max: String): AddEditEnvelopeEvent()
    data class UpdateIcon(val iconId: Int?): AddEditEnvelopeEvent()
    data class UpdateFrequency(val frequencyId: Int): AddEditEnvelopeEvent()
    data class UpdateColor(val color: Color?): AddEditEnvelopeEvent()
}