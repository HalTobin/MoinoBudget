package feature.dashboard.presentation.dialog

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import data.repository.AppPreferences
import feature.dashboard.presentation.component.LabelSelection
import feature.dashboard.presentation.component.TextSwitch
import feature.add_edit_expense.data.AddEditExpense
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.close_dialog_description
import moinobudget.composeapp.generated.resources.create_operation
import moinobudget.composeapp.generated.resources.delete_budget
import moinobudget.composeapp.generated.resources.edit_operation
import moinobudget.composeapp.generated.resources.icon
import moinobudget.composeapp.generated.resources.labels
import moinobudget.composeapp.generated.resources.save
import moinobudget.composeapp.generated.resources.title
import org.jetbrains.compose.resources.stringResource
import presentation.data.ExpenseIcon
import presentation.data.ExpenseUI
import presentation.data.IncomeOrOutcome
import presentation.data.LabelUI
import presentation.shake

@Composable
fun NewEditExpenseDialog(
    preferences: AppPreferences,
    expense: ExpenseUI?,
    labels: List<LabelUI>,
    budgetLabels: List<Int>,
    onDismiss: () -> Unit,
    saveExpense: (AddEditExpense) -> Unit,
    deleteExpense: (Int) -> Unit
) = Dialog(onDismissRequest = onDismiss) {
    var deleteMode by remember { mutableStateOf(false) }

    var expenseTitle by remember { mutableStateOf(expense?.title ?: "") }
    var expenseIncomeOrOutcome by remember { mutableStateOf(expense?.type ?: IncomeOrOutcome.Outcome) }
    var expenseIcon by remember { mutableStateOf(expense?.icon ?: ExpenseIcon.DefaultOutcome) }
    val expenseLabels = remember { mutableStateListOf<Int>() }

    LaunchedEffect(key1 = true) { expenseLabels.clear(); expenseLabels.addAll(budgetLabels) }

    Surface(
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceVariant),
        contentColor = MaterialTheme.colorScheme.onBackground,
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
                expense?.let {
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
                Text(stringResource(expense?.let { Res.string.edit_operation } ?: Res.string.create_operation),
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

                Spacer(Modifier.height(8.dp))

                TextSwitch(
                    items = IncomeOrOutcome.list.map { stringResource(it.textSingular) },
                    modifier = Modifier.width(256.dp).height(48.dp),
                    selectedIndex = expenseIncomeOrOutcome.id,
                    onSelectionChange = { expenseIncomeOrOutcome = IncomeOrOutcome.getById(it) }
                )
                Spacer(Modifier.height(24.dp))

                var titleError by remember { mutableStateOf(false) }
                TextField(modifier = Modifier.shake(deleteMode).clip(RoundedCornerShape(16.dp)),
                    isError = titleError,
                    value = expenseTitle,
                    onValueChange = {
                        if (titleError) titleError = false
                        if (it.length < 19) expenseTitle = it },
                    label = { Text(stringResource(Res.string.title)) },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    maxLines = 1,
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
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
                            selectedIcon = expenseIcon,
                            onClick = { expenseIcon = icon }
                        )
                    }
                }
                Text(stringResource(Res.string.labels),
                    modifier = Modifier.padding(start = 64.dp, top = 8.dp).fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                LabelSelection(
                    labels = labels,
                    selected = expenseLabels,
                    onSelect = { labelId ->
                        if (expenseLabels.any { it == labelId }) expenseLabels.remove(labelId)
                        else expenseLabels.add(labelId)
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
                                if (expenseTitle.isBlank()) titleError = true
                                else {
                                    saveExpense(
                                        AddEditExpense(
                                        id = expense?.id,
                                        incomeOrOutcome = expenseIncomeOrOutcome,
                                        title = expenseTitle,
                                        icon = expenseIcon,
                                        labels = expenseLabels)
                                    )
                                    onDismiss() } }
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = stringResource(Res.string.save))
                            Text(stringResource(Res.string.save),
                                modifier = Modifier.padding(horizontal = 8.dp),
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                    else expense?.let {
                        TextButton(onClick = { deleteExpense(expense.id); onDismiss() }) {
                            Text(stringResource(Res.string.delete_budget),
                                color = MaterialTheme.colorScheme.error)
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