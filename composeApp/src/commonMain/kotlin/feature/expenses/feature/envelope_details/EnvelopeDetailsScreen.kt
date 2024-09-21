package feature.expenses.feature.envelope_details

import androidx.compose.runtime.Composable
import data.repository.AppPreferences

@Composable
fun EnvelopeDetailsScreen(
    state: EnvelopeDetailsState,
    preferences: AppPreferences,
    addEditExpense: (Int?) -> Unit,
    goBack: () -> Unit
) {
}