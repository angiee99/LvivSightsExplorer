package com.angelina.lvivexplorer.domain.model

data class DiaryEntry(
    val id: Long,
    val placeId: String,
    val placeName: String,
    val status: DiaryStatus,
    val note: String?,
    val visitedAt: Long?,
    val updatedAt: Long
)
