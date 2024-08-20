package feature.add_edit_expense.presentation

import presentation.data.BudgetUI
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.IncomeOrOutcome
import presentation.data.LabelUI

data class AddEditExpenseState(
    val budgetUI: BudgetUI? = null,

    val expenseId: Int? = null,
    val expenseIncomeOrOutcome: IncomeOrOutcome = IncomeOrOutcome.Outcome,
    val expenseTitle: String = "",
    val expenseAmount: String = "",
    val expenseDay: Int = 1,
    val expenseMonth: Int? = null,
    val expenseFrequency: ExpenseFrequency = ExpenseFrequency.Monthly,
    val expenseIcon: ExpenseIcon = ExpenseIcon.DefaultOutcome,
    val expenseLabels: List<Int> = emptyList(),

    val labels: List<LabelUI> = emptyList()
)