package data.mapper

import data.db.table.Envelope
import feature.expenses.feature.add_edit_envelope.data.AddEditEnvelope

fun AddEditEnvelope.toEnvelopeEntity(): Envelope =
    Envelope(
        id = this.id ?: 0,
        title = this.title,
        max = this.max,
        iconId = this.iconId,
        frequency = this.frequency
    )