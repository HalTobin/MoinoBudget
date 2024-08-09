package data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import data.db.relation.FullBudget
import data.db.table.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Upsert
    suspend fun upsert(budget: Budget)

    @Delete
    suspend fun delete(budget: Budget)

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getById(id: Int): Budget

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getFullById(id: Int): FullBudget

    @Query("SELECT * FROM budgets")
    fun getAll(): Flow<Budget>

    @Query("SELECT * FROM budgets")
    fun getAllFull(): Flow<FullBudget>

}