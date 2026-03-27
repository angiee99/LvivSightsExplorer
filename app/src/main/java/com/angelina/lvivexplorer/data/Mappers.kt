package com.angelina.lvivexplorer.data

import com.angelina.lvivexplorer.data.local.db.DiaryEntryWithPlaceName
import com.angelina.lvivexplorer.data.local.db.PlaceEntity
import com.angelina.lvivexplorer.data.source.AssetPlaceDto
import com.angelina.lvivexplorer.domain.model.DiaryEntry
import com.angelina.lvivexplorer.domain.model.Place

fun AssetPlaceDto.toEntity(): PlaceEntity = PlaceEntity(
    id = id,
    name = name,
    category = category,
    latitude = latitude,
    longitude = longitude,
    description = description,
    address = address,
    imageUrl = imageUrl
)

fun PlaceEntity.toDomain(): Place = Place(
    id = id,
    name = name,
    category = category,
    latitude = latitude,
    longitude = longitude,
    description = description,
    address = address,
    imageUrl = imageUrl
)

fun DiaryEntryWithPlaceName.toDomain(): DiaryEntry = DiaryEntry(
    id = id,
    placeId = placeId,
    placeName = placeName,
    status = status,
    note = note,
    visitedAt = visitedAt,
    updatedAt = updatedAt
)
