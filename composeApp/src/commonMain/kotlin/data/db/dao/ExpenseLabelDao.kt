package data.db.dao

import androidx.room.*
import data.db.relation.ExpenseWithLabels
import data.db.relation.LabelWithExpenses
import data.db.table.Expense
import data.db.table.ExpenseLabelCrossRef
import data.db.table.Label

@Dao
interface ExpenseLabelDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(expense: Expense)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabel(label: Label)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpenseLabelCrossRef(crossRef: ExpenseLabelCrossRef)

    @Transaction
    @Query("SELECT * FROM expenses WHERE expense_id = :expenseId")
    suspend fun getExpenseWithLabels(expenseId: Int): ExpenseWithLabels

    @Transaction
    @Query("SELECT * FROM expense_label_crossref WHERE label_id IN (:labelIds)")
    suspend fun getExpensesWithLabelsLabelIds(labelIds: List<Int>): List<ExpenseLabelCrossRef>

    @Transaction
    @Query("SELECT * FROM labels WHERE label_id = :labelId")
    suspend fun getLabelWithExpenses(labelId: Long): List<LabelWithExpenses>
}