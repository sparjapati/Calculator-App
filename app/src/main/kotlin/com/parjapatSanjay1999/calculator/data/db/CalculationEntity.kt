package com.parjapatSanjay1999.calculator.data.db

import androidx.room.Entity
import java.math.BigDecimal

@Entity(tableName = "calculations", primaryKeys = ["expr","res"])
data class CalculationEntity(
    val expr: List<String>,
    val res: BigDecimal,
    val time:Long = System.currentTimeMillis()
)
