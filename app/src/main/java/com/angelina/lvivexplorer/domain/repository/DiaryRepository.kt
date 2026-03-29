package com.angelina.lvivexplorer.domain.repository

import com.angelina.lvivexplorer.domain.model.DiaryEntry
import com.angelina.lvivexplorer.domain.model.DiaryStatus
import kotlinx.coroutines.flow.Flow

interface DiaryRepository {
    fun observeByStatus(status: DiaryStatus): Flow<List<DiaryEntry>>
    fun observeSavedStatuses(placeId: String): Flow<Set<DiaryStatus>>
    suspend fun addOrUpdate(placeId: String, status: DiaryStatus, note: String? = null, visitedAt: Long? = null)
    suspend fun updateNote(id: Long, note: String?, visitedAt: Long?)
    suspend fun delete(id: Long)
}
