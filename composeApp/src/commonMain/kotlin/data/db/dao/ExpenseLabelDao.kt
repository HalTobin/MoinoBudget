package data.db.dao

import androidx.room.*
import data.db.relation.ExpenseWithLabels
import data.db.relation.LabelWithExpenses
import data.db.table.Expense
import data.db.table.ExpenseLabelCrossRef
import data.db.table.Label

@Dao
interface ExpenseLabelDao {
    @Delete
    suspend fun deleteExpenseLabelCrossRef(crossRef: ExpenseLabelCrossRef)

    @Delete
    suspend fun deleteExpenseLabelCrossRefs(crossRefs: List<ExpenseLabelCrossRef>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(expense: Expense)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabel(label: Label)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpenseLabelCrossRef(crossRef: ExpenseLabelCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpenseLabelCrossRefs(crossRefs: List<ExpenseLabelCrossRef>)

    @Transaction
    @Query("SELECT * FROM expenses WHERE expense_id = :expenseId")
    suspend fun getExpenseWithLabels(expenseId: Int): ExpenseWithLabels

    @Transaction
    @Query("SELECT * FROM expense_label_crossref WHERE label_id IN (:labelIds)")
    suspend fun getExpensesWithLabelsLabelIds(labelIds: List<Int>): List<ExpenseLabelCrossRef>

    @Transaction
    @Query("SELECT * FROM labels WHERE label_id = :labelId")
    suspend fun getLabelWithExpenses(labelId: Long): List<LabelWithExpenses>

    @Transaction
    @Query("SELECT * FROM expense_label_crossref WHERE expense_id = :expenseId")
    suspend fun getCrossRefsByExpenseId(expenseId: Int): List<ExpenseLabelCrossRef>
}