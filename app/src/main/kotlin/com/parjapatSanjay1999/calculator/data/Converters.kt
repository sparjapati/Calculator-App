package com.parjapatSanjay1999.calculator.data

import androidx.room.TypeConverter
import java.math.BigDecimal

class Converters {
    @TypeConverter
    fun fromStringListToString(list: List<String>): String {
        return list.joinToString(separator = "#")
    }

    @TypeConverter
    fun fromStringToStringsList(str: String): List<String> {
        return str.split("#")
    }

    @TypeConverter
    fun fromBigDecimalToString(res: BigDecimal): String = res.toString()

    @TypeConverter
    fun fromStringToBigDecimal(str: String): BigDecimal = BigDecimal(str)
}
