package data.repository

import androidx.compose.ui.graphics.Color
import data.db.dao.EnvelopeDao
import data.db.dao.ExpenseDao
import data.db.table.Envelope
import data.mapper.toEnvelopeEntity
import feature.expenses.data.EnvelopeUI
import feature.expenses.feature.add_edit_envelope.data.AddEditEnvelope
import feature.expenses.feature.envelope_details.data.ExpensePeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import presentation.data.ExpenseFrequency
import presentation.data.ExpenseIcon
import util.getPeriodFromTimestamp
import util.toEpochMillisecond
import util.toLocalDate
import kotlin.math.exp

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

    private suspend fun Envelope.toEnvelopeUI(selectedPeriod: Pair<LocalDate, LocalDate>? = null): EnvelopeUI {
        val currentDate = Clock.System.now().toEpochMilliseconds().toLocalDate()
        val periods = selectedPeriod ?: getPeriodFromTimestamp(currentDate, this.frequency == ExpenseFrequency.Annually.id)
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

    override suspend fun getEnvelopeHistoryFlowByEnvelopeId(envelopeId: Int): Flow<List<EnvelopeUI>> =
        combine(
            envelopeDao.getFlowById(envelopeId),
            getPeriodsFlowByEnvelopeId(envelopeId)
        ) { envelope, periods ->
            periods.mapNotNull { period ->
                envelope?.toEnvelopeUI(period.getPeriods())
            }
        }

    override suspend fun getPeriodsFlowByEnvelopeId(envelopeId: Int): Flow<List<ExpensePeriod>> =
        combine(
            envelopeDao.getFlowById(envelopeId),
            expenseDao.getOldestTimeFlowByEnvelopeId(envelopeId)
        ) { envelope, oldestTime ->
            envelope?.let {
                oldestTime?.toLocalDate()?.let { oldestDate ->
                    val currentDate = Clock.System.now().toEpochMilliseconds().toLocalDate()
                    return@combine when (envelope.frequency) {
                        // Monthly Frequency
                        0 -> {
                            val periods = generateMonthlyPeriods(oldestDate, currentDate)
                            periods.map { ExpensePeriod.Month(it.first.year, it.first.monthNumber) }
                        }
                        // Annual Frequency
                        1 -> {
                            val periods = generateAnnualPeriods(oldestDate, currentDate)
                            periods.map { ExpensePeriod.Year(it.first.year) }
                        }
                        else -> emptyList()
                    }
                }
                emptyList()
            } ?: emptyList()
        }

    // Generates a list of monthly period pairs (startDate, endDate)
    private fun generateMonthlyPeriods(start: LocalDate, end: LocalDate): List<Pair<LocalDate, LocalDate>> {
        val periods = mutableListOf<Pair<LocalDate, LocalDate>>()
        var currentStart = LocalDate(start.year, start.monthNumber, 1)  // Start at the beginning of the month
        while (currentStart <= end) {
            val currentEnd = currentStart.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
            periods.add(currentStart to currentEnd)
            currentStart = currentStart.plus(1, DateTimeUnit.MONTH)
        }
        return periods
    }

    // Generates a list of yearly period pairs (startDate, endDate)
    private fun generateAnnualPeriods(start: LocalDate, end: LocalDate): List<Pair<LocalDate, LocalDate>> {
        val periods = mutableListOf<Pair<LocalDate, LocalDate>>()
        var currentStart = LocalDate(year = start.year, month = Month.JANUARY, dayOfMonth = 1)  // Start at the beginning of the year
        while (currentStart <= end) {
            val currentEnd = currentStart.plus(1, DateTimeUnit.YEAR).minus(1, DateTimeUnit.DAY)
            periods.add(currentStart to currentEnd)
            currentStart = currentStart.plus(1, DateTimeUnit.YEAR)
        }
        return periods
    }

}

interface EnvelopeRepository {
    suspend fun getEnvelopesFlow(): Flow<List<EnvelopeUI>>
    suspend fun getEnvelopeById(envelopeId: Int): EnvelopeUI?
    suspend fun getEnvelopeFlowById(envelopeId: Int): Flow<EnvelopeUI?>
    suspend fun upsertEnvelope(envelope: AddEditEnvelope): Long
    suspend fun deleteEnvelope(envelopeId: Int)

    suspend fun getEnvelopeHistoryFlowByEnvelopeId(envelopeId: Int): Flow<List<EnvelopeUI>>
    suspend fun getPeriodsFlowByEnvelopeId(envelopeId: Int): Flow<List<ExpensePeriod>>
}