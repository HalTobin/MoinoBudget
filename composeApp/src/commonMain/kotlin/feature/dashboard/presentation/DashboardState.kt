package feature.dashboard.presentation

import presentation.data.ExpenseUI

data class DashboardState(
    val rawIncomesMonth: Float = 0f,
    val rawIncomesYear: Float = 0f,

    val upcomingPaymentsMonthly: Float = 0f,
    val upcomingPaymentsAnnual: Float = 0f,

    val disposableIncomesMonthly: Float = 0f,
    val disposableIncomesAnnual: Float = 0f,
    val expenses: List<ExpenseUI> = emptyList(),
)
