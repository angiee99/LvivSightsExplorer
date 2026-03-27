package com.angelina.lvivexplorer.data.source

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class AssetPlacesDataSource(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun loadPlaces(fileName: String = "sights_lviv.json"): List<AssetPlaceDto> =
        withContext(Dispatchers.IO) {
            val raw = context.assets.open(fileName).bufferedReader().use { it.readText() }
            json.decodeFromString<List<AssetPlaceDto>>(raw)
        }
}
