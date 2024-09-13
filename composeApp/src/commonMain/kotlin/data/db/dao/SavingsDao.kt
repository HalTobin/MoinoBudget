package data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import data.db.table.Savings
import kotlinx.coroutines.flow.Flow


@Dao
interface SavingsDao {
    @Upsert
    suspend fun upsert(savings: Savings): Long

    @Query("UPDATE savings SET amount = :newAmount WHERE savings_id =:id")
    suspend fun updateAmount(id: Int, newAmount: Int)

    @Delete
    suspend fun delete(savings: Savings)

    @Query("DELETE FROM savings WHERE savings_id = :savingsId")
    suspend fun deleteById(savingsId: Long)

    @Query("SELECT * FROM savings")
    fun getAll(): Flow<List<Savings>>

    @Query("SELECT * FROM savings WHERE savings_id = :savingsId")
    suspend fun getById(savingsId: Int): Savings

    @Query("SELECT * FROM savings WHERE savings_id = :savingsId")
    fun getFlowById(savingsId: Int): Flow<Savings>
}