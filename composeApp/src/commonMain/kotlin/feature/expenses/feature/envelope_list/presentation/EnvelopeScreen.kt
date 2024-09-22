package feature.expenses.feature.envelope_list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import data.repository.AppPreferences
import feature.expenses.components.CustomProgressBar
import feature.expenses.components.RemainingMoney
import feature.expenses.data.EnvelopeUI
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.create_envelope
import moinobudget.composeapp.generated.resources.create_envelope_details
import moinobudget.composeapp.generated.resources.remaining_is
import org.jetbrains.compose.resources.stringResource
import presentation.component.AddCard
import presentation.formatCurrency
import util.simpleFormatDate
import util.toHue

@Composable
fun EnvelopeScreen(
    state: EnvelopeState,
    preferences: AppPreferences,
    addEditEnvelope:() -> Unit,
    goToDetails:(Int) -> Unit
) = Column {

    Spacer(Modifier.height(48.dp))

    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(state.envelopes) { envelope ->
            EnvelopeItem(
                modifier = Modifier,
                preferences = preferences,
                envelope = envelope,
                onClick = { goToDetails(envelope.id) }
            )
        }
        item {
            AddCard(
                title = stringResource(Res.string.create_envelope),
                description = stringResource(Res.string.create_envelope_details),
                onClick = { addEditEnvelope() }
            )
        }
    }

}

@Composable
fun EnvelopeItem(
    modifier: Modifier,
    preferences: AppPreferences,
    envelope: EnvelopeUI,
    onClick: () -> Unit
) = Box(modifier
    .fillMaxWidth()
    .padding(vertical = 8.dp)
    .clip(RoundedCornerShape(16.dp))
    .background(MaterialTheme.colorScheme.surface)
    .clickable { onClick() }) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        val envelopePrimary = envelope.color ?: MaterialTheme.colorScheme.primary
        val envelopeOnPrimary = Color.hsl(hue = envelopePrimary.toHue(), lightness = 0.12f, saturation = 1f)

        Surface(
            contentColor = envelopeOnPrimary,
            color = envelopePrimary
        ) {
            Column(Modifier.padding(horizontal = 24.dp)) {
                Row(Modifier.padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    envelope.icon?.let { icon ->
                        Icon(icon.icon,
                            modifier = Modifier.padding(end = 16.dp).size(32.dp),
                            contentDescription = null)
                    }
                    Text(envelope.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.SemiBold)

                    envelope.remainingMoney?.let { remainingMoney ->
                        Text(stringResource(Res.string.remaining_is))
                        Text(formatCurrency(remainingMoney.toFloat(), preferences),
                            modifier = Modifier.padding(start = 4.dp),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Row(Modifier.padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(formatCurrency(envelope.current.toFloat(), preferences),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold)
                    Text("${simpleFormatDate(envelope.startPeriod, preferences)} - ${simpleFormatDate(envelope.endPeriod, preferences)}",
                        style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        envelope.max?.let {
            Row(Modifier.padding(horizontal = 24.dp).padding(top = 12.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                CustomProgressBar(
                    modifier = Modifier.weight(1f),
                    current = envelope.current,
                    max = envelope.max,
                    primaryColor = envelopePrimary,
                    onPrimaryColor = envelopeOnPrimary
                )
                Text(formatCurrency(envelope.max.toFloat(), preferences),
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.titleMedium)
            }

            envelope.remainingMoney?.let {
                RemainingMoney(
                    remainingMoney = it,
                    remainingDays = envelope.remainingDays,
                    preferences = preferences
                )
            }
        }
    }

}