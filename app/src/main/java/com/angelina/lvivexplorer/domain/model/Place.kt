package com.angelina.lvivexplorer.domain.model

data class Place(
    val id: String,
    val name: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val address: String
)
