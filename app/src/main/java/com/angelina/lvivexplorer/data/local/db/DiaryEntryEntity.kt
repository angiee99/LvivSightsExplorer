package com.angelina.lvivexplorer.data.local.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.angelina.lvivexplorer.domain.model.DiaryStatus

@Entity(
    tableName = "diary_entries",
    foreignKeys = [
        ForeignKey(
            entity = PlaceEntity::class,
            parentColumns = ["id"],
            childColumns = ["placeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("placeId"), Index("status")]
)
data class DiaryEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val placeId: String,
    val status: DiaryStatus,
    val note: String?,
    val visitedAt: Long?,
    val updatedAt: Long
)
