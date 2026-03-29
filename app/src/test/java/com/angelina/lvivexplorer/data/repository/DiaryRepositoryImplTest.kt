package com.angelina.lvivexplorer.data.repository

import com.angelina.lvivexplorer.data.local.db.DiaryDao
import com.angelina.lvivexplorer.data.local.db.DiaryEntryEntity
import com.angelina.lvivexplorer.data.local.db.DiaryEntryWithPlaceName
import com.angelina.lvivexplorer.domain.model.DiaryStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class DiaryRepositoryImplTest {
    @Test
    fun `addOrUpdate writes expected status and note`() = runTest {
        val fakeDao = FakeDiaryDao()
        val repository = DiaryRepositoryImpl(fakeDao)

        repository.addOrUpdate(
            placeId = "lviv-opera",
            status = DiaryStatus.WANT_TO_VISIT,
            note = "Go in spring",
            visitedAt = null
        )

        val entry = fakeDao.lastUpserted
        assertNotNull(entry)
        assertEquals("lviv-opera", entry?.placeId)
        assertEquals(DiaryStatus.WANT_TO_VISIT, entry?.status)
        assertEquals("Go in spring", entry?.note)
    }
}

private class FakeDiaryDao : DiaryDao {
    val entries = mutableListOf<DiaryEntryEntity>()
    var lastUpserted: DiaryEntryEntity? = null
    private val state = MutableStateFlow<List<DiaryEntryWithPlaceName>>(emptyList())

    override suspend fun upsert(entry: DiaryEntryEntity): Long {
        val id = if (entry.id == 0L) (entries.size + 1).toLong() else entry.id
        val stored = entry.copy(id = id)
        entries.removeAll { it.id == id }
        entries += stored
        lastUpserted = stored
        return id
    }

    override suspend fun updateNote(id: Long, note: String?, visitedAt: Long?, updatedAt: Long) {
        entries.replaceAll { current ->
            if (current.id == id) current.copy(note = note, visitedAt = visitedAt, updatedAt = updatedAt) else current
        }
    }

    override suspend fun deleteById(id: Long) {
        entries.removeAll { it.id == id }
    }

    override fun observeByStatus(status: DiaryStatus): Flow<List<DiaryEntryWithPlaceName>> {
        return state
    }

    override fun observeStatusesForPlace(placeId: String): Flow<List<DiaryStatus>> {
        return MutableStateFlow(entries.filter { it.placeId == placeId }.map { it.status })
    }

    override suspend fun findByPlaceAndStatus(placeId: String, status: DiaryStatus): DiaryEntryEntity? {
        return entries.firstOrNull { it.placeId == placeId && it.status == status }
    }

    override suspend fun upsertUniqueByPlaceStatus(entry: DiaryEntryEntity) {
        val existing = findByPlaceAndStatus(entry.placeId, entry.status)
        if (existing == null) upsert(entry) else upsert(entry.copy(id = existing.id))
    }
}
