package feature.add_edit_expense.presentation

import feature.add_edit_expense.data.AddEditExpense
import presentation.data.BudgetUI
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.ExpenseUI
import presentation.data.IncomeOrOutcome
import presentation.data.LabelUI
import presentation.data.MonthOption

data class AddEditExpenseState(
    val expenseId: Int? = null,
    val expenseIncomeOrOutcome: IncomeOrOutcome = IncomeOrOutcome.Outcome,
    val expenseTitle: String = "",
    val expenseAmount: String = "",
    val expenseDay: Int = 1,
    val expenseMonth: MonthOption? = null,
    val expenseFrequency: ExpenseFrequency = ExpenseFrequency.Monthly,
    val expenseIcon: ExpenseIcon = ExpenseIcon.DefaultOutcome,
    val expenseLabels: List<Int> = emptyList(),

    val labels: List<LabelUI> = emptyList()
) {

    fun generateAddEditExpense(): AddEditExpense? {
        expenseAmount.toFloatOrNull()?.let { amount ->
            expenseMonth?.offset?.let { monthOffset ->
                return AddEditExpense(
                    id = expenseId,
                    incomeOrOutcome = expenseIncomeOrOutcome,
                    title = expenseTitle,
                    amount = amount,
                    frequency = expenseFrequency,
                    monthOffset = monthOffset,
                    day = expenseDay,
                    icon = expenseIcon,
                    labels = expenseLabels
                )
            }
            return null
        }
        return null
    }

    companion object {

        fun generateStateFromExpenseUI(
            labels: List<LabelUI>,
            expense: ExpenseUI?
        ) = AddEditExpenseState(
            expenseId = expense?.id,
            expenseIncomeOrOutcome = expense?.type ?: IncomeOrOutcome.Outcome,
            expenseTitle = expense?.title ?: "",
            expenseAmount = (expense?.amount ?: "").toString(),
            expenseDay = expense?.day ?: 1,
            expenseMonth = expense?.monthOption,
            expenseFrequency = expense?.frequency ?: ExpenseFrequency.Monthly,
            expenseIcon = expense?.icon ?: ExpenseIcon.DefaultOutcome,
            expenseLabels = expense?.labels?.map { it.id } ?: emptyList(),
            labels = labels
        )

    }

}