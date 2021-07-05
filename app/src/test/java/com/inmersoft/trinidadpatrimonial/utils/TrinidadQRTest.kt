package com.inmersoft.trinidadpatrimonial.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class TrinidadQRTest {

    private val trinidadQR = TrinidadQR("https://inmesoft.com | 10")

    @Test
    fun `extraer el placeID desde el escaner qr`() {
        assertEquals("10", trinidadQR.getPlaceID())
    }

    @Test
    fun `is valid Trinidad QR`() {
        assertTrue(trinidadQR.isValid())
    }


}