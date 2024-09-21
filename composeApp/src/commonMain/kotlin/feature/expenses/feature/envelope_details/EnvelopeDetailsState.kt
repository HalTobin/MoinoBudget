package feature.expenses.feature.envelope_details

import feature.expenses.data.EnvelopeUI
import feature.expenses.data.ExpenseUI

data class EnvelopeDetailsState(
    val envelope: EnvelopeUI? = null,
    val expenses: List<ExpenseUI> = emptyList()
)