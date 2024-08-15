package data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import data.db.relation.BudgetWithLabels
import data.db.table.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Upsert
    suspend fun upsert(budget: Budget): Long

    @Delete
    suspend fun delete(budget: Budget)

    @Query("SELECT * FROM budgets WHERE budget_id = :id")
    suspend fun getById(id: Int): Budget

    @Query("SELECT * FROM budgets")
    fun getAll(): Flow<List<Budget>>

    @Transaction
    @Query("SELECT * FROM budgets")
    suspend fun getAllWithLabels(): List<BudgetWithLabels>

    @Transaction
    @Query("SELECT * FROM budgets")
    fun getAllWithLabelsFlow(): Flow<List<BudgetWithLabels>>
}