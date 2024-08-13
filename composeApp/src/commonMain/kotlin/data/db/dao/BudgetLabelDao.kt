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
    suspend fun insertBudgetLabelCrossRef(crossRef: BudgetLabelCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBudgetLabelCrossRefs(crossRefs: List<BudgetLabelCrossRef>)

    @Transaction
    @Query("SELECT * FROM budgets WHERE budget_id = :budgetId")
    suspend fun getBudgetWithLabels(budgetId: Int): List<BudgetWithLabels>

    @Transaction
    @Query("SELECT * FROM labels WHERE label_id = :labelId")
    suspend fun getLabelWithBudget(labelId: Int): List<LabelWithExpenses>
}