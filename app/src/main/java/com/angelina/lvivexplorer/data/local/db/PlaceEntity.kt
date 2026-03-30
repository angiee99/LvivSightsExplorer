package com.angelina.lvivexplorer.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val address: String
)
