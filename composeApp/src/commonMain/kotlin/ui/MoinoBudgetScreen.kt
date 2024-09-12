package ui

import kotlinx.serialization.Serializable

@Serializable
sealed class MoinoBudgetScreen(val route: String) {
    @Serializable
    data object Main: MoinoBudgetScreen("main")
    @Serializable
    data class AddEditExpense(
        val labelIds: List<Int>,
        val expenseId: Int = -1
    ): MoinoBudgetScreen("add_edit_expense")
    @Serializable
    data class AddEditSavings(
        val defaultSavingsTypeId: Int,
        val savingsId: Int = -1
    ): MoinoBudgetScreen("add_edit_savings")
    @Serializable
    data object Settings: MoinoBudgetScreen("setting")
}