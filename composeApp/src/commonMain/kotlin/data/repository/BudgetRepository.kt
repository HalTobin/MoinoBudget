package data.repository

import data.db.dao.BudgetDao
import data.db.dao.BudgetLabelDao
import data.db.dao.ExpenseDao
import data.db.dao.ExpenseLabelDao
import data.db.relation.BudgetWithLabels
import data.db.relation.ExpenseWithLabels
import data.db.table.Budget
import data.db.table.BudgetLabelCrossRef
import data.mapper.toBudgetEntity
import data.mapper.toExpenseUI
import data.mapper.toLabelUI
import feature.dashboard.data.MonthYearPair
import feature.dashboard.presentation.data.AddEditBudget
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
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
import presentation.data.MonthOption
import util.calculateNextPayment
import util.toLocalDate
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.exp

class BudgetRepositoryImpl(
    private val budgetDao: BudgetDao,
    private val budgetLabelDao: BudgetLabelDao,
    private val expenseLabelDao: ExpenseLabelDao,
): BudgetRepository {

    override suspend fun upsertBudget(budget: AddEditBudget): Int {
        budget.id?.let { budgetId ->
            budgetLabelDao.getCrossRefsByBudgetId(budgetId)
                .filterNot { crossRef -> budget.labels.any { it == crossRef.labelId } }
                .forEach { crossRef -> budgetLabelDao.deleteBudgetLabelCrossRef(crossRef) }
        }

        val upsertedId = budgetDao.upsert(budget.toBudgetEntity(budgetDao.getMaxOrderIndex()+1))
        val budgetId = budget.id ?: upsertedId.toInt()

        budgetLabelDao.insertBudgetLabelCrossRefs(
            budget.labels.map { BudgetLabelCrossRef(
                budgetId = budgetId,
                labelId = it) }
        )

        return budgetId
    }

    @Throws(NeedOneBudget::class, CancellationException::class)
    override suspend fun deleteBudget(budgetId: Int) {
        if (budgetDao.count() > 1) budgetDao.deleteById(budgetId.toLong())
        else throw NeedOneBudget()
    }

    override suspend fun getBudgets(): List<BudgetUI> =
        budgetDao.getAllWithLabels().mapToBudgetUI(expenseLabelDao.getAllExpensesWithLabels())

    override suspend fun getBudgetsFlow(): Flow<List<BudgetUI>> =
        combine(
            budgetDao.getAllWithLabelsFlow(), // Flow from budgetDao
            expenseLabelDao.getAllExpensesWithLabelsFlow() // Flow from expenseLabelDao
        ) { budgetsWithLabels, expensesWithLabels ->
            budgetsWithLabels.mapToBudgetUI(expensesWithLabels)
        }

    private fun List<BudgetWithLabels>.mapToBudgetUI(expensesWithLabels: List<ExpenseWithLabels>) = this.map { budget ->
        val expenses = expensesWithLabels
            //expenseLabelDao.getExpensesWithLabelsLabelIds(budget.labels.map { it.id })
            .filter { it.labels.any { label -> budget.labels.any { budgetLabel -> budgetLabel.id == label.id } } }
            .distinctBy { it.expense.id }
            //.map { expenseLabelDao.getExpenseWithLabels(it.expense.id) }
            .map { it.toExpenseUI() }

        val yearIncomes = expenses
            .filter { it.type == IncomeOrOutcome.Income }
            .sumOf { it.amount * it.frequency.multiplier.toDouble() }
        val yearOutcomes = expenses
            .filter { it.type == IncomeOrOutcome.Outcome }
            .sumOf { it.amount * it.frequency.multiplier.toDouble() }
        val monthPayments = expenses
            .filter { it.type == IncomeOrOutcome.Outcome
                && it.frequency == ExpenseFrequency.Monthly }
            .sumOf { it.amount.toDouble() }
        val toPutAside = expenses
            .filter { it.type == IncomeOrOutcome.Outcome
                && it.frequency != ExpenseFrequency.Monthly }
            .sumOf { it.amount * it.frequency.multiplier.toDouble() }

        BudgetUI(
            id = budget.budget.id,
            orderIndex = budget.budget.orderIndex,
            title = budget.budget.title,
            style = BudgetStyle.list.find { it.id == budget.budget.bgId }
                ?: BudgetStyle.CitrusJuice,
            labels = budget.labels.map { it.toLabelUI() },
            expenses = expenses,
            rawIncomes = MonthYearPair(annual = yearIncomes),
            toPutAside = MonthYearPair(annual = toPutAside),
            monthPayments = MonthYearPair(annual = monthPayments * 12),
            disposableIncomes = MonthYearPair(annual = (yearIncomes - yearOutcomes)),
            upcomingPayments = MonthYearPair(annual = yearOutcomes)
        )
    }

}

interface BudgetRepository {
    suspend fun upsertBudget(budget: AddEditBudget): Int
    suspend fun getBudgets(): List<BudgetUI>
    @Throws(NeedOneBudget::class, CancellationException::class)
    suspend fun deleteBudget(budgetId: Int)
    suspend fun getBudgetsFlow(): Flow<List<BudgetUI>>
}

class NeedOneBudget: Exception("You should at least have one budget!")