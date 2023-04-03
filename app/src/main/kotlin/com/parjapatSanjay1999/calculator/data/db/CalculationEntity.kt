package com.parjapatSanjay1999.calculator.data.db

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
@Parcelize
@Entity(tableName = "calculations", primaryKeys = ["expr","res"])
data class CalculationEntity(
    val expr: List<String>,
    val res: BigDecimal,
    val time:Long = System.currentTimeMillis()
):Parcelable
