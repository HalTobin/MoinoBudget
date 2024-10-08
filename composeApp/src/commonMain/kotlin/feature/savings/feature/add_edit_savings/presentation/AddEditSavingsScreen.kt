package feature.savings.feature.add_edit_savings.presentation

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import data.repository.AppPreferences
import feature.savings.data.SavingsType
import feature.savings.feature.savings_list.presentation.component.ColorSection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.amount
import moinobudget.composeapp.generated.resources.close_dialog_description
import moinobudget.composeapp.generated.resources.color
import moinobudget.composeapp.generated.resources.delete_savings
import moinobudget.composeapp.generated.resources.edit_savings
import moinobudget.composeapp.generated.resources.goal
import moinobudget.composeapp.generated.resources.new_savings
import moinobudget.composeapp.generated.resources.save
import moinobudget.composeapp.generated.resources.subtitle
import moinobudget.composeapp.generated.resources.title
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import presentation.component.IconSelector
import presentation.shake

@Composable
fun AddEditSavingsScreen(
    state: AddEditSavingsState,
    onEvent: (AddEditSavingsEvent) -> Unit,
    uiEvent: SharedFlow<AddEditSavingsViewModel.UiEvent>,
    preferences: AppPreferences,
    goBack: () -> Unit,
    deleteEntry: () -> Unit,
    defaultSavings: SavingsType,
) {
    var deleteMode by remember { mutableStateOf(false) }

    var amountError by remember { mutableStateOf(false) }
    var goalError by remember { mutableStateOf(false) }

    val currentProgress = remember { androidx.compose.animation.core.Animatable(0f) }
    val primary = MaterialTheme.colorScheme.primary
    val colorProgress = remember { Animatable(state.savingsColor ?: primary) }

    LaunchedEffect(key1 = true) {
        if (state.savingsId == null) onEvent(AddEditSavingsEvent.UpdateType(defaultSavings))
        uiEvent.collectLatest { event ->
            when (event) {
                is AddEditSavingsViewModel.UiEvent.QuitDelete -> deleteEntry()
            }
        }
    }

    state.savingsGoal.toIntOrNull()?.let { goal ->
        state.savingsAmount.toIntOrNull()?.let { amount ->
            LaunchedEffect(key1 = true) {
                delay(150)
                currentProgress.animateTo(
                    targetValue = (amount.toFloat() / goal.toFloat()),
                    animationSpec = tween(1500)
                )
            }
        }
    }

    LaunchedEffect(key1 = state.savingsAmount, key2 = state.savingsGoal) {
        val amount = state.savingsAmount.toFloatOrNull()
        val goal = state.savingsGoal.toFloatOrNull()
        if (amount != null && goal != null) currentProgress.animateTo(
            targetValue = (amount / goal),
            animationSpec = tween(1500)
        )
    }

    LaunchedEffect(key1 = state.savingsColor) {
        colorProgress.animateTo(targetValue = state.savingsColor ?: primary, animationSpec = tween(1000))
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        Column(Modifier.safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
                state.savingsId?.let {
                    Crossfade(targetState = deleteMode) { deletion ->
                        IconButton(modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clip(CircleShape)
                            .border(2.dp,
                                if (!deletion) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                                CircleShape),
                            onClick = { deleteMode = !deleteMode }) {
                            Icon(imageVector = if (!deletion) Icons.Default.Delete else Icons.Default.Edit,
                                tint = if (!deletion) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                                contentDescription = stringResource(Res.string.delete_savings))
                        }
                    }
                }
                Text(stringResource(state.savingsId?.let { Res.string.edit_savings } ?: Res.string.new_savings),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold)
                IconButton(modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = goBack) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(Res.string.close_dialog_description)) }
            }

            Spacer(Modifier.height(8.dp))

            Column(Modifier.padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                var titleError by remember { mutableStateOf(false) }
                TextField(modifier = Modifier.shake(deleteMode).padding(horizontal = 64.dp).clip(RoundedCornerShape(16.dp)),
                    isError = titleError,
                    value = state.savingsTitle,
                    onValueChange = {
                        if (titleError) titleError = false
                        if (it.length < 19) onEvent(AddEditSavingsEvent.UpdateTitle(it)) },
                    label = { Text(stringResource(Res.string.title)) },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    maxLines = 1,
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(Modifier.height(8.dp))
                TextField(modifier = Modifier.shake(deleteMode).padding(horizontal = 48.dp).clip(RoundedCornerShape(16.dp)),
                    isError = titleError,
                    value = state.savingsSubtitle,
                    onValueChange = { if (it.length < 19) onEvent(AddEditSavingsEvent.UpdateSubtitle(it)) },
                    label = { Text(stringResource(Res.string.subtitle)) },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    maxLines = 1,
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(Modifier.height(8.dp))

                Row(Modifier.padding(horizontal = 48.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    var animationDirection by remember { mutableStateOf(AnimationDirection.Left) }
                    IconButton(onClick = {
                        animationDirection = AnimationDirection.Left
                        val savingsType = SavingsType.entries.getOrElse(state.savingsType.id-1) { SavingsType.entries.last() }
                        onEvent(AddEditSavingsEvent.UpdateType(savingsType))
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowLeft, contentDescription = null)
                    }
                    AnimatedContent(targetState = state.savingsType,
                        transitionSpec = {
                            when (animationDirection) {
                                AnimationDirection.Left -> slideInHorizontally(initialOffsetX = { it / 2 }) + fadeIn() togetherWith slideOutHorizontally() + fadeOut()
                                AnimationDirection.Right -> slideInHorizontally() + fadeIn() togetherWith slideOutHorizontally(targetOffsetX = { it / 2 }) + fadeOut()
                            }
                        },
                        modifier = Modifier.weight(1f),
                    ) { type ->
                        Text(pluralStringResource(type.text, 2),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium)
                    }
                    IconButton(onClick = {
                        animationDirection = AnimationDirection.Right
                        val savingsType = SavingsType.entries.getOrElse(state.savingsType.id+1) { SavingsType.entries.first() }
                        onEvent(AddEditSavingsEvent.UpdateType(savingsType))
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null)
                    }
                }
                ColorSection(
                    modifier = Modifier.shake(deleteMode),
                    selection = state.savingsColor,
                    colorSelect = { onEvent(AddEditSavingsEvent.UpdateColor(if (state.savingsColor == it) null else it)) })
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    modifier = Modifier.shake(deleteMode).fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp).height(6.dp),
                    progress = { currentProgress.value },
                    color = colorProgress.value,
                    trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    drawStopIndicator = {}
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.padding(horizontal = 16.dp)) {
                    TextField(modifier = Modifier.shake(deleteMode).weight(1f).clip(RoundedCornerShape(16.dp)),
                        isError = amountError,
                        value = state.savingsAmount,
                        onValueChange = {
                            if (amountError) amountError = false
                            onEvent(AddEditSavingsEvent.UpdateAmount(it))
                        },
                        label = { Text(stringResource(Res.string.amount)) },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
                        ),
                        trailingIcon = { Text(preferences.currency.sign,
                            fontWeight = FontWeight.Bold) },
                        maxLines = 1,
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = if (preferences.decimalMode) KeyboardType.Decimal else KeyboardType.Number)
                    )
                    Spacer(Modifier.width(16.dp))
                    TextField(modifier = Modifier.shake(deleteMode).weight(1f).clip(RoundedCornerShape(16.dp)),
                        isError = goalError,
                        value = state.savingsGoal,
                        onValueChange = {
                            if (goalError) goalError = false
                            onEvent(AddEditSavingsEvent.UpdateGoal(it))
                        },
                        label = { Text(stringResource(Res.string.goal)) },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
                        ),
                        trailingIcon = { Text(preferences.currency.sign,
                            fontWeight = FontWeight.Bold) },
                        maxLines = 1,
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = if (preferences.decimalMode) KeyboardType.Decimal else KeyboardType.Number)
                    )
                }

                Spacer(Modifier.height(8.dp))
                Text(stringResource(Res.string.color),
                    modifier = Modifier.padding(start = 64.dp, top = 8.dp).fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                IconSelector(Modifier,
                    selectedIcon = state.savingsIconId,
                    onSelect = { onEvent(AddEditSavingsEvent.UpdateIcon(it)) },
                    deleteMode = deleteMode)

                Spacer(Modifier.weight(1f))
                AnimatedContent(modifier = Modifier.fillMaxWidth(),
                    targetState = deleteMode,
                    transitionSpec = {
                        slideInVertically(initialOffsetY = { it }) togetherWith slideOutVertically(targetOffsetY = { it })
                    }
                ) { deletion ->
                    if (!deletion) Box(contentAlignment = Alignment.Center) {
                        Button(modifier = Modifier.padding(vertical = 4.dp),
                            shape = CircleShape,
                            onClick = {
                                if (state.savingsTitle.isBlank()) titleError = true
                                else if (state.savingsAmount.toIntOrNull() == null) amountError = true
                                else if (state.savingsGoal.isNotBlank() && state.savingsGoal.toIntOrNull() == null) goalError = true
                                else {
                                    onEvent(AddEditSavingsEvent.UpsertSavings)
                                    goBack() } }
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = stringResource(Res.string.save))
                            Text(stringResource(Res.string.save),
                                modifier = Modifier.padding(horizontal = 8.dp),
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                    else state.savingsId?.let {
                        TextButton(modifier = Modifier.padding(horizontal = 64.dp),
                            onClick = { onEvent(AddEditSavingsEvent.DeleteSavings) }) {
                            Text(stringResource(Res.string.delete_savings),
                                color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

enum class AnimationDirection { Left, Right }