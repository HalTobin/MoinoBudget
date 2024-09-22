package data.repository

import data.db.dao.ExpenseDao
import data.mapper.toExpenseEntity
import data.mapper.toExpenseUI
import feature.expenses.data.ExpenseUI
import feature.expenses.feature.add_edit_expense.data.AddEditExpense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepositoryImpl(
    private val dao: ExpenseDao
): ExpenseRepository {

    override suspend fun getExpenseById(expenseId: Int): ExpenseUI =
        dao.getById(expenseId).toExpenseUI()

    override suspend fun getExpensesFlowByEnvelopeId(envelopeId: Int): Flow<List<ExpenseUI>> =
        dao.getFlowByEnvelopeId(envelopeId).map { it.map {
            expense -> expense.toExpenseUI() } }

    override suspend fun upsertExpense(addEditExpense: AddEditExpense): Long =
        dao.upsert(addEditExpense.toExpenseEntity())

    override suspend fun deleteExpense(expenseId: Int) =
        dao.deleteById(expenseId.toLong())

}

interface ExpenseRepository {
    suspend fun getExpenseById(expenseId: Int): ExpenseUI
    suspend fun getExpensesFlowByEnvelopeId(envelopeId: Int): Flow<List<ExpenseUI>>
    suspend fun upsertExpense(addEditExpense: AddEditExpense): Long
    suspend fun deleteExpense(expenseId: Int)
}