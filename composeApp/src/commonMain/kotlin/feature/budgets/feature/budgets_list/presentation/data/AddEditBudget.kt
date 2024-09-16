package feature.budgets.feature.budgets_list.presentation.data

import feature.budgets.data.BudgetStyle

data class AddEditBudget(
    val id: Int?,
    val orderIndex: Int?,
    val title: String,
    val style: BudgetStyle,
    val labels: List<Int>
)
