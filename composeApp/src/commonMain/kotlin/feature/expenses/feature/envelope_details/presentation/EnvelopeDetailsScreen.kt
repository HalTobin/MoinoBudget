package feature.expenses.feature.envelope_details.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.repository.AppPreferences
import feature.expenses.components.CustomProgressBar
import feature.expenses.components.RemainingMoney
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.add_expense
import moinobudget.composeapp.generated.resources.edit_envelope
import moinobudget.composeapp.generated.resources.go_back
import moinobudget.composeapp.generated.resources.left_of
import org.jetbrains.compose.resources.stringResource
import presentation.component.AddFloatingButton
import presentation.formatCurrency
import util.simpleFormatDate
import util.toHue

@Composable
fun EnvelopeDetailsScreen(
    state: EnvelopeDetailsState,
    preferences: AppPreferences,
    addEditEnvelope: (Int) -> Unit,
    addExpense: () -> Unit,
    goBack: () -> Unit
) = Scaffold(
    floatingActionButton = { AddFloatingButton(
        text = stringResource(Res.string.add_expense),
        onClick = { state.envelope?.let { addExpense() } }) }
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {

        val color = state.envelope?.color?.let {
            Color.hsl(hue = it.toHue(), lightness = 0.14f, saturation = 1f)
        } ?: MaterialTheme.colorScheme.onPrimary
        val contentColor = state.envelope?.color ?: MaterialTheme.colorScheme.primary

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Column(modifier = Modifier.background(color).padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row {
                    IconButton(onClick = goBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            tint = contentColor,
                            contentDescription = stringResource(Res.string.go_back))
                    }
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { state.envelope?.let { addEditEnvelope(it.id) } }) {
                        Icon(Icons.Default.Edit,
                            tint = contentColor,
                            contentDescription = stringResource(Res.string.edit_envelope))
                    }
                }
                state.envelope?.let { envelope ->
                    Row(Modifier.padding(top = 16.dp, start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        envelope.icon?.let { icon ->
                            Icon(icon.icon,
                                modifier = Modifier.size(40.dp),
                                tint = contentColor,
                                contentDescription = null)
                            Spacer(Modifier.width(12.dp))
                        }
                        Text(envelope.title,
                            modifier = Modifier.weight(1f),
                            color = contentColor,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.headlineLarge)
                    }
                    Row(Modifier.padding(horizontal = 12.dp).padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        if (envelope.remainingMoney != null && envelope.max != null) {
                            Text(
                                formatCurrency(envelope.remainingMoney.toFloat(), preferences),
                                modifier = Modifier.padding(horizontal = 4.dp),
                                color = contentColor,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(stringResource(Res.string.left_of, envelope.max),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Normal,
                                color = contentColor)
                        }
                        Spacer(Modifier.weight(1f))
                        Text("${simpleFormatDate(envelope.startPeriod, preferences)} - ${simpleFormatDate(envelope.endPeriod, preferences)}",
                            color = contentColor,
                            style = MaterialTheme.typography.titleMedium)
                    }
                    envelope.max?.let { max ->
                        Spacer(Modifier.height(16.dp))
                        CustomProgressBar(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            current = envelope.current,
                            height = 26.dp,
                            textSize = 20.sp,
                            max = max,
                            primaryColor = contentColor,
                            onPrimaryColor = color
                        )
                    }
                    envelope.remainingMoney?.let {
                        Spacer(Modifier.height(8.dp))
                        RemainingMoney(
                            remainingMoney = it,
                            remainingDays = envelope.remainingDays,
                            color = contentColor,
                            preferences = preferences
                        )
                    }
                }
            }
        }

    }

}