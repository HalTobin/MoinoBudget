package data.repository

import data.db.dao.EnvelopeDao
import data.db.dao.ExpenseDao
import data.mapper.toEnvelopeEntity
import feature.expenses.data.EnvelopeUI
import feature.expenses.feature.add_edit_envelope.data.AddEditEnvelope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EnvelopeRepositoryImpl(
    private val envelopeDao: EnvelopeDao,
    private val expenseDao: ExpenseDao
): EnvelopeRepository {
    override suspend fun getEnvelopesFlow(): Flow<List<EnvelopeUI>> =
        envelopeDao.getAll().map { TODO() }

    override suspend fun getEnvelopeById(envelopeId: Int): EnvelopeUI =
        envelopeDao.getBydId(envelopeId).let { TODO() }

    override suspend fun getEnvelopeFlowById(envelopeId: Int): Flow<EnvelopeUI> =
        envelopeDao.getFlowById(envelopeId).map { TODO() }

    override suspend fun upsertEnvelope(envelope: AddEditEnvelope): Long =
        envelopeDao.upsert(envelope.toEnvelopeEntity())

    override suspend fun deleteEnvelope(envelopeId: Int) =
        envelopeDao.deleteById(envelopeId.toLong())
}

interface EnvelopeRepository {
    suspend fun getEnvelopesFlow(): Flow<List<EnvelopeUI>>
    suspend fun getEnvelopeById(envelopeId: Int): EnvelopeUI
    suspend fun getEnvelopeFlowById(envelopeId: Int): Flow<EnvelopeUI>
    suspend fun upsertEnvelope(envelope: AddEditEnvelope): Long
    suspend fun deleteEnvelope(envelopeId: Int)
}