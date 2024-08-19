package feature.dashboard.presentation.data

import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.IncomeOrOutcome

data class AddEditExpense(
    val id: Int?,
    val incomeOrOutcome: IncomeOrOutcome,
    val title: String,
    val amount: Float,
    val frequency: ExpenseFrequency,
    val monthOffset: Int = 0,
    val day: Int,
    val icon: ExpenseIcon,
    val labels: List<Int>,
    val lastPayment: Long
)
