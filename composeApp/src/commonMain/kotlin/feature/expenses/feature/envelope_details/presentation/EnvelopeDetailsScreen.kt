package feature.expenses.feature.envelope_details.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.repository.AppPreferences
import feature.expenses.data.ExpenseUI
import feature.expenses.feature.envelope_details.presentation.component.EnvelopeHeader
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.add_expense
import moinobudget.composeapp.generated.resources.edit_envelope
import moinobudget.composeapp.generated.resources.go_back
import moinobudget.composeapp.generated.resources.open_history_description
import org.jetbrains.compose.resources.stringResource
import presentation.component.AddFloatingButton
import presentation.data.IncomeOrOutcome
import presentation.formatCurrency
import util.fullFormatDate
import util.toHue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnvelopeDetailsScreen(
    state: EnvelopeDetailsState,
    preferences: AppPreferences,
    addEditEnvelope: (Int) -> Unit,
    addEditExpense: (Int?) -> Unit,
    openHistory: () -> Unit,
    goBack: () -> Unit
) {
    val color = state.envelope?.color?.let {
        Color.hsl(hue = it.toHue(), lightness = 0.14f, saturation = 1f)
    } ?: MaterialTheme.colorScheme.onPrimary
    val contentColor = state.envelope?.color ?: MaterialTheme.colorScheme.primary

    Scaffold(
        floatingActionButton = { AddFloatingButton(
            text = stringResource(Res.string.add_expense),
            onClick = { state.envelope?.let { addEditExpense(null) } }) },
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = color
                ),
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.go_back)) } },
                actions = {
                    IconButton(onClick = { state.envelope?.let { openHistory() } }) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = stringResource(Res.string.open_history_description))
                    }
                    IconButton(onClick = { state.envelope?.let { (addEditEnvelope(it.id)) } }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = stringResource(Res.string.edit_envelope))
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            Column(Modifier.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally) {
                EnvelopeHeader(
                    envelope = state.envelope,
                    preferences = preferences,
                    goBack = goBack,
                    edit = { state.envelope?.id?.let { addEditEnvelope(it) } },
                    goToHistory = { openHistory() }
                )
                LazyColumn(Modifier.fillMaxWidth().weight(1f)) {
                    itemsIndexed(state.expenses) { index, expense ->
                        Column {
                            ExpenseItem(
                                expense = expense,
                                max = state.envelope?.max,
                                preferences = preferences,
                                edit = { addEditExpense(expense.id) }
                            )
                            if (index != state.expenses.lastIndex) HorizontalDivider(Modifier.fillMaxWidth())
                        }

                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }

        }

    }
}

@Composable
fun ExpenseItem(
    expense: ExpenseUI,
    max: Int?,
    preferences: AppPreferences,
    edit: () -> Unit,
) {
    Row(modifier = Modifier
        .clickable { edit() }
        .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        expense.icon?.icon?.let { icon ->
            Icon(icon,
                modifier = Modifier.size(28.dp),
                contentDescription = null)
            Spacer(Modifier.width(16.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(expense.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold)
            Text(fullFormatDate(expense.date, preferences),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Light
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("-${formatCurrency(expense.amount, preferences)}",
                style = MaterialTheme.typography.titleSmall,
                fontSize = 20.sp,
                color = IncomeOrOutcome.Outcome.color,
                fontWeight = FontWeight.Bold)
            max?.let { Text("${((expense.amount / it.toFloat())*100).toInt()}%") }
        }
    }
}