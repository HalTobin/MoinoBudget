package data.mapper

import data.db.relation.ExpenseWithLabels
import data.db.table.Expense
import feature.add_edit_expense.data.AddEditExpense
import kotlinx.datetime.Clock
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.ExpenseUI
import presentation.data.IncomeOrOutcome
import presentation.data.MonthOption
import util.calculateNextPayment
import util.toLocalDate

fun AddEditExpense.toExpenseEntity(): Expense =
    Expense(
        id = this.id ?: 0,
        title = this.title,
        amount = this.amount,
        iconId = this.icon.id,
        isIncome = this.incomeOrOutcome.dbId,
        frequency = this.frequency.id,
        monthOffset = this.monthOffset,
        day = this.day,
    )

fun ExpenseWithLabels.toExpenseUI(): ExpenseUI {
    val frequency = ExpenseFrequency.findById(this.expense.frequency)
    val currentDate = Clock.System.now().epochSeconds.toLocalDate()
    val nextPayment = calculateNextPayment(this.expense)
    val dueIn = nextPayment.compareTo(currentDate)
    return ExpenseUI(
        id = this.expense.id,
        amount = this.expense.amount,
        type = IncomeOrOutcome.getByDbId(this.expense.isIncome),
        title = this.expense.title,
        icon = ExpenseIcon.findById(this.expense.iconId),
        frequency = frequency,
        payed = false,
        dueIn = dueIn,
        nextPayment = nextPayment,
        day = this.expense.day,
        monthOption = MonthOption.findByOffsetAndFrequency(frequency, this.expense.monthOffset),
        labels = this.labels.map { it.toLabelUI() }
    )
}