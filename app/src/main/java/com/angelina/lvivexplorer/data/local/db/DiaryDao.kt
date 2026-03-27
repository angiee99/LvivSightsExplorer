package com.angelina.lvivexplorer.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

data class DiaryEntryWithPlaceName(
    val id: Long,
    val placeId: String,
    val placeName: String,
    val status: com.angelina.lvivexplorer.domain.model.DiaryStatus,
    val note: String?,
    val visitedAt: Long?,
    val updatedAt: Long
)

@Dao
interface DiaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: DiaryEntryEntity): Long

    @Query("UPDATE diary_entries SET note = :note, visitedAt = :visitedAt, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateNote(id: Long, note: String?, visitedAt: Long?, updatedAt: Long)

    @Query("DELETE FROM diary_entries WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query(
        """
        SELECT d.id, d.placeId, p.name AS placeName, d.status, d.note, d.visitedAt, d.updatedAt
        FROM diary_entries d
        INNER JOIN places p ON p.id = d.placeId
        WHERE d.status = :status
        ORDER BY d.updatedAt DESC
        """
    )
    fun observeByStatus(status: com.angelina.lvivexplorer.domain.model.DiaryStatus): Flow<List<DiaryEntryWithPlaceName>>

    @Query("SELECT * FROM diary_entries WHERE placeId = :placeId AND status = :status LIMIT 1")
    suspend fun findByPlaceAndStatus(
        placeId: String,
        status: com.angelina.lvivexplorer.domain.model.DiaryStatus
    ): DiaryEntryEntity?

    @Transaction
    suspend fun upsertUniqueByPlaceStatus(entry: DiaryEntryEntity) {
        val existing = findByPlaceAndStatus(entry.placeId, entry.status)
        if (existing == null) {
            upsert(entry)
        } else {
            upsert(entry.copy(id = existing.id))
        }
    }
}
