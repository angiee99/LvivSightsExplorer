package com.angelina.lvivexplorer.data.repository

import com.angelina.lvivexplorer.data.local.db.PlaceDao
import com.angelina.lvivexplorer.data.local.prefs.UserPreferencesDataSource
import com.angelina.lvivexplorer.data.source.AssetPlacesDataSource
import com.angelina.lvivexplorer.data.toDomain
import com.angelina.lvivexplorer.data.toEntity
import com.angelina.lvivexplorer.domain.model.Place
import com.angelina.lvivexplorer.domain.repository.PlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeDao: PlaceDao,
    private val assetPlacesDataSource: AssetPlacesDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : PlaceRepository {

    override suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {
        val count = placeDao.count()
        if (count == 0) {
            val entities = assetPlacesDataSource.loadPlaces().map { it.toEntity() }
            placeDao.upsertAll(entities)
            userPreferencesDataSource.setOnboardingDone(true)
        }
    }

    override fun observePlaces(selectedCategories: Set<String>): Flow<List<Place>> {
        val flow = if (selectedCategories.isEmpty()) {
            placeDao.observeAll()
        } else {
            placeDao.observeByCategories(selectedCategories.toList())
        }
        return flow.map { list -> list.map { it.toDomain() } }
    }

    override fun observeCategories(): Flow<List<String>> = placeDao.observeCategories()

    override suspend fun getPlaceById(id: String): Place? = withContext(Dispatchers.IO) {
        placeDao.getById(id)?.toDomain()
    }
}
