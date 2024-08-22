package ui

import kotlinx.serialization.Serializable

@Serializable
sealed class MoinoBudgetScreen(val route: String) {
    @Serializable
    data object Dashboard: MoinoBudgetScreen("dashboard")
    @Serializable
    data class AddEditExpense(
        val styleId: Int,
        val labelIds: List<Int>,
        val expenseId: Int = -1
    ): MoinoBudgetScreen("add_edit_expense")
    @Serializable
    data object Settings: MoinoBudgetScreen("setting")
}