package com.angelina.lvivexplorer.data.local.db

import androidx.room.TypeConverter
import com.angelina.lvivexplorer.domain.model.DiaryStatus

class Converters {
    @TypeConverter
    fun toDiaryStatus(value: String): DiaryStatus = DiaryStatus.valueOf(value)

    @TypeConverter
    fun fromDiaryStatus(status: DiaryStatus): String = status.name
}
