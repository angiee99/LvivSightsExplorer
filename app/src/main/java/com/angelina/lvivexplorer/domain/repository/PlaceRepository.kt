package com.angelina.lvivexplorer.domain.repository

import com.angelina.lvivexplorer.domain.model.Place
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    suspend fun seedIfNeeded()
    fun observePlaces(selectedCategories: Set<String>): Flow<List<Place>>
    fun observeCategories(): Flow<List<String>>
    suspend fun getPlaceById(id: String): Place?
}
