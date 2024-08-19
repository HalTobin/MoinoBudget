package data.repository

import data.db.dao.ExpenseDao
import data.db.dao.ExpenseLabelDao
import data.db.table.ExpenseLabelCrossRef
import data.mapper.toExpenseEntity
import feature.dashboard.presentation.data.AddEditExpense

class ExpenseRepositoryImpl(
    private val expenseDao: ExpenseDao,
    private val expenseLabelDao: ExpenseLabelDao
): ExpenseRepository {

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
    suspend fun upsertExpense(expense: AddEditExpense)
    suspend fun deleteExpense(expenseId: Int)
}