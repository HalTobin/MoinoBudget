package feature.dashboard.presentation

import presentation.data.BudgetUI
import presentation.data.ExpenseUI
import presentation.data.LabelUI

data class DashboardState(
    val labels: List<LabelUI> = emptyList(),
    val expenses: List<ExpenseUI> = emptyList(),
    val budgets: List<BudgetUI> = emptyList(),
)
