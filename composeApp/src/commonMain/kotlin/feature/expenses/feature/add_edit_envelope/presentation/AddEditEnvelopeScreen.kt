package feature.expenses.feature.add_edit_envelope.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import data.repository.AppPreferences
import feature.savings.feature.savings_list.presentation.component.ColorSection
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.close_dialog_description
import moinobudget.composeapp.generated.resources.delete_budget
import moinobudget.composeapp.generated.resources.delete_operation
import moinobudget.composeapp.generated.resources.edit_envelope
import moinobudget.composeapp.generated.resources.icon
import moinobudget.composeapp.generated.resources.limit
import moinobudget.composeapp.generated.resources.save
import moinobudget.composeapp.generated.resources.save_envelope
import moinobudget.composeapp.generated.resources.title
import org.jetbrains.compose.resources.stringResource
import presentation.component.IconSelector
import presentation.component.YearMonthSwitch
import presentation.data.ExpenseFrequency
import presentation.shake

@Composable
fun AddEditEnvelopeScreen(
    state: AddEditEnvelopeState,
    preferences: AppPreferences,
    onEvent: (AddEditEnvelopeEvent) -> Unit,
    uiEvent: SharedFlow<AddEditEnvelopeViewModel.UiEvent>,
    goBack: () -> Unit,
    deleteEntry: () -> Unit
) {

    var deleteMode by remember { mutableStateOf(false) }

    var maxError by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        uiEvent.collectLatest { event ->
            when (event) {
                is AddEditEnvelopeViewModel.UiEvent.DeleteQuit -> deleteEntry()
            }
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
                state.envelopeId?.let {
                    Crossfade(targetState = deleteMode) { deletion ->
                        IconButton(modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clip(CircleShape)
                            .border(2.dp,
                                if (!deletion) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                                CircleShape
                            ),
                            onClick = { deleteMode = !deleteMode }) {
                            Icon(imageVector = if (!deletion) Icons.Default.Delete else Icons.Default.Edit,
                                tint = if (!deletion) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                                contentDescription = stringResource(Res.string.delete_budget))
                        }
                    }
                }
                Text(stringResource(state.envelopeId?.let { Res.string.edit_envelope } ?: Res.string.save_envelope),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold)
                IconButton(modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = goBack) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(Res.string.close_dialog_description)) }
            }
            Spacer(Modifier.height(16.dp))

            Column(Modifier.padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(Modifier.height(4.dp))

                // Element - Frequency
                YearMonthSwitch(
                    modifier = Modifier.shake(deleteMode),
                    year = state.frequency == ExpenseFrequency.Annually.id,
                    onChange = { onEvent(
                        AddEditEnvelopeEvent.UpdateFrequency(if (it) ExpenseFrequency.Annually.id else ExpenseFrequency.Monthly.id)
                    ) })

                Spacer(Modifier.height(16.dp))

                // Element - Title
                var titleError by remember { mutableStateOf(false) }
                TextField(modifier = Modifier.shake(deleteMode).clip(RoundedCornerShape(16.dp)),
                    isError = titleError,
                    value = state.title,
                    onValueChange = {
                        if (titleError) titleError = false
                        if (it.length < 19) onEvent(AddEditEnvelopeEvent.UpdateTitle(it)) },
                    label = { Text(stringResource(Res.string.title)) },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    maxLines = 1,
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(Modifier.height(16.dp))

                TextField(modifier = Modifier.shake(deleteMode).clip(RoundedCornerShape(16.dp)),
                    isError = maxError,
                    value = state.max,
                    onValueChange = {
                        if (maxError) maxError = false
                        onEvent(AddEditEnvelopeEvent.UpdateMax(it))
                    },
                    label = { Text(stringResource(Res.string.limit)) },
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

                Spacer(Modifier.height(16.dp))

                Text(stringResource(Res.string.icon),
                    modifier = Modifier.padding(start = 64.dp, top = 8.dp).fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                IconSelector(Modifier,
                    selectedIcon = state.iconId,
                    onSelect = { onEvent(AddEditEnvelopeEvent.UpdateIcon(it)) },
                    deleteMode = deleteMode
                )

                Spacer(Modifier.height(16.dp))

                ColorSection(
                    modifier = Modifier.shake(deleteMode),
                    selection = state.color,
                    colorSelect = { onEvent(AddEditEnvelopeEvent.UpdateColor(if (state.color == it) null else it)) })

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
                                titleError = state.title.isBlank()
                                state.max.toFloatOrNull()
                                maxError = state.max.isNotBlank() && (state.max.toFloatOrNull() == null)
                                if (!titleError && !maxError) {
                                    onEvent(AddEditEnvelopeEvent.UpsertEnvelope)
                                    goBack() }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = stringResource(Res.string.save))
                            Text(stringResource(Res.string.save),
                                modifier = Modifier.padding(horizontal = 8.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                    else state.envelopeId?.let {
                        TextButton(modifier = Modifier.padding(horizontal = 64.dp),
                            onClick = { onEvent(AddEditEnvelopeEvent.DeleteEnvelope) }) {
                            Text(stringResource(Res.string.delete_operation),
                                color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }

}