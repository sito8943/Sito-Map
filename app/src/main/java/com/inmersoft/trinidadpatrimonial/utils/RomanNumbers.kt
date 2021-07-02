package com.inmersoft.trinidadpatrimonial.utils

object RomanNumbers {
    fun convert(romanNumber: String): Int {
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
        val regEx = Regex("""^M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$""")
        val valid = regEx.findAll(text)
        val listReslt = mutableListOf<String>()
        valid.forEach { romanValidData ->
            val romanValid = romanValidData.value
            listReslt.add(romanValid)
        }
        return listReslt
    }
}