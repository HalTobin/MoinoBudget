package feature.budgets.feature.add_edit_budget_operation.presentation

import feature.budgets.feature.add_edit_budget_operation.data.AddEditBudgetOperation
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import feature.budgets.data.BudgetOperationUI
import presentation.data.IncomeOrOutcome
import feature.budgets.data.LabelUI
import presentation.data.MonthOption

data class AddEditBudgetOperationState(
    val budgetOperationId: Int? = null,
    val incomeOrOutcome: IncomeOrOutcome = IncomeOrOutcome.Outcome,
    val title: String = "",
    val amount: String = "",
    val day: Int = 1,
    val month: MonthOption? = null,
    val frequency: ExpenseFrequency = ExpenseFrequency.Monthly,
    val icon: ExpenseIcon = ExpenseIcon.DefaultOutcome,
    val selectedLabels: List<Int> = emptyList(),

    val availableLabels: List<LabelUI> = emptyList()
) {

    fun generateAddEditExpense(): AddEditBudgetOperation? =
        amount.toFloatOrNull()?.let { amountAsFloat ->
            AddEditBudgetOperation(
                id = budgetOperationId,
                incomeOrOutcome = incomeOrOutcome,
                title = title,
                amount = amountAsFloat,
                frequency = frequency,
                monthOffset = month?.offset ?: 0,
                day = day,
                icon = icon,
                labels = selectedLabels
            )
        }

    companion object {
        fun generateStateFromExpenseUI(
            labels: List<LabelUI>,
            expense: BudgetOperationUI?
        ) = AddEditBudgetOperationState(
            budgetOperationId = expense?.id,
            incomeOrOutcome = expense?.type ?: IncomeOrOutcome.Outcome,
            title = expense?.title ?: "",
            amount = (expense?.amount ?: "").toString(),
            day = expense?.day ?: 1,
            month = expense?.monthOption,
            frequency = expense?.frequency ?: ExpenseFrequency.Monthly,
            icon = expense?.icon ?: ExpenseIcon.DefaultOutcome,
            selectedLabels = expense?.labels?.map { it.id } ?: emptyList(),
            availableLabels = labels
        )
    }

}