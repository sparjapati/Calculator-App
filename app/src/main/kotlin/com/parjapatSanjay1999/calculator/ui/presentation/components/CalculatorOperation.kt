package com.parjapatSanjay1999.calculator.ui.presentation.components

sealed class CalculatorOperation(val symbol:String){
    object Add: CalculatorOperation("+")
    object Subtract: CalculatorOperation("-")
    object Multiply: CalculatorOperation("*")
    object Divide: CalculatorOperation("/")
}
