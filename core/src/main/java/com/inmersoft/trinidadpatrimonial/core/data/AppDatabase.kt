package com.inmersoft.trinidadpatrimonial.core.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.inmersoft.trinidadpatrimonial.core.data.converters.Converters
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceType
import com.inmersoft.trinidadpatrimonial.core.data.entity.Route
import com.inmersoft.trinidadpatrimonial.core.data.entity.Trinidad
import com.inmersoft.trinidadpatrimonial.core.data.source.local.PlaceDao
import com.inmersoft.trinidadpatrimonial.core.data.source.local.PlaceTypeDao
import com.inmersoft.trinidadpatrimonial.core.data.source.local.RoutesDao
import com.inmersoft.trinidadpatrimonial.core.utils.DATABASE_NAME
import com.inmersoft.trinidadpatrimonial.core.utils.readJSONFromAsset
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Place::class, Route::class, PlaceType::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun placesDao(): PlaceDao
    abstract fun routesDao(): RoutesDao
    abstract fun placesTypeDao(): PlaceTypeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration()
                        .addCallback(
                            TrinidadDatabaseCallback(
                                context,
                                scope
                            )
                        )
                        .build()
                    INSTANCE = instance
                    instance
                }
        }

        private class TrinidadDatabaseCallback(
            private val context: Context,
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory())
                            .build()
                        val strJSON = readJSONFromAsset(context)
                        val trinidadAdapter: JsonAdapter<Trinidad> =
                            moshi.adapter(Trinidad::class.java)
                        Log.d("DATABASE-TRINIDAD", "onOpen: PROCESS DATABASE-TRINIDAD CALLBACK")
                        val resultTrinidadFromJson = trinidadAdapter.fromJson(strJSON)
                        populateDatabase(
                            database,
                            resultTrinidadFromJson!!
                        )
                    }
                }
            }
        }

        private suspend fun populateDatabase(database: AppDatabase, infoFromJsonAsset: Trinidad) {
            val placesDao = database.placesDao()
            val routesDao = database.routesDao()
            val placesTypeDao = database.placesTypeDao()
            Log.d("DATABASE-TRINIDAD", "populateDatabase: populateDatabase START")

            database.clearAllTables()

            try {

                placesDao.insertAll(infoFromJsonAsset.places)

                routesDao.insertAll(infoFromJsonAsset.routes)

                placesTypeDao.insertAll(infoFromJsonAsset.place_type)

            } catch (ex: java.lang.Exception) {
                Log.d(
                    "DATABASE-TRINIDAD",
                    "populateDatabaseERROR: populateDatabase ${ex.printStackTrace()}"
                )

            }
        }
    }
}