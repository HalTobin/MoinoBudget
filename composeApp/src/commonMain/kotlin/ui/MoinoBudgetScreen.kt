package ui

import kotlinx.serialization.Serializable

@Serializable
sealed class MoinoBudgetScreen(val route: String) {
    /** Mains **/
    @Serializable
    data object Main: MoinoBudgetScreen("main")
    @Serializable
    data object Settings: MoinoBudgetScreen("setting")

    /** Related To Savings **/
    @Serializable
    data class AddEditSavings(
        val defaultSavingsTypeId: Int,
        val savingsId: Int = -1
    ): MoinoBudgetScreen("add_edit_savings")
    @Serializable
    data class SavingsDetails(
        val savingsId: Int = -1
    ): MoinoBudgetScreen("savings_details")

    /** Related To Budgets **/
    @Serializable
    data class AddEditBudgetOperation(
        val labelIds: List<Int>,
        val expenseId: Int = -1
    ): MoinoBudgetScreen("add_edit_expense")

    /** Related To Expenses **/
    @Serializable
    data class EnvelopeDetails(
        val envelopeId: Int
    ): MoinoBudgetScreen("envelope_details")
    @Serializable
    data class EnvelopeHistory(
        val envelopeId: Int
    ): MoinoBudgetScreen("envelope_history")
    @Serializable
    data class AddEditEnvelope(
        val envelopeId: Int = -1
    ): MoinoBudgetScreen("add_edit_envelope")
    @Serializable
    data class AddEditExpense(
        val expenseId: Int = -1,
        val envelopeId: Int
    ): MoinoBudgetScreen("add_edit_expense")
}