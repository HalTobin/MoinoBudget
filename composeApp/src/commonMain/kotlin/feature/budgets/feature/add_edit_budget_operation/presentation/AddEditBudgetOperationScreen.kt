package feature.budgets.feature.add_edit_budget_operation.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInVertically
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
import feature.budgets.feature.add_edit_budget_operation.presentation.component.SelectDay
import feature.budgets.feature.add_edit_budget_operation.presentation.component.SelectMonthOption
import feature.budgets.feature.budgets_list.presentation.component.LabelSelection
import feature.budgets.feature.budgets_list.presentation.component.TextSwitch
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.amount
import moinobudget.composeapp.generated.resources.close_dialog_description
import moinobudget.composeapp.generated.resources.create_operation
import moinobudget.composeapp.generated.resources.delete_budget
import moinobudget.composeapp.generated.resources.delete_operation
import moinobudget.composeapp.generated.resources.edit_operation
import moinobudget.composeapp.generated.resources.icon
import moinobudget.composeapp.generated.resources.labels
import moinobudget.composeapp.generated.resources.save
import moinobudget.composeapp.generated.resources.title
import org.jetbrains.compose.resources.stringResource
import presentation.component.IconSelector
import presentation.component.YearMonthSwitch
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.IncomeOrOutcome
import presentation.shake

@Composable
fun AddEditBudgetOperationScreen(
    preferences: AppPreferences,
    state: AddEditBudgetOperationState,
    onEvent: (AddEditBudgetOperationEvent) -> Unit,
    goBack: () -> Unit,
) {
    var deleteMode by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        Column(Modifier.safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
                state.budgetOperationId?.let {
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
                                contentDescription = stringResource(Res.string.delete_budget))
                        }
                    }
                }
                Text(stringResource(state.budgetOperationId?.let { Res.string.edit_operation } ?: Res.string.create_operation),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold)
                IconButton(modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = goBack) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(Res.string.close_dialog_description)) }
            }
            Spacer(Modifier.height(24.dp))

            Column(Modifier.padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(Modifier.height(4.dp))

                // Element - Income Or Outcome
                TextSwitch(
                    items = IncomeOrOutcome.list.map { stringResource(it.textSingular) },
                    modifier = Modifier.shake(deleteMode).width(256.dp).height(48.dp),
                    selectedIndex = state.incomeOrOutcome.tabId,
                    onSelectionChange = {
                        onEvent(
                            AddEditBudgetOperationEvent.UpdateIncomeOrOutcome(
                                IncomeOrOutcome.getByTabId(
                                    it
                                )
                            )
                        )
                    }
                )
                Spacer(Modifier.height(24.dp))

                // Element - Title
                var titleError by remember { mutableStateOf(false) }
                var amountError by remember { mutableStateOf(false) }
                Row(Modifier.padding(horizontal = 16.dp)) {
                    TextField(modifier = Modifier.shake(deleteMode).weight(3f).clip(RoundedCornerShape(16.dp)),
                        isError = titleError,
                        value = state.title,
                        onValueChange = {
                            if (titleError) titleError = false
                            if (it.length < 19) onEvent(AddEditBudgetOperationEvent.UpdateTitle(it)) },
                        label = { Text(stringResource(Res.string.title)) },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
                        ),
                        maxLines = 1,
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )
                    Spacer(Modifier.width(16.dp))

                    // Element - Amount
                    TextField(modifier = Modifier.shake(deleteMode).weight(2f).clip(RoundedCornerShape(16.dp)),
                        isError = amountError,
                        value = state.amount,
                        trailingIcon = { Text(preferences.currency.sign,
                            fontWeight = FontWeight.Bold) },
                        onValueChange = {
                            if (amountError) amountError = false
                            onEvent(AddEditBudgetOperationEvent.UpdateAmount(it))
                        },
                        label = { Text(stringResource(Res.string.amount)) },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
                        ),
                        maxLines = 1,
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = if (preferences.decimalMode) KeyboardType.Decimal else KeyboardType.Number)
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Element - Frequency
                /*TextSwitch(
                    items = ExpenseFrequency.list.map { stringResource(it.title) },
                    modifier = Modifier.shake(deleteMode).width(256.dp).height(48.dp),
                    selectedIndex = state.frequency.id,
                    onSelectionChange = { onEvent(
                        AddEditBudgetOperationEvent.UpdateFrequency(
                            ExpenseFrequency.findById(it)
                        )
                    ) }
                )*/
                YearMonthSwitch(
                    modifier = Modifier.shake(deleteMode),
                    year = state.frequency.id == ExpenseFrequency.Annually.id,
                    onChange = { onEvent(
                        AddEditBudgetOperationEvent.UpdateFrequency(
                            ExpenseFrequency.findById(if (it) ExpenseFrequency.Annually.id else ExpenseFrequency.Monthly.id)
                        )
                    ) })

                Spacer(Modifier.height(16.dp))
                AnimatedContent(state.frequency.options.isNotEmpty()) { monthSection ->
                    Row {
                        SelectDay(
                            modifier = Modifier.shake(deleteMode),
                            value = state.day,
                            onChange = { onEvent(AddEditBudgetOperationEvent.UpdateDay(it)) },
                            month = state.month)
                        if (monthSection) {
                            Spacer(Modifier.width(16.dp))
                            SelectMonthOption(Modifier.shake(deleteMode).fillMaxWidth(0.55f),
                                options = state.frequency.options,
                                value = state.month,
                                onSelect = { onEvent(AddEditBudgetOperationEvent.UpdateMonthOffset(it)) })
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))

                Text(stringResource(Res.string.icon),
                    modifier = Modifier.padding(start = 64.dp, top = 8.dp).fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                IconSelector(Modifier,
                    selectedIcon = state.icon.id,
                    onSelect = { onEvent(AddEditBudgetOperationEvent.UpdateIcon(ExpenseIcon.findById(it))) },
                    deleteMode = deleteMode
                )
                Text(stringResource(Res.string.labels),
                    modifier = Modifier.padding(start = 64.dp, top = 8.dp).fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Element - Label
                Row(Modifier.padding(horizontal = 48.dp)) {
                    LabelSelection(
                        labels = state.availableLabels,
                        selected = state.selectedLabels,
                        onSelect = { onEvent(AddEditBudgetOperationEvent.UpdateLabel(it)) },
                        deleteMode = deleteMode
                    )
                }
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
                                state.amount.toFloatOrNull()
                                amountError = (state.amount.toFloatOrNull() == null)
                                if (!titleError && !amountError) {
                                    onEvent(AddEditBudgetOperationEvent.UpsertBudgetOperation)
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
                    else state.budgetOperationId?.let {
                        TextButton(modifier = Modifier.padding(horizontal = 64.dp),
                            onClick = { onEvent(AddEditBudgetOperationEvent.DeleteBudgetOperation); goBack() }) {
                            Text(stringResource(Res.string.delete_operation),
                                color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}