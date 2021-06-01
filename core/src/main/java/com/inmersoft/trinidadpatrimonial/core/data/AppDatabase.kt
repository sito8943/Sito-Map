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
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.lang.reflect.Type

@Database(
    entities = [Place::class, Route::class, PlaceType::class],
    version = 1,
    exportSchema = false
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
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "trinidad-db.db"
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
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        val moshi = Moshi.Builder().build()
                        val type: Type = Types.newParameterizedType(
                            String::class.java,
                            Trinidad::class.java,
                        )
                        val trinidadAdapter: JsonAdapter<Trinidad> = moshi.adapter(type)
                        val strJSON = readJSONFromAsset(context)
                        val resultTrinidadFromJson = trinidadAdapter.fromJson(strJSON)
                        populateDatabase(
                            database,
                            resultTrinidadFromJson!!
                        )
                    }
                }
            }
        }

        suspend fun populateDatabase(database: AppDatabase, infoFromJsonAsset: Trinidad) {
            val placesDao = database.placesDao()
            val routesDao = database.routesDao()
            val placesTypeDao = database.placesTypeDao()

            placesDao.deleteAll()
            routesDao.deleteAll()
            placesTypeDao.deleteAll()

            try {
                val placesList = infoFromJsonAsset.places
                placesList?.forEach { placeItem ->
                    placesDao.insert(placeItem)
                }

                val routesList = infoFromJsonAsset.routes
                routesList?.forEach { routesItem ->
                    routesDao.insert(routesItem)
                }

                val placeTypeList = infoFromJsonAsset.placeType
                placeTypeList?.forEach { placeTypeListItem ->
                    placesTypeDao.insert(placeTypeListItem)
                }
            } catch (ex: java.lang.Exception) {
                Log.e("ERROR", "populateDatabase: ${ex.printStackTrace()}")
            }
        }

        fun readJSONFromAsset(context: Context): String {
            val json: String
            try {
                val inputStream: InputStream = context.assets.open("trinidad-db.json")
                json = inputStream.bufferedReader().use {
                    it.readText()
                }
            } catch (ex: Exception) {
                ex.localizedMessage
                Log.e("APP-Database", "readJSONFromAsset: ${ex.localizedMessage}")
                return ""
            }
            return json
        }

    }
}