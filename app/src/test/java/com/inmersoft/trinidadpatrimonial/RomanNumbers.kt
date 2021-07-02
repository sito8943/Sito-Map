package com.inmersoft.trinidadpatrimonial

import com.inmersoft.trinidadpatrimonial.utils.RomanNumbers
import org.junit.Assert
import org.junit.Test

class RomanNumbers {

    @Test
    fun convertTest() {
// Test string, the number 895

// Test string, the number 895

        val test = "DCCCXCV"
        val result = RomanNumbers.convert(test)
        Assert.assertEquals(895, result)
    }

    @Test
    fun validateRomanNumberTest() {
        val text = "Casa de las DCCCXCV americas"
        val romanValid="DCCCXCV"

        val list = RomanNumbers.extractRomanNumbers(text)

        Assert.assertTrue(list[0]==romanValid)


    }

}