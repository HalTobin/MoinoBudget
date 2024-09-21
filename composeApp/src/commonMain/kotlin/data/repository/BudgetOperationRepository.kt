package data.repository

import data.db.dao.BudgetOperationDao
import data.db.dao.BudgetOperationLabelDao
import data.db.table.BudgetOperationLabelCrossRef
import data.mapper.toExpenseEntity
import data.mapper.toExpenseUI
import feature.budgets.feature.add_edit_budget_operation.data.AddEditBudgetOperation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import feature.budgets.data.BudgetOperationUI

class BudgetOperationRepositoryImpl(
    private val budgetOperationDao: BudgetOperationDao,
    private val budgetOperationLabelDao: BudgetOperationLabelDao
): BudgetOperationRepository {

    override suspend fun getBudgetOperation(id: Int): BudgetOperationUI =
        budgetOperationLabelDao.getExpenseWithLabels(id).toExpenseUI()

    override suspend fun getBudgetOperationsFlow(): Flow<List<BudgetOperationUI>> =
        budgetOperationLabelDao.getAllExpensesWithLabelsFlow().map { it.map { expenseWithLabel -> expenseWithLabel.toExpenseUI() } }

    override suspend fun upsertBudgetOperation(operation: AddEditBudgetOperation) {
        operation.id?.let { expenseId ->
            budgetOperationLabelDao.getCrossRefsByExpenseId(expenseId)
                .filterNot { crossRef -> operation.labels.any { it == crossRef.labelId } }
                .forEach { crossRef -> budgetOperationLabelDao.deleteExpenseLabelCrossRef(crossRef) }
        }

        val upsertedId = budgetOperationDao.upsert(operation.toExpenseEntity())
        val operationId = operation.id ?: upsertedId.toInt()

        budgetOperationLabelDao.insertExpenseLabelCrossRefs(
            operation.labels.map { BudgetOperationLabelCrossRef(
                operationId = operationId,
                labelId = it) }
        )
    }

    override suspend fun deleteBudgetOperation(operationId: Int) =
        budgetOperationDao.deleteById(operationId.toLong())

}

interface BudgetOperationRepository {
    suspend fun getBudgetOperation(id: Int): BudgetOperationUI
    suspend fun getBudgetOperationsFlow(): Flow<List<BudgetOperationUI>>
    suspend fun upsertBudgetOperation(operation: AddEditBudgetOperation)
    suspend fun deleteBudgetOperation(operationId: Int)
}