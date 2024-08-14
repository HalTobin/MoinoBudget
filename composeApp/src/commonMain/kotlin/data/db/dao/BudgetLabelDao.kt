package data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import data.db.relation.BudgetWithLabels
import data.db.relation.ExpenseWithLabels
import data.db.relation.LabelWithExpenses
import data.db.table.Budget
import data.db.table.BudgetLabelCrossRef
import data.db.table.ExpenseLabelCrossRef
import data.db.table.Label

@Dao
interface BudgetLabelDao {
    @Delete
    suspend fun deleteBudgetLabelCrossRef(crossRef: BudgetLabelCrossRef)

    @Delete
    suspend fun deleteBudgetLabelCrossRefs(crossRefs: List<BudgetLabelCrossRef>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBudgetLabelCrossRef(crossRef: BudgetLabelCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBudgetLabelCrossRefs(crossRefs: List<BudgetLabelCrossRef>)

    @Transaction
    @Query("SELECT * FROM budgets WHERE budget_id = :budgetId")
    suspend fun getBudgetWithLabels(budgetId: Int): List<BudgetWithLabels>

    @Transaction
    @Query("SELECT * FROM budget_label_crossref WHERE budget_id = :budgetId")
    suspend fun getCrossRefsByBudgetId(budgetId: Int): List<BudgetLabelCrossRef>
}