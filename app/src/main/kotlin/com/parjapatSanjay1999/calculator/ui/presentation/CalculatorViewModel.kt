package com.parjapatSanjay1999.calculator.ui.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.parjapatSanjay1999.calculator.ui.presentation.components.CalculatorEvent
import com.parjapatSanjay1999.calculator.ui.presentation.components.CalculatorOperation
import com.parjapatSanjay1999.calculator.ui.presentation.components.CalculatorState

private const val TAG = "CalculatorViewModel"

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    fun onEvent(event: CalculatorEvent) {
        when (event) {
            CalculatorEvent.Calculate -> {
                if (state.operation == null) return
                val num1 = state.num1!!.toFloat()
                val num2 = state.num2!!.toFloat()
                val calculatedResult = calculate(num1, num2, state.operation!!)
                state =
                    state.copy(result = "= " + calculate(num1, num2, state.operation!!).toString())
                saveState(state)
            }
            is CalculatorEvent.CalculatorNum -> {
                if(state.result!=null)
                    state = state.copy(num1 = null, num2 = null, operation = null, result = null)
                state = if (state.operation == null) {
                    state.copy(num1 = if (state.num1 != null) state.num1 + event.num else event.num.toString())
                } else {
                    state.copy(num2 = if (state.num2 != null) state.num2 + event.num else event.num.toString())
                }
            }
            CalculatorEvent.Clear -> {
                state = state.copy(num1 = null, num2 = null, operation = null, result = null)
            }
            CalculatorEvent.Decimal -> {
                state = if (state.operation == null) {
                    if (state.num1 != null && !state.num1!!.contains(".")) state.copy(num1 = state.num1 + ".")
                    else state
                } else {
                    if (state.num2 != null && !state.num2!!.contains(".")) state.copy(num2 = state.num2 + ".")
                    else state
                }
            }
            CalculatorEvent.Delete -> {
                state = if (state.operation == null) {
                    state.copy(num1 = state.num1?.dropLast(1))
                } else {
                    state.copy(num2 = state.num2?.dropLast(1))
                }
            }
            is CalculatorEvent.Operation -> {
                if (state.operation == null) {
                    state = state.copy(operation = event.operation)
                    return
                }
                val num1 = state.num1!!.toFloat()
                val num2 = state.num2!!.toFloat()
                val calculatedResult = calculate(num1, num2, state.operation!!)
                state = state.copy(
                    num1 = calculatedResult.toString(),
                    num2 = null,
                    operation = event.operation,
                    result = null
                )
            }
            CalculatorEvent.Percentage -> {
                state = if (state.operation == null)
                    state.copy(num1 = ((state.num1?.toFloat() ?: return) / 100).toString())
                else
                    state.copy(num2 = ((state.num2?.toFloat() ?: return) / 100).toString())
            }
        }
    }

    private fun calculate(num1: Float, num2: Float, operation: CalculatorOperation): Float {
        return when (operation) {
            CalculatorOperation.Add -> num1 + num2
            CalculatorOperation.Divide -> num1 / num2
            CalculatorOperation.Multiply -> num1 * num2
            CalculatorOperation.Subtract -> num1 - num2
        }
    }

    private fun saveState(state: CalculatorState) {

    }
}
