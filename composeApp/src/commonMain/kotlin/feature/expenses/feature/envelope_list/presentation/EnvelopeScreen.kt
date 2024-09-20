package feature.expenses.feature.envelope_list.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.repository.AppPreferences
import feature.expenses.data.EnvelopeUI
import kotlinx.coroutines.delay
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.available_until
import moinobudget.composeapp.generated.resources.create_envelope
import moinobudget.composeapp.generated.resources.create_envelope_details
import moinobudget.composeapp.generated.resources.remaining_is
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import presentation.component.AddCard
import presentation.data.IncomeOrOutcome
import presentation.formatCurrency
import util.formatDate
import util.toHue

@Composable
fun EnvelopeScreen(
    state: EnvelopeState,
    preferences: AppPreferences,
    addEditEnvelope: (Int?) -> Unit
) = Column {

    Spacer(Modifier.height(48.dp))

    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(state.envelopes) { envelope ->
            EnvelopeItem(
                modifier = Modifier,
                preferences = preferences,
                envelope = envelope,
                onClick = { addEditEnvelope(envelope.id) }
            )
        }
        item {
            AddCard(
                title = stringResource(Res.string.create_envelope),
                description = stringResource(Res.string.create_envelope_details),
                onClick = { addEditEnvelope(null) }
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

        val envelopeColor = envelope.color ?: MaterialTheme.colorScheme.primary
        val darkBackground = Color.hsl(hue = envelopeColor.toHue(), lightness = 0.12f, saturation = 1f)

        Surface(
            contentColor = darkBackground,
            color = envelopeColor
        ) {
            Column(Modifier
                //.background(darkBackground)
                .padding(horizontal = 24.dp)
            ) {
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
                    Text("${formatDate(envelope.startPeriod, preferences)} - ${formatDate(envelope.endPeriod, preferences)}",
                        style = MaterialTheme.typography.titleMedium)
                }
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

            val accentColor = lerp(envelopeColor, IncomeOrOutcome.Outcome.color, progress)
            val darkAccentColor = Color.hsl(hue = accentColor.toHue(), lightness = 0.15f, saturation = 1f)
            Row(Modifier.padding(top = 8.dp).padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier
                    .padding(vertical = 8.dp)
                    .padding(end = 12.dp)
                    .clip(CircleShape)
                    .height(20.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .weight(1f)) {
                    Box(Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(currentProgress.value)
                        .clip(CircleShape)
                        .background(accentColor)) {
                        Text("${(progress * 100).toInt()}%",
                            modifier = Modifier.align(Alignment.Center),
                            fontWeight = FontWeight.SemiBold,
                            color = darkAccentColor,
                            style = MaterialTheme.typography.titleSmall)
                    }
                }
                Text(formatCurrency(envelope.max.toFloat(), preferences),
                    style = MaterialTheme.typography.titleMedium)
            }

            envelope.remainingMoney?.let { remainingMoney ->
                val remainingMoneyFormatted = formatCurrency(remainingMoney.toFloat() / envelope.remainingDays, preferences)
                val fullText = pluralStringResource(Res.plurals.available_until,envelope.remainingDays, remainingMoneyFormatted, envelope.remainingDays)
                val styledText = buildAnnotatedString {
                    val splitText = fullText.split(remainingMoneyFormatted, envelope.remainingDays.toString())
                    val accentStyle = SpanStyle(
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold)
                    append(splitText[0])
                    withStyle(style = accentStyle) { append(remainingMoneyFormatted) }
                    append(splitText[1])
                    withStyle(style = accentStyle) { append(envelope.remainingDays.toString()) }
                    append(splitText[2])
                }
                Text(styledText,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontStyle = FontStyle.Italic,
                    fontSize = 16.sp)
            }
        }
    }

}