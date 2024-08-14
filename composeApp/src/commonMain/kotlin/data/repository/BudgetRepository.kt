package data.repository

import data.db.dao.BudgetDao
import data.db.dao.BudgetLabelDao
import data.db.dao.ExpenseDao
import data.db.dao.ExpenseLabelDao
import data.db.relation.BudgetWithLabels
import data.db.table.Budget
import data.db.table.BudgetLabelCrossRef
import data.mapper.toBudgetEntity
import data.mapper.toLabelUI
import feature.dashboard.data.MonthYearPair
import feature.dashboard.presentation.data.AddEditBudget
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
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
    private val expenseLabelDao: ExpenseLabelDao,
): BudgetRepository {

    override suspend fun upsertBudget(budget: AddEditBudget) {
        budget.id?.let { budgetId ->
            budgetLabelDao.getCrossRefsByBudgetId(budgetId)
                .filterNot { crossRef -> budget.labels.any { it == crossRef.labelId } }
                .forEach { crossRef -> budgetLabelDao.deleteBudgetLabelCrossRef(crossRef) }
        }
        val budgetId = budget.id ?: budgetDao.upsert(budget.toBudgetEntity())
        budgetLabelDao.insertBudgetLabelCrossRefs(
            budget.labels.map { BudgetLabelCrossRef(
                budgetId = budgetId.toInt(),
                labelId = it) }
        )
    }

    override suspend fun getBudgets(): List<BudgetUI> = budgetDao.getAllWithLabels().mapToBudgetUI()

    override fun getBudgetsFlow(): Flow<List<BudgetUI>> = budgetDao.getAllWithLabelsFlow().map { it.mapToBudgetUI() }

    private suspend fun List<BudgetWithLabels>.mapToBudgetUI() = this.map { budget ->
        val expenses = expenseLabelDao.getExpensesWithLabelsLabelIds(budget.labels.map { it.id })
            .distinctBy { it.expenseId }
            .map { expenseLabelDao.getExpenseWithLabels(it.expenseId) }
            .map { expense ->
                val frequency = ExpenseFrequency.findById(expense.expense.frequency)
                val currentDate = Clock.System.now().epochSeconds.toLocalDate()
                val nextPayment = expense.expense.lastPayment.toLocalDate()
                nextPayment.plus(
                    DatePeriod(
                        years = 0,
                        months = frequency.everyXMonth
                    )
                )
                val dueIn = nextPayment.compareTo(currentDate)

                ExpenseUI(
                    id = expense.expense.id,
                    amount = expense.expense.amount,
                    type = IncomeOrOutcome.getByDbId(expense.expense.isIncome),
                    title = expense.expense.title,
                    icon = ExpenseIcon.findById(expense.expense.iconId),
                    frequency = frequency,
                    payed = false,
                    dueIn = dueIn,
                    nextPayment = nextPayment,
                    lastPayment = expense.expense.lastPayment.toLocalDate(),
                    labels = expense.labels.map { it.toLabelUI() }
                )
            }

        val yearIncomes = expenses
            .filter { it.type == IncomeOrOutcome.Income }
            .sumOf { it.amount * it.frequency.multiplier.toDouble() }
        val yearOutcomes = expenses
            .filter { it.type == IncomeOrOutcome.Outcome }
            .sumOf { it.amount * it.frequency.multiplier.toDouble() }
        val monthPayments = expenses
            .filter { it.frequency == ExpenseFrequency.Monthly }
            .sumOf { it.amount.toDouble() }
        val toPutAside = expenses
            .filter { it.frequency != ExpenseFrequency.Monthly }
            .sumOf { it.amount * it.frequency.multiplier.toDouble() }

        BudgetUI(
            id = budget.budget.id,
            title = budget.budget.title,
            style = BudgetStyle.list.find { it.id == budget.budget.bgId }
                ?: BudgetStyle.CitrusJuice,
            labels = budget.labels.map { it.toLabelUI() },
            expenses = expenses,
            rawIncomes = MonthYearPair(annual = yearIncomes / 2),
            toPutAside = MonthYearPair(annual = toPutAside / 2),
            monthPayments = MonthYearPair(annual = monthPayments / 2),
            disposableIncomes = MonthYearPair(annual = (yearIncomes - yearOutcomes) / 2),
            upcomingPayments = MonthYearPair(annual = yearOutcomes / 2)
        )
    }

}

interface BudgetRepository {
    suspend fun upsertBudget(budget: AddEditBudget)
    suspend fun getBudgets(): List<BudgetUI>
    fun getBudgetsFlow(): Flow<List<BudgetUI>>
}