package data.mapper

import data.db.relation.ExpenseWithLabels
import data.db.table.BudgetOperation
import feature.budgets.feature.add_edit_budget_operation.data.AddEditBudgetOperation
import kotlinx.datetime.Clock
import kotlinx.datetime.daysUntil
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import feature.budgets.data.BudgetOperationUI
import presentation.data.IncomeOrOutcome
import presentation.data.MonthOption
import util.calculateNextPayment
import util.toLocalDate

fun AddEditBudgetOperation.toExpenseEntity(): BudgetOperation =
    BudgetOperation(
        id = this.id ?: 0,
        title = this.title,
        amount = this.amount,
        iconId = this.icon.id,
        isIncome = this.incomeOrOutcome.dbId,
        frequency = this.frequency.id,
        monthOffset = this.monthOffset,
        day = this.day,
    )

fun ExpenseWithLabels.toExpenseUI(): BudgetOperationUI {
    val frequency = ExpenseFrequency.findById(this.budgetOperation.frequency)
    val currentDate = Clock.System.now().toEpochMilliseconds().toLocalDate()
    val nextPayment = calculateNextPayment(this.budgetOperation)
    val dueIn = currentDate.daysUntil(nextPayment)
    return BudgetOperationUI(
        id = this.budgetOperation.id,
        amount = this.budgetOperation.amount,
        type = IncomeOrOutcome.getByDbId(this.budgetOperation.isIncome),
        title = this.budgetOperation.title,
        icon = ExpenseIcon.findById(this.budgetOperation.iconId),
        frequency = frequency,
        payed = false,
        dueIn = dueIn,
        nextPayment = nextPayment,
        day = this.budgetOperation.day,
        monthOption = MonthOption.findByOffsetAndFrequency(frequency, this.budgetOperation.monthOffset),
        labels = this.labels.map { it.toLabelUI() }
    )
}