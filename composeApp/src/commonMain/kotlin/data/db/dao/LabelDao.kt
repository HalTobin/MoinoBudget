package data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import data.db.table.Label
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelDao {

    @Upsert
    suspend fun upsert(label: Label)

    @Upsert
    suspend fun upsertAll(labels: List<Label>)

    @Delete
    suspend fun delete(label: Label)

    @Query("SELECT * FROM labels WHERE label_id = :id")
    suspend fun getById(id: Int): Label

    @Query("SELECT * FROM labels")
    suspend fun getAll(): List<Label>

    @Query("SELECT * FROM labels")
    fun getAllFlow(): Flow<List<Label>>

}