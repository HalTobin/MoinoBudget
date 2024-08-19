package feature.dashboard.presentation

import feature.dashboard.presentation.data.AddEditBudget
import feature.dashboard.presentation.data.AddEditExpense
import presentation.data.LabelUI

sealed class DashboardEvent {
    data class UpsertLabel(val label: LabelUI): DashboardEvent()
    data class UpsertBudget(val budget: AddEditBudget): DashboardEvent()
    data class DeleteBudget(val budgetId: Int): DashboardEvent()
    data class UpsertExpense(val expense: AddEditExpense): DashboardEvent()
    data class DeleteExpense(val expenseId: Int): DashboardEvent()
}