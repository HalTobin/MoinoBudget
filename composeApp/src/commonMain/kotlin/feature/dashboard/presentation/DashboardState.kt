package feature.dashboard.presentation

import presentation.data.BudgetUI

data class DashboardState(
    val budgets: List<BudgetUI> = emptyList(),
)
