package feature.savings.presentation.dialog

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import data.repository.AppPreferences
import feature.dashboard.presentation.component.LabelSelection
import feature.savings.data.AddEditSavings
import kotlinx.coroutines.delay
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.amount
import moinobudget.composeapp.generated.resources.close_dialog_description
import moinobudget.composeapp.generated.resources.delete_savings
import moinobudget.composeapp.generated.resources.edit_savings
import moinobudget.composeapp.generated.resources.goal
import moinobudget.composeapp.generated.resources.labels
import moinobudget.composeapp.generated.resources.new_savings
import moinobudget.composeapp.generated.resources.save
import moinobudget.composeapp.generated.resources.subtitle
import moinobudget.composeapp.generated.resources.title
import org.jetbrains.compose.resources.stringResource
import presentation.data.LabelUI
import presentation.data.SavingsType
import presentation.data.SavingsUI
import presentation.shake

@Composable
fun AddEditSavingsDialog(
    savings: SavingsUI?,
    preferences: AppPreferences,
    labels: List<LabelUI>,
    onDismiss: () -> Unit,
    saveSavings: (AddEditSavings) -> Unit,
    deleteSavings: (Int) -> Unit
) = Dialog(onDismissRequest = onDismiss) {
    var deleteMode by remember { mutableStateOf(false) }

    var amountError by remember { mutableStateOf(false) }
    var goalError by remember { mutableStateOf(false) }

    var savingsTitle by remember { mutableStateOf(savings?.title ?: "") }
    var savingsSubtitle by remember { mutableStateOf(savings?.subtitle ?: "") }
    var savingsAmount by remember { mutableStateOf(savings?.amount?.toString() ?: "") }
    var savingsGoal by remember { mutableStateOf(savings?.goal?.toString() ?: "") }
    var savingsLabel by remember { mutableStateOf(savings?.label?.id) }

    val currentProgress = remember { androidx.compose.animation.core.Animatable(0f) }
    val primary = MaterialTheme.colorScheme.primary
    val colorProgress = remember { Animatable(labels.find { savings?.label?.id == it.id }?.color ?: primary) }

    savings?.goal?.let {
        LaunchedEffect(key1 = true) {
            delay(150)
            currentProgress.animateTo(
                targetValue = (savings.amount.toFloat() / savings.goal.toFloat()),
                animationSpec = tween(1500)
            )
        }
    }

    LaunchedEffect(key1 = savingsAmount, key2 = savingsGoal) {
        val amount = savingsAmount.toFloatOrNull()
        val goal = savingsGoal.toFloatOrNull()
        if (amount != null && goal != null) currentProgress.animateTo(
            targetValue = (amount / goal),
            animationSpec = tween(1500)
        )
    }

    LaunchedEffect(key1 = savingsLabel) {
        val targetColor = labels.find { savingsLabel == it.id }?.color
        colorProgress.animateTo(targetValue = targetColor ?: primary, animationSpec = tween(1000))
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceVariant),
        contentColor = MaterialTheme.colorScheme.onBackground,
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
                savings?.let {
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
                Text(stringResource(savings?.let { Res.string.edit_savings } ?: Res.string.new_savings),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold)
                IconButton(modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(Res.string.close_dialog_description)) }
            }

            Spacer(Modifier.height(8.dp))

            Column(Modifier.padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                var titleError by remember { mutableStateOf(false) }
                TextField(modifier = Modifier.shake(deleteMode).padding(horizontal = 64.dp).clip(RoundedCornerShape(16.dp)),
                    isError = titleError,
                    value = savingsTitle,
                    onValueChange = {
                        if (titleError) titleError = false
                        if (it.length < 19) savingsTitle = it },
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
                    value = savingsSubtitle,
                    onValueChange = { if (it.length < 19) savingsSubtitle = it },
                    label = { Text(stringResource(Res.string.subtitle)) },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    maxLines = 1,
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

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
                        value = savingsAmount,
                        onValueChange = {
                            if (amountError) amountError = false
                            savingsAmount = it
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
                        value = savingsGoal,
                        onValueChange = {
                            if (goalError) goalError = false
                            savingsGoal = it
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
                Text(stringResource(Res.string.labels),
                    modifier = Modifier.padding(start = 64.dp, top = 8.dp).fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                LabelSelection(
                    labels = labels,
                    selected = listOf(savingsLabel ?: -1),
                    onSelect = { labelId ->
                        savingsLabel = if (savingsLabel == labelId) null else labelId
                    },
                    deleteMode = deleteMode
                )
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
                                if (savingsTitle.isBlank()) titleError = true
                                else if (savingsAmount.toIntOrNull() == null) amountError = true
                                else if (savingsGoal.isNotBlank() && savingsGoal.toIntOrNull() == null) goalError = true
                                else {
                                    saveSavings(AddEditSavings(
                                        id = savings?.id,
                                        title = savingsTitle,
                                        type = SavingsType.Accounts,
                                        subtitle = savingsSubtitle,
                                        amount = savingsAmount.toInt(),
                                        goal = savingsGoal.toIntOrNull(),
                                        autoIncrement = 0,
                                        lastMonthAutoIncrement = null,
                                        labelId = savingsLabel
                                    ))
                                    onDismiss() } }
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = stringResource(Res.string.save))
                            Text(stringResource(Res.string.save),
                                modifier = Modifier.padding(horizontal = 8.dp),
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                    else savings?.id?.let {
                        TextButton(modifier = Modifier.padding(horizontal = 64.dp),
                            onClick = { deleteSavings(savings.id); onDismiss() }) {
                            Text(stringResource(Res.string.delete_savings),
                                color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}