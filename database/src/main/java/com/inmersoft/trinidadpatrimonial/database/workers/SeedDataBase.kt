package com.inmersoft.trinidadpatrimonial.database.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.inmersoft.trinidadpatrimonial.database.data.AppDatabase
import com.inmersoft.trinidadpatrimonial.database.data.entity.Trinidad
import com.inmersoft.trinidadpatrimonial.database.data.entity.cross_refrences.PlaceTypesAndPlacesCrossRef
import com.inmersoft.trinidadpatrimonial.database.data.entity.cross_refrences.RoutesAndPlacesCrossRef
import com.inmersoft.trinidadpatrimonial.database.utils.JSONObjectManager
import com.inmersoft.trinidadpatrimonial.database.utils.ObjectMap
import com.inmersoft.trinidadpatrimonial.database.utils.readJSONFromAsset
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext


class SeedDatabaseWorker(
    private val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {

        withContext(Dispatchers.Default) {
            val startTime = System.currentTimeMillis()
            val database = AppDatabase.getDatabase(context = context)
            val placesDao = database.placesDao()
            val routesDao = database.routesDao()
            val placesTypeDao = database.placesTypeDao()

            val placeTypesAndPlacesCrossDao = database.placeTypesAndPlacesCrossDao()
            val routesAndPlacesCrossDao = database.routesAndPlacesCrossDao()

            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

            val jsonReader = readJSONFromAsset(context = applicationContext)

            val trinidadAdapter: JsonAdapter<Trinidad> =
                moshi.adapter(Trinidad::class.java)

            val resultTrinidadFromJson =
                trinidadAdapter.fromJson(jsonReader)

            val placeJSONList = JSONObjectManager.extractPlacesFromJSON(jsonReader)
            val placeWithPlacesTypeId: List<ObjectMap> =
                JSONObjectManager.extractPlaceAndPlacesTypeInObjectMap(placeJSONList)

            placeWithPlacesTypeId.forEach {
                val placeID = it.parentValueID
                val placeTypesId = it.childValuesID
                placeTypesId.forEach { currentPlaceTypeId ->
                    placeTypesAndPlacesCrossDao.addPlaceAndPlaceTypeCrossRef(
                        PlaceTypesAndPlacesCrossRef(
                            placeID, currentPlaceTypeId
                        )
                    )
                }
            }

            val routesJSONList = JSONObjectManager.extractRoutesFromJSON(jsonReader)
            val routesWithPlacesId: List<ObjectMap> =
                JSONObjectManager.extractRoutesAndPlacesIDInObjectMap(routesJSONList)
            routesWithPlacesId.forEach {
                val routesID = it.parentValueID
                val placesID = it.childValuesID
                placesID.forEach { currentPlaceID ->
                    routesAndPlacesCrossDao.addRoutesAndPlacesCrossRef(
                        RoutesAndPlacesCrossRef(
                            routesID, currentPlaceID
                        )
                    )
                }
            }

            withContext(Dispatchers.IO) {
                resultTrinidadFromJson?.let {
                    placesDao.insertAll(it.places)
                    routesDao.insertAll(it.routes)
                    placesTypeDao.insertAll(it.place_type)
                }
            }

            Log.d(TAG, "doWork: Called")
            val endTime = System.currentTimeMillis()
            val resultCalc = endTime - startTime
            Log.d(TAG, "TIME: $resultCalc")
            Result.success()
        }
    }

    companion object {
        private const val TAG = "SeedDatabaseWorker"
    }
}