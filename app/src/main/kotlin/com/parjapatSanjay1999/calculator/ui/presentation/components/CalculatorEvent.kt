package com.parjapatSanjay1999.calculator.ui.presentation.components

sealed class CalculatorEvent {
    object Clear : CalculatorEvent()
    object Delete : CalculatorEvent()
    object Decimal: CalculatorEvent()
    object Percentage: CalculatorEvent()
    object Calculate : CalculatorEvent()
    object ChangeSign:CalculatorEvent()
    data class CalculatorNum(val num :Int) : CalculatorEvent()
    data class Operation(val operation: CalculatorOperation) : CalculatorEvent()
    object ToggleShowHistory:CalculatorEvent()
    object ClearHistory:CalculatorEvent()
}
