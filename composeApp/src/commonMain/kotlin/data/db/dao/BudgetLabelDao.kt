package data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import data.db.relation.BudgetWithLabels
import data.db.relation.ExpenseWithLabels
import data.db.relation.LabelWithExpenses
import data.db.table.Budget
import data.db.table.BudgetLabelCrossRef
import data.db.table.Label

@Dao
interface BudgetLabelDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(budget: Budget)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabel(label: Label)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBudgetLabelCrossRef(crossRef: BudgetLabelCrossRef)

    @Transaction
    @Query("SELECT * FROM expenses WHERE id = :expenseId")
    suspend fun getBudgetWithLabels(budgetId: Int): List<BudgetWithLabels>

    @Transaction
    @Query("SELECT * FROM labels WHERE id = :labelId")
    suspend fun getLabelWithBudget(labelId: Int): List<LabelWithExpenses>
}