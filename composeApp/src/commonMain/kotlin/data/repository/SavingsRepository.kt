package data.repository

import data.db.dao.SavingsDao
import data.db.table.Savings
import feature.savings.data.AddEditSavings
import kotlinx.coroutines.flow.Flow
import presentation.data.SavingsUI

class SavingsRepositoryImpl(
    private val dao: SavingsDao
): SavingsRepository {
    override suspend fun getSavingsFlow(): Flow<List<SavingsUI>> {

    }

    override suspend fun upsertSavings(savings: AddEditSavings) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSavings(savingsId: Int) =
        dao.deleteById(savingsId.toLong())

}

interface SavingsRepository {
    suspend fun getSavingsFlow(): Flow<List<Savings>>
    suspend fun upsertSavings(savings: AddEditSavings)
    suspend fun deleteSavings(savingsId: Int)
}