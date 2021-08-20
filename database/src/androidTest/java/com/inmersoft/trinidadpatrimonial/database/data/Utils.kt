package com.inmersoft.trinidadpatrimonial.database.data

import androidx.test.platform.app.InstrumentationRegistry
import com.inmersoft.trinidadpatrimonial.database.utils.readJSONFromAsset


fun getAssetJSON(): String {
    return readJSONFromAsset(InstrumentationRegistry.getInstrumentation().context)
}
