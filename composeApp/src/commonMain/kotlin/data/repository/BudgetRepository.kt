package data.repository

import data.db.dao.BudgetDao
import data.db.dao.BudgetLabelDao
import data.db.dao.ExpenseDao
import data.db.dao.ExpenseLabelDao
import data.db.table.Budget
import data.db.table.BudgetLabelCrossRef
import data.mapper.toBudgetEntity
import data.mapper.toLabelUI
import feature.dashboard.presentation.data.AddEditBudget
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDateTime
import presentation.data.BudgetStyle
import presentation.data.BudgetUI
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.ExpenseUI
import presentation.data.IncomeOrOutcome
import util.toLocalDate
import kotlin.math.exp

class BudgetRepositoryImpl(
    private val budgetDao: BudgetDao,
    private val budgetLabelDao: BudgetLabelDao,
    private val expenseLabelDao: ExpenseLabelDao
): BudgetRepository {

    override suspend fun upsertBudget(budget: AddEditBudget) {
        val budgetId = budgetDao.upsert(budget.toBudgetEntity())
        budgetLabelDao.insertBudgetLabelCrossRefs(
            budget.labels.map { BudgetLabelCrossRef(
                budgetId = budgetId.toInt(),
                labelId = it) }
        )
    }

    override fun getBudgetsFlow(): Flow<List<BudgetUI>> {
        val currentTime = Clock.System.now()
        return budgetDao.getAllWithLabels().map { budgets ->
            budgets.map { budget ->
                BudgetUI(
                    id = budget.budget.id,
                    title = budget.budget.title,
                    style = BudgetStyle.list.find { it.id == budget.budget.bgId } ?: BudgetStyle.CitrusJuice,
                    labels = budget.labels.map { it.toLabelUI() },
                    expenses = expenseLabelDao.getExpensesWithLabelsLabelIds(budget.labels.map { it.id })
                        .distinctBy { it.expenseId }
                        .map { expenseLabelDao.getExpenseWithLabels(it.expenseId) }
                        .map { expense ->
                            val frequency = ExpenseFrequency.findById(expense.expense.frequency)
                            val currentDate = Clock.System.now().epochSeconds.toLocalDate()
                            val lastPayment = expense.expense.lastPayment.toLocalDate()
                            ExpenseUI(
                                id = expense.expense.id,
                                amount = expense.expense.amount,
                                type = IncomeOrOutcome.getByDbId(expense.expense.isIncome),
                                title = expense.expense.title,
                                icon = ExpenseIcon.findById(expense.expense.iconId),
                                frequency = frequency,
                                payed = false,
                                dueIn = 12,
                                nextPayment = LocalDate(lastPayment.year,
                                    lastPayment.monthNumber + (expense.expense.monthOffset ?: 0) + frequency.everyXMonth, expense.expense.day),
                                lastPayment = expense.expense.lastPayment.toLocalDate(),
                                labels = expense.labels.map { it.toLabelUI() }
                        ) }
                )
            }
        }
    }

}

interface BudgetRepository {
    suspend fun upsertBudget(label: AddEditBudget)
    fun getBudgetsFlow(): Flow<List<BudgetUI>>
}