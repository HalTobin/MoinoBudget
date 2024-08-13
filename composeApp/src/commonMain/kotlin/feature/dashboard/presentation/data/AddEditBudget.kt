package feature.dashboard.presentation.data

import presentation.data.BudgetStyle

data class AddEditBudget(
    val id: Int?,
    val title: String,
    val style: BudgetStyle,
    val labels: List<Int>
)
