package com.parjapatSanjay1999.calculator.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CalculationEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class CalculationDatabase : RoomDatabase() {
    abstract val dao: CalculatorDao

    companion object{
        const val DATABASE_NAME = "calculations.db"
    }
}
