package feature.budgets.data

import kotlinx.datetime.LocalDate
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.IncomeOrOutcome
import presentation.data.MonthOption

data class BudgetOperationUI(
    val id: Int,
    val amount: Float,
    val type: IncomeOrOutcome,
    val title: String,
    val icon: ExpenseIcon,
    val frequency: ExpenseFrequency,
    val payed: Boolean,
    val dueIn: Int,
    val nextPayment: LocalDate,
    val monthOption: MonthOption?,
    val day: Int,
    val labels: List<LabelUI>
)