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
    @Query("SELECT * FROM expense WHERE expenseId = :expenseId")
    suspend fun getExpenseWithLabels(expenseId: Long): List<ExpenseWithLabels>

    @Transaction
    @Query("SELECT * FROM label WHERE labelId = :labelId")
    suspend fun getLabelWithExpenses(labelId: Long): List<LabelWithExpenses>
}