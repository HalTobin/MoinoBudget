package feature.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.dashboard.data.DueExpense
import ui.Screen

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
        UpcomingPayments(state.dueExpenses)
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
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Total Regular Expenses: $$totalExpenses", fontSize = 18.sp)
        Text("Total Disposable Income: $$disposableIncome", fontSize = 18.sp)
    }
}

@Composable
fun UpcomingPayments(dueExpenses: List<DueExpense>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Upcoming Payments", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(dueExpenses) { dueExpense ->
                DueExpenseItem(dueExpense)
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
fun DueExpenseItem(dueExpense: DueExpense) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(dueExpense.labelColor.copy(alpha = 0.1f))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(dueExpense.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("Due in ${dueExpense.dueIn} days", fontSize = 14.sp)
        }
        Text("$${dueExpense.amount}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}