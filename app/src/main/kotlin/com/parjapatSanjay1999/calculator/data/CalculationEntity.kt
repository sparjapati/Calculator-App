package com.parjapatSanjay1999.calculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "calculations")
data class CalculationEntity(
    @PrimaryKey
    val time: Long = System.currentTimeMillis(),
    val expr: List<String>,
    val res: BigDecimal
)
