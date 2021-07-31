package com.inmersoft.trinidadpatrimonial.database.data

import androidx.test.platform.app.InstrumentationRegistry
import com.inmersoft.trinidadpatrimonial.database.data.entity.Trinidad
import com.inmersoft.trinidadpatrimonial.database.utils.JSONObjectManager
import com.inmersoft.trinidadpatrimonial.database.utils.readJSONFromAsset
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DataBasePopulationTest {

    @Test
    fun testConvertJsonToTrinidadClass() {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory())
            .build()
        val strJSON = readJSONFromAsset(InstrumentationRegistry.getInstrumentation().context)
        val trinidadAdapter: JsonAdapter<Trinidad> = moshi.adapter(Trinidad::class.java)
        val resultPlace = trinidadAdapter.fromJson(strJSON)
        assertTrue(resultPlace!!.places.isNotEmpty())
    }

    @Test
    fun isProcessPlacesTypeCorrectly() {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory())
            .build()
        val strJSON = readJSONFromAsset(InstrumentationRegistry.getInstrumentation().context)
        val trinidadAdapter: JsonAdapter<Trinidad> = moshi.adapter(Trinidad::class.java)
        val resultPlace = trinidadAdapter.fromJson(strJSON)
        assertTrue(resultPlace!!.place_type.isNotEmpty())
    }


    @Test
    fun testExtractPlacesFromJSON() {
        val readJSON = getAssetJSON()
        assertTrue(JSONObjectManager.extractPlacesFromJSON(readJSON).isNotEmpty())
        assertEquals(
            0,
            JSONObjectManager.extractPlacesFromJSON(readJSON)[0]["place_id"]
        )
    }

    @Test
    fun testExtractRoutesFromJSON() {
        val readJSON = getAssetJSON()
        assertTrue(JSONObjectManager.extractRoutesFromJSON(readJSON).isNotEmpty())
        assertEquals(
            0,
            JSONObjectManager.extractRoutesFromJSON(readJSON)[0]["route_id"]
        )
    }

    @Test
    fun testExtractPlacesTypeFromJSON() {
        val readJSON = getAssetJSON()
        assertTrue(JSONObjectManager.extractPlacesTypeFromJSON(readJSON).isNotEmpty())
        assertEquals(
            0,
            JSONObjectManager.extractPlacesTypeFromJSON(readJSON)[0]["place_type_id"]
        )
    }


    @Test
    fun textExtractPlaceAndPlacesTypeInObjectMap() {
        val placesList = JSONObjectManager.extractPlacesFromJSON(getAssetJSON())
        val objectMap = JSONObjectManager.extractPlaceAndPlacesTypeInObjectMap(placesList)
        assertEquals(0, objectMap[0].parentValueID)
        assertEquals(0, objectMap[0].childValuesID[0])
    }

    @Test
    fun testExtractPlaceAndRoutesInObjectMap() {
        val placesList = JSONObjectManager.extractPlacesFromJSON(getAssetJSON())
        val objectMap = JSONObjectManager.extractPlaceAndRoutesInObjectMap(placesList)
        assertEquals(0, objectMap[0].parentValueID)
        assertEquals(0, objectMap[0].childValuesID[0])
    }

    @Test
    fun testExtractRoutesAndPlacesIDInObjectMap() {
        val routesList = JSONObjectManager.extractRoutesFromJSON(getAssetJSON())
        val objectMap = JSONObjectManager.extractRoutesAndPlacesIDInObjectMap(routesList)
        assertEquals(0, objectMap[0].parentValueID)
        assertEquals(0, objectMap[0].childValuesID[0])
    }

    @Test
    fun testExtractPlaceTypeAndPlacesIDInObjectMap() {
        val placesTypeList = JSONObjectManager.extractPlacesTypeFromJSON(getAssetJSON())
        val objectMap = JSONObjectManager.extractPlaceTypeAndPlacesIDInObjectMap(placesTypeList)
        assertEquals(0, objectMap[0].parentValueID)
        assertEquals(0, objectMap[0].childValuesID[0])
    }

}