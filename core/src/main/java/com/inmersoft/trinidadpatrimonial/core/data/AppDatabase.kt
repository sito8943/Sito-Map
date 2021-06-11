package com.inmersoft.trinidadpatrimonial.core.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.inmersoft.trinidadpatrimonial.core.data.converters.Converters
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceType
import com.inmersoft.trinidadpatrimonial.core.data.entity.Route
import com.inmersoft.trinidadpatrimonial.core.data.entity.cross_refrences.PlaceTypesAndPlacesCrossRef
import com.inmersoft.trinidadpatrimonial.core.data.entity.cross_refrences.RoutesAndPlacesCrossRef
import com.inmersoft.trinidadpatrimonial.core.data.source.local.*
import com.inmersoft.trinidadpatrimonial.core.utils.DATABASE_NAME
import com.inmersoft.trinidadpatrimonial.core.workers.SeedDatabaseWorker

@Database(
    entities = [Place::class, Route::class, PlaceType::class,PlaceTypesAndPlacesCrossRef::class, RoutesAndPlacesCrossRef::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun placesDao(): PlaceDao
    abstract fun routesDao(): RoutesDao
    abstract fun placesTypeDao(): PlaceTypeDao

    abstract fun routesAndPlacesCrossDao(): RoutesAndPlacesCrossDao
    abstract fun placeTypesAndPlacesCrossDao(): PlaceTypesAndPlacesCrossDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("DATABASE-X255", "onCreate: CALLED SEED DATABASE")
                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .build()
        }

    }
}