package data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import data.db.table.Envelope
import kotlinx.coroutines.flow.Flow

@Dao
interface EnvelopeDao {
    @Upsert
    suspend fun upsert(envelope: Envelope): Long

    @Delete
    suspend fun delete(envelope: Envelope)

    @Query("DELETE FROM envelopes WHERE envelope_id = :envelopeId")
    suspend fun deleteById(envelopeId: Long)

    @Query("SELECT * FROM envelopes")
    fun getAll(): Flow<List<Envelope>>

    @Query("SELECT * FROM envelopes WHERE envelope_id = :envelopeId")
    suspend fun getBydId(envelopeId: Int): Envelope?

    @Query("SELECT * FROM envelopes WHERE envelope_id = :envelopeId")
    fun getFlowById(envelopeId: Int): Flow<Envelope?>
}