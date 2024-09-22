package feature.savings.feature.savings_detail

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import data.repository.AppPreferences
import feature.savings.data.SavingsType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.add
import moinobudget.composeapp.generated.resources.close_savings_details
import moinobudget.composeapp.generated.resources.current_balance
import moinobudget.composeapp.generated.resources.current_goal
import moinobudget.composeapp.generated.resources.operation_amount
import moinobudget.composeapp.generated.resources.subtract
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import presentation.component.AmountAnimation
import presentation.component.AmountWithText
import presentation.component.EditFloatingButton
import presentation.component.ShimmerAmountWithText
import presentation.formatCurrency
import kotlin.math.roundToInt

@Composable
fun SavingsDetailsScreen(
    state: SavingsDetailsState,
    preferences: AppPreferences,
    onEvent: (SavingsDetailsEvent) -> Unit,
    goToEdit: (Int, SavingsType) -> Unit,
    goBack: () -> Unit
) = Scaffold(
    floatingActionButton = {
        EditFloatingButton(onClick = {
            state.savings?.let { savings -> goToEdit(savings.id, savings.type) }
        })
    }
) {
    val scope = rememberCoroutineScope()
    Surface(
        contentColor = MaterialTheme.colorScheme.onBackground,
        color = MaterialTheme.colorScheme.background
    ) {
        Box(Modifier.fillMaxSize()) {
            IconButton(modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
                onClick = goBack) {
                Icon(Icons.Default.Close, contentDescription = stringResource(Res.string.close_savings_details))
            }
            Column {
                val currentProgress = remember { Animatable(0f) }
                Crossfade(
                    targetState = state.savings == null,
                    modifier = Modifier.animateContentSize()
                ) {
                    state.savings?.let { savings ->
                        Column {
                            AmountWithText(
                                modifier = Modifier.padding(start = 24.dp, top = 16.dp),
                                preferences = preferences,
                                amount = savings.amount,
                                text = stringResource(Res.string.current_balance),
                                animation = AmountAnimation.Rolling
                            )
                            Spacer(Modifier.height(32.dp))
                            Column(Modifier.padding(horizontal = 16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    savings.icon?.let { Icon(it.icon,
                                        modifier = Modifier.size(40.dp),
                                        contentDescription = null) }
                                    Column(Modifier.padding(horizontal = 16.dp)) {
                                        Text(savings.title,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.SemiBold)
                                        Text(savings.subtitle,
                                            Modifier.padding(top = 4.dp),
                                            style = MaterialTheme.typography.titleMedium)
                                    }
                                }
                                Text(pluralStringResource(savings.type.text, 1),
                                    Modifier.padding(horizontal = 16.dp))
                                savings.goal?.let { goal ->
                                    val progress = savings.amount.toFloat() / goal

                                    LaunchedEffect(key1 = progress) {
                                        delay(300)
                                        currentProgress.animateTo(
                                            targetValue = progress,
                                            animationSpec = tween(1500)
                                        )
                                    }

                                    Spacer(Modifier.height(8.dp))

                                    Text(stringResource(Res.string.current_goal),
                                        Modifier.padding(vertical = 8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        LinearProgressIndicator(
                                            modifier = Modifier.weight(1f).height(16.dp),
                                            progress = { currentProgress.value },
                                            gapSize = 8.dp,
                                            color = savings.color ?: MaterialTheme.colorScheme.primary,
                                            trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                            drawStopIndicator = {}
                                        )
                                        Text(formatCurrency(goal.toFloat(), preferences),
                                            Modifier.padding(horizontal = 12.dp))
                                    }
                                }
                            }
                        }
                    } ?: LoadingDetails()
                }

                val shakeAnimation = remember { Animatable(0f) }
                var isAmountError by remember { mutableStateOf(false) }
                fun handleUpdateAmount(amount: String, operation: Operation) {
                    val amountAsInt = amount.toIntOrNull()
                    if (amountAsInt != null)
                        onEvent(SavingsDetailsEvent.UpdateAmount(amountAsInt, operation))
                    else {
                        isAmountError = true
                        scope.launch {
                            for (i in 0..10) {
                                when (i % 2) {
                                    0 -> shakeAnimation.animateTo(5f, spring(stiffness = 100_000f))
                                    else -> shakeAnimation.animateTo(-5f, spring(stiffness = 100_000f))
                                }
                            }
                            shakeAnimation.animateTo(0f)
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TextField(
                        modifier = Modifier
                            .padding(top = 32.dp, bottom = 8.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .offset { IntOffset(shakeAnimation.value.roundToInt(), y = 0) }
                            .graphicsLayer { translationX = shakeAnimation.value * 2 },
                        value = state.amountText,
                        singleLine = true,
                        maxLines = 1,
                        label = { Text(stringResource(Res.string.operation_amount)) },
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = { Text(preferences.currency.sign,
                            fontWeight = FontWeight.Bold) },
                        isError = isAmountError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                            if (isAmountError) isAmountError = false
                            onEvent(SavingsDetailsEvent.UpdateAmountField(it))
                        }
                    )
                    Row(Modifier.padding(horizontal = 16.dp)) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = { handleUpdateAmount(state.amountText, Operation.Subtract) }
                        ) {
                            Text(stringResource(Res.string.subtract))
                        }
                        Spacer(Modifier.width(16.dp))
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { handleUpdateAmount(state.amountText, Operation.Add) },
                        ) {
                            Text(stringResource(Res.string.add))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingDetails() = Column {
    ShimmerAmountWithText(Modifier.padding(start = 24.dp, top = 16.dp))
}