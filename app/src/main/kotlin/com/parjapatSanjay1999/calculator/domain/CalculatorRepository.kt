package com.parjapatSanjay1999.calculator.domain

import com.parjapatSanjay1999.calculator.data.db.CalculationEntity
import kotlinx.coroutines.flow.Flow

interface CalculatorRepository {
    fun getUnCalculatedExpressions(): Flow<List<String>>
    suspend fun saveUnCalculatedExpression(expression: List<String>)
    fun getPreviousCalculations(): Flow<List<CalculationEntity>>
    suspend fun saveCalculation(calculation: CalculationEntity)
    suspend fun clearCalculationHistory()
}
