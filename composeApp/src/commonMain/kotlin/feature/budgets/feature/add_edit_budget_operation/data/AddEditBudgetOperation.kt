package feature.budgets.feature.add_edit_budget_operation.data

import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.IncomeOrOutcome

data class AddEditBudgetOperation(
    val id: Int? = null,
    val incomeOrOutcome: IncomeOrOutcome = IncomeOrOutcome.Outcome,
    val title: String = "",
    val amount: Float = 0.0f,
    val frequency: ExpenseFrequency = ExpenseFrequency.Monthly,
    val monthOffset: Int = 0,
    val day: Int = 1,
    val icon: ExpenseIcon = ExpenseIcon.DefaultOutcome,
    val labels: List<Int> = emptyList()
)
