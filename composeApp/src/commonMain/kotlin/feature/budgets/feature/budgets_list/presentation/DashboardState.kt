package feature.budgets.feature.budgets_list.presentation

import feature.budgets.data.BudgetUI
import feature.budgets.data.BudgetOperationUI
import feature.budgets.data.LabelUI

data class DashboardState(
    val labels: List<LabelUI> = emptyList(),
    val expenses: List<BudgetOperationUI> = emptyList(),
    val budgets: List<BudgetUI> = emptyList(),
)
