package com.inmersoft.trinidadpatrimonial

import com.inmersoft.trinidadpatrimonial.utils.RomanNumbers
import org.junit.Assert
import org.junit.Test

class RomanNumbersTest {

    @Test
    fun convertTest() {
// Test string, the number 895
        val test = "XVIII"
        val result = RomanNumbers.convertRxD(test)
        Assert.assertEquals(18, result)
    }

    @Test
    fun validateRomanNumberTest() {
        val text =
            "Cronológicamente ubicada dentro de la primera mitad del siglo XVIII, esta vivienda constituye uno de "
        val romanValid = "XVIII"
        val list = RomanNumbers.extractRomanNumbers(text)
        Assert.assertEquals(list[0], romanValid)
    }

    @Test
    fun validateRomanNumberReplaceTest() {
        val text =
            "Cronológicamente ubicada dentro de la primera mitad del siglo XVIII, esta vivienda constituye uno de "
        val expected =
            "Cronológicamente ubicada dentro de la primera mitad del siglo 18, esta vivienda constituye uno de "

        val resultReplace: String = RomanNumbers.replaceRomanNumber(text)

        Assert.assertEquals(expected, resultReplace)
    }
}