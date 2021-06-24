package com.inmersoft.trinidadpatrimonial.services.tts

import junit.framework.TestCase
import org.junit.Test
import java.util.*

class TTSTest : TestCase(){

    @Test
    fun localesTest(){

        val locales=Locale.getAvailableLocales()

        assertEquals(true,locales[0])

    }

}