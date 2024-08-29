package feature.savings.presentation

import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import data.repository.AppPreferences
import feature.savings.presentation.dialog.AddEditSavingsDialog
import kotlinx.coroutines.delay
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.goal_with_value
import moinobudget.composeapp.generated.resources.register_savings
import moinobudget.composeapp.generated.resources.register_savings_details
import moinobudget.composeapp.generated.resources.savings
import org.jetbrains.compose.resources.stringResource
import presentation.TopBackAndTitle
import presentation.dashedBorder
import presentation.data.BudgetStyle
import presentation.data.IncomeOrOutcome
import presentation.data.SavingsUI
import presentation.formatCurrency

@Composable
fun SavingsScreen(
    state: SavingsState,
    preferences: AppPreferences,
    onEvent: (SavingsEvent) -> Unit,
    style: BudgetStyle,
    goBack: () -> Unit
    //onEvent
) = MaterialTheme(
    colorScheme = MaterialTheme.colorScheme.copy(
        primary = style.getPrimary(preferences),
        onPrimary = style.getOnPrimary(preferences)
    )
) {

    var addSavingsDialog by remember { mutableStateOf(false) }
    var savingsForDialog by remember { mutableStateOf<SavingsUI?>(null) }

    if (addSavingsDialog) AddEditSavingsDialog(
        savings = savingsForDialog,
        preferences = preferences,
        labels = state.labels,
        saveSavings = { onEvent(SavingsEvent.UpsertSavings(it)) },
        deleteSavings = { onEvent(SavingsEvent.DeleteSavings(it)) },
        onDismiss = { addSavingsDialog = false; savingsForDialog = null }
    )

    Column {
        TopBackAndTitle(title = stringResource(Res.string.savings),
            goBack = goBack)
        Spacer(Modifier.height(16.dp))
        Crossfade(modifier = Modifier.fillMaxWidth(),
            targetState = state.total) { total ->
            Text(formatCurrency(total, preferences.copy(decimalMode = true)),
                modifier = Modifier.fillMaxWidth(1f).padding(horizontal = 16.dp),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                color = IncomeOrOutcome.Income.color)
        }
        Spacer(Modifier.height(16.dp))
        val listState = rememberLazyListState()
        Box(Modifier.weight(1f)) {
            Crossfade(modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth(),
                targetState = listState.canScrollBackward) { displayDivider ->
                if (displayDivider) HorizontalDivider(Modifier.fillMaxWidth(),
                    thickness = 2.dp)
            }
            LazyColumn(state = listState,
                modifier = Modifier.padding(horizontal = 16.dp)) {
            items(state.savings) { savings ->
                SavingsItem(modifier = Modifier.animateItem(),
                    savings = savings,
                    preferences = preferences,
                    onClick = { savingsForDialog = savings; addSavingsDialog = true })
            }
            item {
                Row(Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .dashedBorder(4.dp, MaterialTheme.colorScheme.primary, 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { addSavingsDialog = true }
                    .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.3f)),
                        tint = MaterialTheme.colorScheme.primary,
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(Res.string.register_savings_details))
                    Column(Modifier.padding(start = 16.dp)) {
                        Text(stringResource(Res.string.register_savings),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge)
                        Text(stringResource(Res.string.register_savings_details),
                            style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
        }
    }
}

@Composable
fun SavingsItem(
    modifier: Modifier,
    preferences: AppPreferences,
    savings: SavingsUI,
    onClick: () -> Unit
) = Box(modifier
    .fillMaxWidth()
    .padding(vertical = 12.dp)
    .clip(RoundedCornerShape(16.dp))
    .background(MaterialTheme.colorScheme.surface)
    .clickable { onClick() }
    .padding(16.dp)
) {
    val savingPrimary = savings.label?.color ?: MaterialTheme.colorScheme.primary

    val currentProgress = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        delay(150)
        currentProgress.animateTo(
            targetValue = (savings.amount.toFloat() / savings.goal.toFloat()),
            animationSpec = tween(1500)
        )
    }

    Column(Modifier.padding(horizontal = 8.dp)) {
        Row {
            Text(savings.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
            fontWeight = FontWeight.SemiBold)
            Text(
                formatCurrency(savings.amount.toFloat(), preferences),
                color = savingPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold)
        }
        Text(savings.subtitle,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold)
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(6.dp),
            progress = { currentProgress.value },
            color = savingPrimary,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            drawStopIndicator = {}
        )
        Text(
            stringResource(Res.string.goal_with_value, formatCurrency(savings.goal.toFloat(), preferences)),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.End)
    }
}