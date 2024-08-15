package feature.dashboard.presentation.data

import presentation.data.BudgetStyle

data class AddEditBudget(
    val id: Int?,
    val orderIndex: Int?,
    val title: String,
    val style: BudgetStyle,
    val labels: List<Int>
)
