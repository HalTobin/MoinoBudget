package feature.add_edit_expense.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
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
import feature.add_edit_expense.presentation.component.SelectDay
import feature.add_edit_expense.presentation.component.SelectMonthOption
import feature.dashboard.presentation.component.LabelSelection
import feature.dashboard.presentation.component.TextSwitch
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.amount
import moinobudget.composeapp.generated.resources.close_dialog_description
import moinobudget.composeapp.generated.resources.create_operation
import moinobudget.composeapp.generated.resources.delete_budget
import moinobudget.composeapp.generated.resources.edit_operation
import moinobudget.composeapp.generated.resources.icon
import moinobudget.composeapp.generated.resources.labels
import moinobudget.composeapp.generated.resources.save
import moinobudget.composeapp.generated.resources.title
import org.jetbrains.compose.resources.stringResource
import presentation.data.BudgetStyle
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.IncomeOrOutcome
import presentation.data.MonthOption
import presentation.shake

@Composable
fun AddEditExpenseScreen(
    preferences: AppPreferences,
    state: AddEditExpenseState,
    onEvent: (AddEditExpenseEvent) -> Unit,
    style: BudgetStyle,
    labels: List<Int>,
    //expense: ExpenseUI?,
    //budgetLabels: List<Int>,
    goBack: () -> Unit,
) {
    var deleteMode by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        if (state.labels.isEmpty()) labels.forEach { onEvent(AddEditExpenseEvent.UpdateLabel(it)) }
    }

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = style.getPrimary(preferences),
            onPrimary = style.getOnPrimary(preferences)
        )
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
                    state.expenseId?.let {
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
                    Text(stringResource(state.expenseId?.let { Res.string.edit_operation } ?: Res.string.create_operation),
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
                        modifier = Modifier.width(256.dp).height(48.dp),
                        selectedIndex = state.expenseIncomeOrOutcome.id,
                        onSelectionChange = { onEvent(AddEditExpenseEvent.UpdateIncomeOrOutcome(IncomeOrOutcome.getById(it))) }
                    )
                    Spacer(Modifier.height(24.dp))

                    // Element - Title
                    var titleError by remember { mutableStateOf(false) }
                    var amountError by remember { mutableStateOf(false) }
                    Row(Modifier.padding(horizontal = 16.dp)) {
                        TextField(modifier = Modifier.shake(deleteMode).weight(2f).clip(RoundedCornerShape(16.dp)),
                            isError = titleError,
                            value = state.expenseTitle,
                            onValueChange = {
                                if (titleError) titleError = false
                                if (it.length < 19) onEvent(AddEditExpenseEvent.UpdateTitle(it)) },
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
                        TextField(modifier = Modifier.shake(deleteMode).weight(1f).clip(RoundedCornerShape(16.dp)),
                            isError = amountError,
                            value = state.expenseAmount,
                            onValueChange = {
                                if (amountError) amountError = false
                                onEvent(AddEditExpenseEvent.UpdateAmount(it))
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
                    TextSwitch(
                        items = ExpenseFrequency.list.map { stringResource(it.title) },
                        modifier = Modifier.width(256.dp).height(48.dp),
                        selectedIndex = state.expenseFrequency.id,
                        onSelectionChange = { onEvent(AddEditExpenseEvent.UpdateFrequency(ExpenseFrequency.findById(it))) }
                    )
                    Spacer(Modifier.height(16.dp))
                    AnimatedContent(state.expenseFrequency.options.isNotEmpty()) { monthSection ->
                        Row {
                            SelectDay(
                                value = state.expenseDay,
                                onChange = { onEvent(AddEditExpenseEvent.UpdateDay(it)) },
                                month = state.expenseMonth)
                            if (monthSection) {
                                Spacer(Modifier.width(16.dp))
                                SelectMonthOption(Modifier.fillMaxWidth(0.5f),
                                    options = state.expenseFrequency.options,
                                    value = state.expenseMonth,
                                    onSelect = { onEvent(AddEditExpenseEvent.UpdateMonthOffset(it)) })
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))

                    Text(stringResource(Res.string.icon),
                        modifier = Modifier.padding(start = 64.dp, top = 8.dp).fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    LazyHorizontalGrid(GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        modifier = Modifier.height(128.dp)) {
                        items(ExpenseIcon.list) { icon ->
                            IconEntry(icon = icon,
                                selectedIcon = state.expenseIcon,
                                onClick = { onEvent(AddEditExpenseEvent.UpdateIcon(icon)) }
                            )
                        }
                    }
                    Text(stringResource(Res.string.labels),
                        modifier = Modifier.padding(start = 64.dp, top = 8.dp).fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Element - Label
                    Row(Modifier.padding(horizontal = 48.dp)) {
                        LabelSelection(
                            labels = state.labels,
                            selected = state.expenseLabels,
                            onSelect = { onEvent(AddEditExpenseEvent.UpdateLabel(it)) },
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
                                    titleError = state.expenseTitle.isBlank()
                                    state.expenseAmount.toFloatOrNull()
                                    amountError = (state.expenseAmount.toFloatOrNull() == null)
                                    if (!titleError && !amountError) {
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
                        else state.expenseId?.let { expenseId ->
                            TextButton(onClick = { onEvent(AddEditExpenseEvent.DeleteExpense); goBack() }) {
                                Text(stringResource(Res.string.delete_budget),
                                    color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IconEntry(
    icon: ExpenseIcon,
    selectedIcon: ExpenseIcon,
    onClick: () -> Unit,
) {
    val selected = selectedIcon == icon
    Box(modifier = Modifier
        .aspectRatio(1f)
        .padding(horizontal = 4.dp, vertical = 6.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(
            if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surface)
        .clickable { onClick() }
        .padding(4.dp),
        contentAlignment = Alignment.Center) {
        Icon(modifier = Modifier.size(30.dp),
            tint = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
            imageVector = icon.icon,
            contentDescription = null)
    }
}