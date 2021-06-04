package com.inmersoft.trinidadpatrimonial.core.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.inmersoft.trinidadpatrimonial.core.data.AppDatabase
import com.inmersoft.trinidadpatrimonial.core.data.entity.Trinidad
import com.inmersoft.trinidadpatrimonial.core.utils.readJSONFromAsset
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.coroutineScope


class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            runCatching {

                val database = AppDatabase.getDatabase(applicationContext)
                val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                val jsonReader = readJSONFromAsset(context = applicationContext)
                val trinidadAdapter: JsonAdapter<Trinidad> =
                    moshi.adapter(Trinidad::class.java)
                val resultTrinidadFromJson =
                    trinidadAdapter.fromJson(jsonReader)

                val placesDao = database.placesDao()
                val routesDao = database.routesDao()
                val placesTypeDao = database.placesTypeDao()

                resultTrinidadFromJson?.let { placesDao.insertAll(it.places) }
                resultTrinidadFromJson?.let { routesDao.insertAll(it.routes) }
                resultTrinidadFromJson?.let { placesTypeDao.insertAll(it.place_type) }
                Log.d(TAG, "doWork: Called")
                Result.success()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    } as Result


    companion object {
        private const val TAG = "SeedDatabaseWorker"
    }
}