package feature.expenses.feature.envelope_list.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.material.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import data.repository.AppPreferences
import feature.expenses.data.EnvelopeUI
import kotlinx.coroutines.delay
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.create_envelope
import moinobudget.composeapp.generated.resources.create_envelope_details
import moinobudget.composeapp.generated.resources.max_with_value
import org.jetbrains.compose.resources.stringResource
import presentation.component.AddCard
import presentation.data.IncomeOrOutcome
import presentation.formatCurrency

@Composable
fun EnvelopeScreen(
    state: EnvelopeState,
    preferences: AppPreferences
) = Column {

    Spacer(Modifier.height(48.dp))

    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(state.envelopes) {

        }
        item {
            AddCard(
                title = stringResource(Res.string.create_envelope),
                description = stringResource(Res.string.create_envelope_details),
                onClick = { TODO() }
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
    .clickable { onClick() }
    .padding(16.dp)) {

    val primaryColor = MaterialTheme.colorScheme.primary

    fun getColorByConsumption(current: Int, max: Int): Color {
        val percentage = current.toFloat() / max

        val red = IncomeOrOutcome.Outcome.color // Red
        val primary = primaryColor

        return lerp(primary, red, percentage)
    }

    Column(Modifier.padding(horizontal = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            envelope.icon?.let { icon ->
                Icon(icon.icon,
                    modifier = Modifier.padding(end = 16.dp).size(32.dp),
                    contentDescription = null)
            }
            Column {
                Row {
                    Text(envelope.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.SemiBold)
                    Text(
                        formatCurrency(envelope.current.toFloat(), preferences),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold)
                }
                Text(envelope.subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold)
            }
        }

        envelope.max?.let {
            val currentProgress = remember { Animatable(0f) }
            val progress = envelope.current.toFloat() / envelope.max

            LaunchedEffect(key1 = progress) {
                delay(300)
                currentProgress.animateTo(
                    targetValue = progress,
                    animationSpec = tween(1500)
                )
            }

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(6.dp),
                progress = { currentProgress.value },
                color = lerp(MaterialTheme.colorScheme.primary, IncomeOrOutcome.Outcome.color, progress),
                trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                drawStopIndicator = {}
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("${(progress * 100).toInt()}%",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = lerp(MaterialTheme.colorScheme.primary, IncomeOrOutcome.Outcome.color, progress),
                    style = MaterialTheme.typography.titleLarge)
                Text(
                    stringResource(Res.string.max_with_value, formatCurrency(envelope.max.toFloat(), preferences)),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light)
            }
        }
    }

}