package feature.budgets.feature.budgets_list.presentation

import feature.budgets.feature.budgets_list.presentation.data.AddEditBudget
import feature.budgets.data.LabelUI

sealed class DashboardEvent {
    data class UpsertLabel(val label: LabelUI): DashboardEvent()
    data class UpsertBudget(val budget: AddEditBudget): DashboardEvent()
    data class DeleteBudget(val budgetId: Int): DashboardEvent()
}