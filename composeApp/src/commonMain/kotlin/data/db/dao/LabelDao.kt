package data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import data.db.table.Label
import data.db.table.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelDao {

    @Upsert
    suspend fun upsert(label: Label)

    @Delete
    suspend fun delete(label: Label)

    @Query("SELECT * FROM labels WHERE id = :id")
    suspend fun getById(id: Int): Expense

    @Query("SELECT * FROM labels")
    fun getAll(): Flow<Expense>

}