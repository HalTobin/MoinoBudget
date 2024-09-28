package data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import data.db.table.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Upsert
    suspend fun upsert(expense: Expense): Long

    @Delete
    suspend fun delete(expense: Expense)

    @Query("DELETE FROM expenses WHERE expense_id = :expenseId")
    suspend fun deleteById(expenseId: Long)

    @Query("SELECT * FROM expenses WHERE expense_id = :id")
    suspend fun getById(id: Int): Expense

    @Query("SELECT * FROM expenses")
    fun getAll(): Flow<List<Expense>>

    @Query("SELECT MIN(timestamp) AS oldest_timestamp FROM expenses WHERE envelope_id = :envelopeId")
    suspend fun getOldestTimeByEnvelopeId(envelopeId: Int): Long?

    @Query("SELECT MIN(timestamp) AS oldest_timestamp FROM expenses WHERE envelope_id = :envelopeId")
    fun getOldestTimeFlowByEnvelopeId(envelopeId: Int): Flow<Long?>

    @Query("SELECT * FROM expenses WHERE envelope_id = :envelopeId")
    suspend fun getByEnvelopeId(envelopeId: Int): List<Expense>

    @Query("SELECT * FROM expenses WHERE envelope_id = :envelopeId AND timestamp >= :startPeriod AND timestamp <= :endPeriod")
    suspend fun getByEnvelopeIdAndPeriod(envelopeId: Int, startPeriod: Long, endPeriod: Long): List<Expense>

    @Query("SELECT * FROM expenses WHERE envelope_id = :envelopeId")
    fun getFlowByEnvelopeId(envelopeId: Int): Flow<List<Expense>>
}