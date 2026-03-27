package com.angelina.lvivexplorer.data.source

import kotlinx.serialization.Serializable

@Serializable
data class AssetPlaceDto(
    val id: String,
    val name: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val address: String,
    val imageUrl: String? = null
)
