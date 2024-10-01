package feature.expenses.feature.envelope_history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.repository.AppPreferences
import feature.expenses.feature.envelope_list.presentation.EnvelopeItem

@Composable
fun EnvelopeHistoryScreen(
    state: EnvelopeHistoryState,
    preferences: AppPreferences,
    goBack: () -> Unit,
    openDetails: (Int) -> Unit
) = Surface(
    color = MaterialTheme.colorScheme.background,
    contentColor = MaterialTheme.colorScheme.onBackground
) {
    Scaffold {
        Column(Modifier.windowInsetsPadding(WindowInsets.systemBars)) {
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(state.envelopes) { envelope ->
                    EnvelopeItem(Modifier,
                        preferences = preferences,
                        envelope = envelope,
                        onClick = { openDetails(envelope.id) })
                }
            }
        }
    }
}