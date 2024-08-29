package data.repository

import data.db.dao.LabelDao
import data.db.dao.SavingsDao
import data.mapper.toLabelUI
import data.mapper.toSavingsEntity
import data.mapper.toSavingsUI
import feature.savings.data.AddEditSavings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import presentation.data.SavingsUI

class SavingsRepositoryImpl(
    private val savingsDao: SavingsDao,
    private val labelsDao: LabelDao
): SavingsRepository {
    override suspend fun getSavingsFlow(): Flow<List<SavingsUI>> = savingsDao.getAll().map {
        it.map { savings -> savings.toSavingsUI(labelsDao.getAll().map { label -> label.toLabelUI() }) } }

    override suspend fun upsertSavings(savings: AddEditSavings) =
        savingsDao.upsert(savings.toSavingsEntity())

    override suspend fun deleteSavings(savingsId: Int) =
        savingsDao.deleteById(savingsId.toLong())
}

interface SavingsRepository {
    suspend fun getSavingsFlow(): Flow<List<SavingsUI>>
    suspend fun upsertSavings(savings: AddEditSavings): Long
    suspend fun deleteSavings(savingsId: Int)
}