package com.parjapatSanjay1999.calculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "calculations", primaryKeys = ["expr","res"])
data class CalculationEntity(
    val expr: List<String>,
    val res: BigDecimal,
    val time:Long = System.currentTimeMillis()
)
