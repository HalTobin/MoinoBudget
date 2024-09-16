package data.repository

import data.db.dao.BudgetOperationDao
import data.db.dao.BudgetOperationLabelDao
import data.db.table.BudgetOperationLabelCrossRef
import data.mapper.toExpenseEntity
import data.mapper.toExpenseUI
import feature.budgets.feature.add_edit_budgets.data.AddEditExpense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import feature.budgets.data.BudgetOperationUI

class ExpenseRepositoryImpl(
    private val budgetOperationDao: BudgetOperationDao,
    private val budgetOperationLabelDao: BudgetOperationLabelDao
): ExpenseRepository {

    override suspend fun getExpense(id: Int): BudgetOperationUI =
        budgetOperationLabelDao.getExpenseWithLabels(id).toExpenseUI()

    override suspend fun getExpensesFlow(): Flow<List<BudgetOperationUI>> =
        budgetOperationLabelDao.getAllExpensesWithLabelsFlow().map { it.map { expenseWithLabel -> expenseWithLabel.toExpenseUI() } }

    override suspend fun upsertExpense(expense: AddEditExpense) {
        expense.id?.let { expenseId ->
            budgetOperationLabelDao.getCrossRefsByExpenseId(expenseId)
                .filterNot { crossRef -> expense.labels.any { it == crossRef.labelId } }
                .forEach { crossRef -> budgetOperationLabelDao.deleteExpenseLabelCrossRef(crossRef) }
        }

        val upsertedId = budgetOperationDao.upsert(expense.toExpenseEntity())
        val expenseId = expense.id ?: upsertedId.toInt()

        budgetOperationLabelDao.insertExpenseLabelCrossRefs(
            expense.labels.map { BudgetOperationLabelCrossRef(
                expenseId = expenseId,
                labelId = it) }
        )
    }

    override suspend fun deleteExpense(expenseId: Int) =
        budgetOperationDao.deleteById(expenseId.toLong())

}

interface ExpenseRepository {
    suspend fun getExpense(id: Int): BudgetOperationUI
    suspend fun getExpensesFlow(): Flow<List<BudgetOperationUI>>
    suspend fun upsertExpense(expense: AddEditExpense)
    suspend fun deleteExpense(expenseId: Int)
}