package data.repository

import data.db.dao.LabelDao
import data.mapper.toLabelEntity
import data.mapper.toLabelUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import presentation.data.LabelUI

class LabelRepositoryImpl(
    private val labelDao: LabelDao
): LabelRepository {

    override suspend fun addLabel(label: LabelUI) =
        labelDao.upsert(label.toLabelEntity())

    override suspend fun addLabels(labels: List<LabelUI>) =
        labelDao.upsertAll(labels.map { it.toLabelEntity() })

    override suspend fun getLabels(): List<LabelUI> =
        labelDao.getAll().map { it.toLabelUI() }

    override fun getLabelsFlow(): Flow<List<LabelUI>> =
        labelDao.getAllFlow().map { list -> list.map { it.toLabelUI() } }

}

interface LabelRepository {
    suspend fun addLabel(label: LabelUI)
    suspend fun addLabels(labels: List<LabelUI>)
    suspend fun getLabels(): List<LabelUI>
    fun getLabelsFlow(): Flow<List<LabelUI>>
}