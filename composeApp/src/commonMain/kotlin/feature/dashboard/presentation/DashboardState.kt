package feature.dashboard.presentation

import presentation.data.ExpenseUI

// NOTE: Every Pair represent an <Monthly, Annual> value
data class DashboardState(
    val rawIncomes: Pair<Float, Float> = Pair(0f, 0f),
    val upcomingPayments: Pair<Float, Float> = Pair(0f, 0f),
    val disposableIncomes: Pair<Float, Float> = Pair(0f, 0f),
    val expenses: List<ExpenseUI> = emptyList(),
)
