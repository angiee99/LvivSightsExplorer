package com.angelina.lvivexplorer.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(places: List<PlaceEntity>)

    @Query("SELECT * FROM places ORDER BY name ASC")
    fun observeAll(): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM places WHERE category IN (:categories) ORDER BY name ASC")
    fun observeByCategories(categories: List<String>): Flow<List<PlaceEntity>>

    @Query("SELECT DISTINCT category FROM places ORDER BY category ASC")
    fun observeCategories(): Flow<List<String>>

    @Query("SELECT * FROM places WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): PlaceEntity?

    @Query("SELECT COUNT(*) FROM places")
    suspend fun count(): Int
}
