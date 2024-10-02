package feature.expenses.feature.envelope_history

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import data.repository.AppPreferences
import feature.expenses.data.EnvelopeUI
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.go_back
import moinobudget.composeapp.generated.resources.history
import moinobudget.composeapp.generated.resources.left_of
import moinobudget.composeapp.generated.resources.spend
import org.jetbrains.compose.resources.stringResource
import presentation.data.ExpenseFrequency
import presentation.data.IncomeOrOutcome
import presentation.formatCurrency
import util.getMonthByMonthNumber
import util.toHue

@OptIn(ExperimentalMaterial3Api::class)
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
    val color = state.color?.let {
        Color.hsl(hue = it.toHue(), lightness = 0.14f, saturation = 1f)
    } ?: MaterialTheme.colorScheme.onPrimary
    val contentColor = state.color ?: MaterialTheme.colorScheme.primary

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.history)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = color
                ),
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.go_back)) } }
            )
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                itemsIndexed(state.envelopes) { index, envelope ->
                    EnvelopeHistoryItem(
                        Modifier,
                        isCurrent = index == 0,
                        preferences = preferences,
                        envelope = envelope,
                        onClick = { openDetails(envelope.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun EnvelopeHistoryItem(
    modifier: Modifier,
    isCurrent: Boolean,
    preferences: AppPreferences,
    envelope: EnvelopeUI,
    onClick: () -> Unit
) = Box(modifier
    .fillMaxWidth()
    .padding(vertical = 6.dp)
    .clip(RoundedCornerShape(16.dp))
    .background(MaterialTheme.colorScheme.surface)
    .clickable { onClick() }) {
    Row(Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(envelope.getPeriodText(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (envelope.remainingMoney != null && envelope.max != null) {
                    Text(
                        formatCurrency(envelope.remainingMoney.toFloat(), preferences),
                        modifier = Modifier.padding(horizontal = 4.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold)
                    Text(
                        stringResource(Res.string.left_of, envelope.max),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal)
                }
                else {
                    Text(
                        formatCurrency(envelope.current.toFloat(), preferences),
                        modifier = Modifier.padding(horizontal = 4.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold)
                    Text(
                        stringResource(Res.string.spend),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal)
                }
            }
        }
        envelope.max?.let { max ->
            val percentage = envelope.current.toFloat() / max
            val color =
                if (envelope.current.toFloat() > max) IncomeOrOutcome.Outcome.color
                else envelope.color ?: MaterialTheme.colorScheme.primary
            val current by animateFloatAsState(
                targetValue = percentage,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing), label = "",
            )
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(56.dp),
                    progress = { current },
                    color = color
                )
                Text("${(percentage*100).toInt()}%",
                    fontWeight = FontWeight.Bold,
                    color = color,
                    style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun EnvelopeUI.getPeriodText(): String = when (this.frequency) {
    ExpenseFrequency.Monthly -> getMonthByMonthNumber(this.startPeriod.monthNumber)
    ExpenseFrequency.Annually -> this.startPeriod.year.toString()
    //ExpenseFrequency.Quarterly -> TODO()
    //ExpenseFrequency.Biannually -> TODO()
    else -> "Not supported"
}