package com.inmersoft.trinidadpatrimonial.di

import android.content.Context
import com.inmersoft.trinidadpatrimonial.core.data.AppDatabase
import com.inmersoft.trinidadpatrimonial.core.data.DataRepository
import com.inmersoft.trinidadpatrimonial.core.data.source.local.PlaceDao
import com.inmersoft.trinidadpatrimonial.core.data.source.local.PlaceTypeDao
import com.inmersoft.trinidadpatrimonial.core.data.source.local.RoutesDao
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrinidadDatabaseModules {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().addLast(
        KotlinJsonAdapterFactory()
    ).build()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun providePlaceDao(database: AppDatabase): PlaceDao = database.placesDao()

    @Singleton
    @Provides
    fun provideRoutesDao(database: AppDatabase): RoutesDao = database.routesDao()

    @Singleton
    @Provides
    fun providePlaceTypeDao(database: AppDatabase): PlaceTypeDao = database.placesTypeDao()

    @Singleton
    @Provides
    fun provideRepository(
        placeDao: PlaceDao,
        routesDao: RoutesDao,
        placeTypeDao: PlaceTypeDao
    ) = DataRepository(placeDao, routesDao, placeTypeDao)
}
