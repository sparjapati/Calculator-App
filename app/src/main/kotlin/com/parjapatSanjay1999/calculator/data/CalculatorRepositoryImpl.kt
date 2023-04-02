package com.parjapatSanjay1999.calculator.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.parjapatSanjay1999.calculator.data.datastore.CalculatorDataStore
import com.parjapatSanjay1999.calculator.data.db.CalculationEntity
import com.parjapatSanjay1999.calculator.data.db.CalculatorDao
import com.parjapatSanjay1999.calculator.domain.CalculatorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val TAG = "CalculatorRepositoryImp"

class CalculatorRepositoryImpl(
    private val db: CalculatorDao,
    private val calculatorDataStore: CalculatorDataStore
) : CalculatorRepository {

    private val gson = Gson()

    override fun getUnCalculatedExpressions(): Flow<List<String>> {
        val type = object : TypeToken<List<String>>() {}.type
        return calculatorDataStore.unCalculatedExpressions.map {
            gson.fromJson(it, type)
        }
    }

    override suspend fun saveUnCalculatedExpression(expression: List<String>) {
        calculatorDataStore.saveUnCalculatedExpressions(gson.toJson(expression))
    }

    override fun getPreviousCalculations(): Flow<List<CalculationEntity>> {
        return db.getAllCalculations()
    }

    override suspend fun saveCalculation(calculation: CalculationEntity) {
        db.insertCalculation(calculation)
    }

    override suspend fun clearCalculationHistory() {
        db.clearAllCalculations()
    }
}
