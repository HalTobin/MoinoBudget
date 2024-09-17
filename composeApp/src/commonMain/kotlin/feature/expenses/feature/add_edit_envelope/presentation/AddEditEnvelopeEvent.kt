package feature.expenses.feature.add_edit_envelope.presentation

sealed class AddEditEnvelopeEvent {
    data class Init(val envelopeId: Int): AddEditEnvelopeEvent()
    data object UpsertEnvelope: AddEditEnvelopeEvent()
    data object DeleteEnvelope: AddEditEnvelopeEvent()

    data class UpdateTitle(val title: String): AddEditEnvelopeEvent()
    data class UpdateSubtitle(val subtitle: String): AddEditEnvelopeEvent()
    data class UpdateMax(val max: String): AddEditEnvelopeEvent()
    data class UpdateIcon(val iconId: Int?): AddEditEnvelopeEvent()
    data class UpdateFrequency(val frequencyId: Int): AddEditEnvelopeEvent()
}