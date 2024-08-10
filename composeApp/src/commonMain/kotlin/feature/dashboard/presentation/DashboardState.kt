package feature.dashboard.presentation

import presentation.data.BudgetUI
import presentation.data.LabelUI

data class DashboardState(
    val labels: List<LabelUI> = emptyList(),
    val budgets: List<BudgetUI> = emptyList(),
)
