package data.repository

import data.db.dao.SavingsDao
import data.mapper.toSavingsEntity
import data.mapper.toSavingsUI
import feature.savings.feature.add_edit_savings.data.AddEditSavings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import feature.savings.data.SavingsUI

class SavingsRepositoryImpl(
    private val savingsDao: SavingsDao
): SavingsRepository {
    override suspend fun getSavingsFlow(): Flow<List<SavingsUI>> = savingsDao.getAll().map {
        it.map { savings -> savings.toSavingsUI() } }

    override suspend fun getSavingsById(savingsId: Int): SavingsUI? =
        savingsDao.getById(savingsId)?.toSavingsUI()

    override suspend fun getSavingsFlowById(savingsId: Int): Flow<SavingsUI?> =
        savingsDao.getFlowById(savingsId).map { it?.toSavingsUI() }

    override suspend fun upsertSavings(savings: AddEditSavings) =
        savingsDao.upsert(savings.toSavingsEntity())

    override suspend fun updateAmount(id: Int, newAmount: Int) =
        savingsDao.updateAmount(id = id, newAmount = newAmount)

    override suspend fun deleteSavings(savingsId: Int) =
        savingsDao.deleteById(savingsId.toLong())
}

interface SavingsRepository {
    suspend fun getSavingsFlow(): Flow<List<SavingsUI>>
    suspend fun getSavingsById(savingsId: Int): SavingsUI?
    suspend fun getSavingsFlowById(savingsId: Int): Flow<SavingsUI?>
    suspend fun upsertSavings(savings: AddEditSavings): Long
    suspend fun updateAmount(id: Int, newAmount: Int)
    suspend fun deleteSavings(savingsId: Int)
}