package feature.dashboard.presentation

import presentation.data.ExpenseUI

data class DashboardState(
    val upcomingPayments: Float = 0f,
    val disposableIncomes: Float = 0f,
    val expenses: List<ExpenseUI> = emptyList(),
)
