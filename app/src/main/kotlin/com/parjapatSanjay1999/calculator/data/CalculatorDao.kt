package com.parjapatSanjay1999.calculator.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculatorDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCalculation(calculationEntity: CalculationEntity)

    @Query("DELETE FROM calculations")
    suspend fun clearAllCalculations()

    @Query("SELECT * FROM calculations ORDER BY time ASC")
    fun getAllCalculations(): Flow<List<CalculationEntity>>
}
