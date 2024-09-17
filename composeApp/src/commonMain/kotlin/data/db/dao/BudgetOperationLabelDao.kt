package data.db.dao

import androidx.room.*
import data.db.relation.ExpenseWithLabels
import data.db.relation.LabelWithExpenses
import data.db.table.BudgetOperation
import data.db.table.BudgetOperationLabelCrossRef
import data.db.table.Label
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetOperationLabelDao {
    @Delete
    suspend fun deleteExpenseLabelCrossRef(crossRef: BudgetOperationLabelCrossRef)

    @Delete
    suspend fun deleteExpenseLabelCrossRefs(crossRefs: List<BudgetOperationLabelCrossRef>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(budgetOperation: BudgetOperation)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabel(label: Label)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpenseLabelCrossRef(crossRef: BudgetOperationLabelCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpenseLabelCrossRefs(crossRefs: List<BudgetOperationLabelCrossRef>)

    @Transaction
    @Query("SELECT * FROM budget_operations WHERE operation_id = :expenseId")
    suspend fun getExpenseWithLabels(expenseId: Int): ExpenseWithLabels

    @Transaction
    @Query("SELECT * FROM budget_operations")
    suspend fun getAllExpensesWithLabels(): List<ExpenseWithLabels>

    @Transaction
    @Query("SELECT * FROM budget_operations")
    fun getAllExpensesWithLabelsFlow(): Flow<List<ExpenseWithLabels>>

    @Transaction
    @Query("SELECT * FROM operation_label_crossref WHERE label_id IN (:labelIds)")
    suspend fun getExpensesWithLabelsLabelIds(labelIds: List<Int>): List<BudgetOperationLabelCrossRef>

    @Transaction
    @Query("SELECT * FROM operation_label_crossref WHERE label_id IN (:labelIds)")
    fun getExpensesWithLabelsLabelIdsFlow(labelIds: List<Int>): Flow<List<BudgetOperationLabelCrossRef>>

    @Transaction
    @Query("SELECT * FROM labels WHERE label_id = :labelId")
    suspend fun getLabelWithExpenses(labelId: Long): List<LabelWithExpenses>

    @Transaction
    @Query("SELECT * FROM operation_label_crossref WHERE operation_id = :expenseId")
    suspend fun getCrossRefsByExpenseId(expenseId: Int): List<BudgetOperationLabelCrossRef>
}