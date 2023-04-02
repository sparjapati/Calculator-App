package com.parjapatSanjay1999.calculator.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal

class Converters {
    @TypeConverter
    fun fromStringListToString(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToStringsList(str: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(str, type)
    }

    @TypeConverter
    fun fromBigDecimalToString(res: BigDecimal): String = res.toString()

    @TypeConverter
    fun fromStringToBigDecimal(str: String): BigDecimal = BigDecimal(str)
}
