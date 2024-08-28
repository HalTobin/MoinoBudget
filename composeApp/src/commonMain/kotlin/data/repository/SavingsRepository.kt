package data.repository

import data.db.dao.SavingsDao
import data.mapper.toSavingsEntity
import data.mapper.toSavingsUI
import feature.savings.data.AddEditSavings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import presentation.data.SavingsUI

class SavingsRepositoryImpl(
    private val dao: SavingsDao
): SavingsRepository {
    override suspend fun getSavingsFlow(): Flow<List<SavingsUI>> = dao.getAll().map {
        it.map { savings -> savings.toSavingsUI() } }

    override suspend fun upsertSavings(savings: AddEditSavings) =
        dao.upsert(savings.toSavingsEntity())

    override suspend fun deleteSavings(savingsId: Int) =
        dao.deleteById(savingsId.toLong())
}

interface SavingsRepository {
    suspend fun getSavingsFlow(): Flow<List<SavingsUI>>
    suspend fun upsertSavings(savings: AddEditSavings): Long
    suspend fun deleteSavings(savingsId: Int)
}