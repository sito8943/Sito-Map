package com.inmersoft.trinidadpatrimonial.utils

import android.util.Log

object RomanNumbers {

    const val ROMAN_REG_EXP = """(M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{2,3}))"""

    fun convertRxD(romanNumber: String): Int {
        var _romanNumber = romanNumber
        var result = 0
        val decimal = intArrayOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
        val roman = arrayOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")

        for (i in decimal.indices) {
            while (_romanNumber.indexOf(roman[i]) == 0) {
                result += decimal[i]
                _romanNumber = _romanNumber.substring(roman[i].length)
            }
        }
        return result
    }

    fun extractRomanNumbers(text: String): List<String> {
        val regEx = Regex(ROMAN_REG_EXP)
        val valid = regEx.findAll(text)
        val listResult = mutableListOf<String>()
        valid.forEach { romanValidData ->
            val romanValid = romanValidData.value
            listResult.add(romanValid)
        }
        return listResult
    }

    fun replaceRomanNumber(text: String): String {
        var result = text
        val roman = extractRomanNumbers(text)
        roman.forEach { currentRoman ->
            val number = convertRxD(currentRoman).toString()
            result=result.replace(currentRoman, number,true)
        }
        return result
    }
}