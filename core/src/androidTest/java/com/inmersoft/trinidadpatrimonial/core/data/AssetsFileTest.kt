package com.inmersoft.trinidadpatrimonial.core.data

import androidx.test.platform.app.InstrumentationRegistry
import com.inmersoft.trinidadpatrimonial.core.utils.JSONObjectManager
import org.junit.Assert
import org.junit.Test
import java.io.IOException
import java.io.InputStream

class AssetsFileTest {

    @Test
    fun testImagesHeaders() {
        val readJSON = getAssetJSON()
        val places = JSONObjectManager.extractPlacesFromJSON(readJSON)
        val headersList = mutableListOf<String>()
        places.forEach { currentPlace ->
            val jsonHeaderImages = currentPlace.getJSONArray("header_images")
            for (header_url in 0 until jsonHeaderImages.length())
                headersList.add(jsonHeaderImages[header_url].toString())
        }

        val context = InstrumentationRegistry.getInstrumentation().context
        headersList.forEach { header_url ->
            var input: InputStream? = null
            val file = "file:///android_asset/$header_url.jpg"
            try {
                input = context.assets.open(file)
                Assert.assertNotNull("File: $file", input)
            } catch (ex: IOException) {
                Assert.assertNotNull("File: $file", input)
            }
        }
    }

}