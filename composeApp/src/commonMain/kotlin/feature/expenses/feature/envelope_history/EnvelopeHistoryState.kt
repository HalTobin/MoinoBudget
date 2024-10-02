package feature.expenses.feature.envelope_history

import androidx.compose.ui.graphics.Color
import feature.expenses.data.EnvelopeUI

data class EnvelopeHistoryState(
    val color: Color? = null,
    val envelopes: List<EnvelopeUI> = emptyList()
)