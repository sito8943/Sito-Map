package com.inmersoft.trinidadpatrimonial.core.data

import androidx.test.platform.app.InstrumentationRegistry
import com.inmersoft.trinidadpatrimonial.core.data.entity.Trinidad
import com.inmersoft.trinidadpatrimonial.core.utils.readJSONFromAsset
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert
import org.junit.Test

class DataBasePopulation {

    @Test
    fun testConvertJsonToTrinidadClass() {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory())
            .build()
        val strJSON = readJSONFromAsset(InstrumentationRegistry.getInstrumentation().context)
        val trinidadAdapter: JsonAdapter<Trinidad> = moshi.adapter(Trinidad::class.java)
        val resultPlace = trinidadAdapter.fromJson(strJSON)
        Assert.assertTrue(resultPlace!!.places.isNotEmpty())
    }

}