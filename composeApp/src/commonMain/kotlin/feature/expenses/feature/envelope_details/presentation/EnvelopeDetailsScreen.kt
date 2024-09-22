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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import data.repository.AppPreferences
import feature.expenses.feature.envelope_details.presentation.component.EnvelopeHeader
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.add_expense
import org.jetbrains.compose.resources.stringResource
import presentation.component.AddFloatingButton
import presentation.data.IncomeOrOutcome
import presentation.formatCurrency
import util.fullFormatDate

@Composable
fun EnvelopeDetailsScreen(
    state: EnvelopeDetailsState,
    preferences: AppPreferences,
    addEditEnvelope: (Int) -> Unit,
    addEditExpense: (Int?) -> Unit,
    goBack: () -> Unit
) = Scaffold(
    floatingActionButton = { AddFloatingButton(
        text = stringResource(Res.string.add_expense),
        onClick = { state.envelope?.let { addEditExpense(null) } }) }
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            EnvelopeHeader(
                envelope = state.envelope,
                preferences = preferences,
                goBack = goBack,
                edit = { state.envelope?.id?.let { addEditEnvelope(it) } }
            )
            LazyColumn(Modifier.fillMaxWidth().weight(1f)) {
                itemsIndexed(state.expenses) { index, expense ->
                    Column {
                        Row(modifier = Modifier
                            .clickable { addEditExpense(expense.id) }
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            expense.icon?.icon?.let { icon ->
                                Icon(icon,
                                    modifier = Modifier.size(32.dp),
                                    contentDescription = null)
                                Spacer(Modifier.width(16.dp))
                            }
                            Column(Modifier.weight(1f)) {
                                Text(expense.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold)
                                Text(fullFormatDate(expense.date, preferences),
                                    fontWeight = FontWeight.Light
                                )
                            }
                            Text("-${formatCurrency(expense.amount, preferences)}",
                                style = MaterialTheme.typography.titleLarge,
                                color = IncomeOrOutcome.Outcome.color,
                                fontWeight = FontWeight.Bold)
                            state.envelope?.max?.let { max ->
                                Text(" / ${((expense.amount / max.toFloat())*100).toInt()}%")
                            }
                        }
                        if (index != state.expenses.lastIndex) HorizontalDivider(Modifier.fillMaxWidth())
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }

    }

}