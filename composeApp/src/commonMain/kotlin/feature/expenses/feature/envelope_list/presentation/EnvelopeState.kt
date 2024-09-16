package feature.expenses.feature.envelope_list.presentation

import feature.expenses.data.EnvelopeUI

data class EnvelopeState(
    val envelopes: List<EnvelopeUI> = emptyList()
)