package com.angelina.lvivexplorer.data.repository

import com.angelina.lvivexplorer.data.local.db.DiaryDao
import com.angelina.lvivexplorer.data.local.db.DiaryEntryEntity
import com.angelina.lvivexplorer.data.toDomain
import com.angelina.lvivexplorer.domain.model.DiaryEntry
import com.angelina.lvivexplorer.domain.model.DiaryStatus
import com.angelina.lvivexplorer.domain.repository.DiaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val diaryDao: DiaryDao
) : DiaryRepository {
    override fun observeByStatus(status: DiaryStatus): Flow<List<DiaryEntry>> =
        diaryDao.observeByStatus(status).map { list -> list.map { it.toDomain() } }

    override fun observeSavedStatuses(placeId: String): Flow<Set<DiaryStatus>> =
        diaryDao.observeStatusesForPlace(placeId).map { it.toSet() }

    override suspend fun addOrUpdate(
        placeId: String,
        status: DiaryStatus,
        note: String?,
        visitedAt: Long?
    ) = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        diaryDao.upsertUniqueByPlaceStatus(
            DiaryEntryEntity(
                placeId = placeId,
                status = status,
                note = note,
                visitedAt = visitedAt,
                updatedAt = now
            )
        )
    }

    override suspend fun updateNote(id: Long, note: String?, visitedAt: Long?) = withContext(Dispatchers.IO) {
        diaryDao.updateNote(id, note, visitedAt, System.currentTimeMillis())
    }

    override suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        diaryDao.deleteById(id)
    }
}
