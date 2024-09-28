package feature.expenses.feature.envelope_details.presentation

import feature.expenses.data.EnvelopeUI
import feature.expenses.data.ExpenseUI
import feature.expenses.feature.envelope_details.data.ExpensePeriod

data class EnvelopeDetailsState(
    val envelope: EnvelopeUI? = null,
    val expenses: List<ExpenseUI> = emptyList(),
    val periods: List<ExpensePeriod> = emptyList()
)