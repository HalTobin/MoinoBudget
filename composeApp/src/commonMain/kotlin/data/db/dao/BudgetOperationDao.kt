package data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import data.db.table.BudgetOperation
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetOperationDao {

    @Upsert
    suspend fun upsert(budgetOperation: BudgetOperation): Long

    @Delete
    suspend fun delete(budgetOperation: BudgetOperation)

    @Query("DELETE FROM expenses WHERE expense_id = :expenseId")
    suspend fun deleteById(expenseId: Long)

    @Query("SELECT * FROM expenses WHERE expense_id = :id")
    suspend fun getById(id: Int): BudgetOperation

    @Query("SELECT * FROM expenses")
    fun getAll(): Flow<List<BudgetOperation>>

}