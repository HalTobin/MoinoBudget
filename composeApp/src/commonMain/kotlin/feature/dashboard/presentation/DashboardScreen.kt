package feature.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moinobudget.composeapp.generated.resources.Res
import moinobudget.composeapp.generated.resources.due_in
import moinobudget.composeapp.generated.resources.incomes
import moinobudget.composeapp.generated.resources.outcomes
import moinobudget.composeapp.generated.resources.upcoming_payments
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import presentation.data.ExpenseUI
import ui.Screen
import ui.theme.Orange40
import ui.theme.Orange80
import ui.theme.OrangeGrey40
import ui.theme.OrangeGrey80

@Composable
fun DashboardScreen(
    state: DashboardState,
    onEvent: (DashboardEvent) -> Unit,
    goTo: (Screen) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        MonthlySummary()
        Spacer(modifier = Modifier.height(16.dp))
        QuickActions()
        Spacer(modifier = Modifier.height(16.dp))
        FinancialSummary(
            totalExpenses = state.upcomingPayments,
            disposableIncome = state.disposableIncomes
        )
        Spacer(modifier = Modifier.height(16.dp))
        UpcomingPayments(state.expenses)
    }
}

@Composable
fun MonthlySummary() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Monthly Summary", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Quick Actions", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun QuickActions() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = { /* TODO: Add New Expense */ }) {
            Text("Add New Expense")
        }
        Button(onClick = { /* TODO: Add New Label */ }) {
            Text("Add New Label")
        }
    }
}

@Composable
fun FinancialSummary(totalExpenses: Float, disposableIncome: Float) {
    Row {
        Column(modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$$totalExpenses",
                color = Color.Green,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displaySmall)
            Text(stringResource(Res.string.incomes).uppercase(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        Column(modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$$disposableIncome",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displaySmall)
            Text(stringResource(Res.string.outcomes).uppercase(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        // Year / Month modes
    }
}

@Composable
fun UpcomingPayments(dueExpenses: List<ExpenseUI>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(Res.string.upcoming_payments), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(dueExpenses.sortedBy { it.dueIn }) { dueExpense ->
                DueExpenseItem(dueExpense)
            }
        }
    }
}

@Composable
fun DueExpenseItem(dueExpense: ExpenseUI) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(modifier = Modifier.padding(horizontal = 8.dp).size(32.dp),
            imageVector = dueExpense.icon.icon, contentDescription = null)

        Column(Modifier.padding(start = 4.dp)) {
            Row {
                Text(dueExpense.title, modifier = Modifier.weight(1f), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("$${dueExpense.amount}",
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                fun getColorFromDueInDays(dueIn: Int): Color = if (dueIn < 7) Color.Red else if (dueIn < 30) Orange80 else Color.Green
                val dueInText = pluralStringResource(Res.plurals.due_in, dueExpense.dueIn, dueExpense.dueIn)
                val numberText = dueExpense.dueIn.toString()
                val styledText = buildAnnotatedString {
                    val splitText = dueInText.split(numberText)
                    append(splitText[0])
                    withStyle(style = SpanStyle(color = getColorFromDueInDays(dueExpense.dueIn),
                        fontWeight = FontWeight.SemiBold)) { append(numberText) }
                    append(splitText[1])
                }
                Text(styledText, fontSize = 14.sp, modifier = Modifier.weight(1f))
                dueExpense.labels.forEach {
                    Box(Modifier.padding(horizontal = 4.dp).size(20.dp).clip(CircleShape).background(it))
                }
            }
        }
    }
}