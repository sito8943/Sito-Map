package com.inmersoft.trinidadpatrimonial.core.data

import androidx.test.platform.app.InstrumentationRegistry
import com.inmersoft.trinidadpatrimonial.core.utils.readJSONFromAsset


fun getAssetJSON(): String {
    return readJSONFromAsset(InstrumentationRegistry.getInstrumentation().context)
}
