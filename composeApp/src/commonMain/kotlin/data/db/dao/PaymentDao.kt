package data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import data.db.table.Label
import data.db.table.Expense
import data.db.table.Payment
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {

    @Upsert
    suspend fun upsert(payment: Payment)

    @Delete
    suspend fun delete(payment: Payment)

    @Query("SELECT * FROM payments WHERE id = :id")
    suspend fun getById(id: Int): Payment

    @Query("SELECT * FROM payments")
    fun getAll(): Flow<Payment>

}