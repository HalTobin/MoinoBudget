package feature.expenses.feature.envelope_history

import feature.expenses.data.EnvelopeUI

data class EnvelopeHistoryState(
    val envelopes: List<EnvelopeUI> = emptyList()
)