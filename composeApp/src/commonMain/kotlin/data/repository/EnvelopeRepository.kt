package data.repository

import androidx.compose.ui.graphics.Color
import data.db.dao.EnvelopeDao
import data.db.dao.ExpenseDao
import data.db.table.Envelope
import data.db.table.Expense
import data.mapper.toEnvelopeEntity
import feature.expenses.data.EnvelopeUI
import feature.expenses.feature.add_edit_envelope.data.AddEditEnvelope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Clock
import kotlinx.datetime.daysUntil
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import util.getPeriodFromTimestamp
import util.toEpochMillisecond
import util.toLocalDate

class EnvelopeRepositoryImpl(
    private val envelopeDao: EnvelopeDao,
    private val expenseDao: ExpenseDao
): EnvelopeRepository {
    override suspend fun getEnvelopesFlow(): Flow<List<EnvelopeUI>> =
        combine(
            envelopeDao.getAll(),
            expenseDao.getAll()
        ) { envelopes, _ ->
            envelopes.mapToEnvelopeUI()
        }

    override suspend fun getEnvelopeById(envelopeId: Int): EnvelopeUI? =
        envelopeDao.getBydId(envelopeId)?.toEnvelopeUI()

    override suspend fun getEnvelopeFlowById(envelopeId: Int): Flow<EnvelopeUI?> =
        combine(
            envelopeDao.getFlowById(envelopeId),
            expenseDao.getFlowByEnvelopeId(envelopeId)
        ) { envelope, _ ->
            envelope?.toEnvelopeUI()
        }

    override suspend fun upsertEnvelope(envelope: AddEditEnvelope): Long =
        envelopeDao.upsert(envelope.toEnvelopeEntity())

    override suspend fun deleteEnvelope(envelopeId: Int) =
        envelopeDao.deleteById(envelopeId.toLong())

    private suspend fun Envelope.toEnvelopeUI(): EnvelopeUI {
        val currentDate = Clock.System.now().toEpochMilliseconds().toLocalDate()
        val periods = getPeriodFromTimestamp(currentDate, this.frequency == ExpenseFrequency.Annually.id)
        val relatedExpenses = expenseDao.getByEnvelopeIdAndPeriod(this.id, periods.first.toEpochMillisecond(), periods.second.toEpochMillisecond())
        val current = relatedExpenses.sumOf { it.amount.toDouble() }
        return EnvelopeUI(
            id = this.id,
            title = this.title,
            current = current,
            startPeriod = periods.first,
            endPeriod = periods.second,
            frequency = ExpenseFrequency.findById(this.frequency),
            icon = this.iconId?.let { ExpenseIcon.findById(it) },
            max = this.max,
            remainingMoney = this.max?.let { it - current.toInt() },
            remainingDays = currentDate.daysUntil(periods.second),
            color = this.color?.let { Color(it) }
        )
    }

    private suspend fun List<Envelope>.mapToEnvelopeUI(): List<EnvelopeUI> =
        this.map { it.toEnvelopeUI() }
}

interface EnvelopeRepository {
    suspend fun getEnvelopesFlow(): Flow<List<EnvelopeUI>>
    suspend fun getEnvelopeById(envelopeId: Int): EnvelopeUI?
    suspend fun getEnvelopeFlowById(envelopeId: Int): Flow<EnvelopeUI?>
    suspend fun upsertEnvelope(envelope: AddEditEnvelope): Long
    suspend fun deleteEnvelope(envelopeId: Int)
}