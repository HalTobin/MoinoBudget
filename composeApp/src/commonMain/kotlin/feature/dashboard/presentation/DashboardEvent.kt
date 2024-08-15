package feature.dashboard.presentation

import feature.dashboard.presentation.data.AddEditBudget
import presentation.data.LabelUI

sealed class DashboardEvent {
    data class UpsertLabel(val label: LabelUI): DashboardEvent()
    data class UpsertBudget(val budget: AddEditBudget): DashboardEvent()
    data class DeleteBudget(val budgetId: Int): DashboardEvent()
}