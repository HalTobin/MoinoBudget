package data.repository

import data.db.dao.ExpenseDao
import data.db.dao.ExpenseLabelDao
import data.db.table.ExpenseLabelCrossRef
import data.mapper.toExpenseEntity
import data.mapper.toExpenseUI
import feature.add_edit_expense.data.AddEditExpense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import presentation.data.ExpenseUI
import presentation.data.IncomeOrOutcome
import kotlin.math.exp

class ExpenseRepositoryImpl(
    private val expenseDao: ExpenseDao,
    private val expenseLabelDao: ExpenseLabelDao
): ExpenseRepository {

    override suspend fun getExpense(id: Int): ExpenseUI =
        expenseLabelDao.getExpenseWithLabels(id).toExpenseUI()

    override suspend fun getExpensesFlow(): Flow<List<ExpenseUI>> =
        expenseLabelDao.getAllExpensesWithLabelsFlow().map { it.map { expenseWithLabel -> expenseWithLabel.toExpenseUI() } }

    override suspend fun upsertExpense(expense: AddEditExpense) {
        expense.id?.let { expenseId ->
            expenseLabelDao.getCrossRefsByExpenseId(expenseId)
                .filterNot { crossRef -> expense.labels.any { it == crossRef.labelId } }
                .forEach { crossRef -> expenseLabelDao.deleteExpenseLabelCrossRef(crossRef) }
        }

        val upsertedId = expenseDao.upsert(expense.toExpenseEntity())
        val expenseId = expense.id ?: upsertedId.toInt()

        expenseLabelDao.insertExpenseLabelCrossRefs(
            expense.labels.map { ExpenseLabelCrossRef(
                expenseId = expenseId,
                labelId = it) }
        )
    }

    override suspend fun deleteExpense(expenseId: Int) {
        TODO("Not yet implemented")
    }

}

interface ExpenseRepository {
    suspend fun getExpense(id: Int): ExpenseUI
    suspend fun getExpensesFlow(): Flow<List<ExpenseUI>>
    suspend fun upsertExpense(expense: AddEditExpense)
    suspend fun deleteExpense(expenseId: Int)
}