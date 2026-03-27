package com.angelina.lvivexplorer.di

import android.content.Context
import androidx.room.Room
import com.angelina.lvivexplorer.data.local.db.AppDatabase
import com.angelina.lvivexplorer.data.local.db.DiaryDao
import com.angelina.lvivexplorer.data.local.db.PlaceDao
import com.angelina.lvivexplorer.data.local.prefs.UserPreferencesDataSource
import com.angelina.lvivexplorer.data.repository.DiaryRepositoryImpl
import com.angelina.lvivexplorer.data.repository.PlaceRepositoryImpl
import com.angelina.lvivexplorer.data.repository.SettingsRepositoryImpl
import com.angelina.lvivexplorer.data.source.AssetPlacesDataSource
import com.angelina.lvivexplorer.domain.repository.DiaryRepository
import com.angelina.lvivexplorer.domain.repository.PlaceRepository
import com.angelina.lvivexplorer.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "lviv_explorer.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providePlaceDao(db: AppDatabase): PlaceDao = db.placeDao()

    @Provides
    fun provideDiaryDao(db: AppDatabase): DiaryDao = db.diaryDao()

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferencesDataSource =
        UserPreferencesDataSource(context)

    @Provides
    @Singleton
    fun provideAssetPlacesDataSource(@ApplicationContext context: Context): AssetPlacesDataSource =
        AssetPlacesDataSource(context)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindPlaceRepository(impl: PlaceRepositoryImpl): PlaceRepository

    @Binds
    abstract fun bindDiaryRepository(impl: DiaryRepositoryImpl): DiaryRepository

    @Binds
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
