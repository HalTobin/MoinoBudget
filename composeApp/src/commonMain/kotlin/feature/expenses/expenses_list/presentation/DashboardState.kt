package feature.expenses.expenses_list.presentation

import presentation.data.BudgetUI
import presentation.data.ExpenseUI
import presentation.data.LabelUI

data class DashboardState(
    val labels: List<LabelUI> = emptyList(),
    val expenses: List<ExpenseUI> = emptyList(),
    val budgets: List<BudgetUI> = emptyList(),
)
