package com.parjapatSanjay1999.calculator.ui.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.parjapatSanjay1999.calculator.ui.presentation.components.CalculatorEvent
import com.parjapatSanjay1999.calculator.ui.presentation.components.CalculatorOperation
import java.math.BigDecimal
import java.util.*

private const val TAG = "CalculatorViewModel"

class CalculatorViewModel : ViewModel() {

    var result by mutableStateOf<BigDecimal?>(null)
        private set
    private val _expression = mutableStateListOf<String>()
    val expression = _expression

    private var _selectedIndex by mutableStateOf<Int?>(null)
    val isSelectedMode = _selectedIndex

    fun onEvent(event: CalculatorEvent) {
        when (event) {
            CalculatorEvent.Calculate -> {
                if (_expression.isNotEmpty())
                    result = calculate(_expression)
            }
            is CalculatorEvent.CalculatorNum -> {
                when {
                    _expression.isEmpty() -> {
                        _expression.add(event.num.toString())
                    }
                    isOperator(_expression.last()) -> {
                        _expression.add(event.num.toString())
                    }
                    else -> {
                        val last = _expression.removeLast()
                        _expression.add(last + event.num)
                    }
                }
            }
            CalculatorEvent.Clear -> {
                _expression.clear()
                result = null
            }
            CalculatorEvent.Decimal -> {
                when {
                    _expression.isEmpty() || isOperator(_expression.last()) -> return
                    result != null -> {
                        _expression.clear()
                        _expression.add(
                            if (!result.toString().contains("."))
                                "${result}."
                            else
                                result.toString()
                        )
                        result = null
                    }
                    !_expression.last().contains(".") -> {
                        val last = _expression.removeLast()
                        _expression.add("$last.")
                    }
                }
            }
            CalculatorEvent.Delete -> {
                when {
                    _expression.isEmpty() -> return
                    isOperator(_expression.last()) -> _expression.removeLast()
                    else -> {
                        val last = _expression.removeLast()
                        _expression.add(last.dropLast(1))
                    }
                }
            }
            is CalculatorEvent.Operation -> {
                when {
                    _expression.isEmpty() -> {
                        // Case for first negative number
                        _expression.add("-1")
                        _expression.add(CalculatorOperation.Multiply.symbol)
                    }
                    isOperator(_expression.last()) -> {
                        _expression.removeLast()
                        _expression.add(event.operation.symbol)
                    }
                    result != null -> {
                        _expression.clear()
                        _expression.add(result.toString())
                        _expression.add(event.operation.symbol)
                        result = null
                    }
                    else -> {
                        _expression.add(event.operation.symbol)
                    }
                }
            }
            CalculatorEvent.Percentage -> {
                if (_expression.isEmpty() || isOperator(_expression.last()))
                    return
                val last = _expression.removeLast()
                val new = BigDecimal(last).divide(BigDecimal(100))
                _expression.add(new.toString())
            }
        }
    }

    private fun isOperator(char: String): Boolean {
        return CalculatorOperation.values().map { it.symbol }.contains(char)
    }

    private fun precedence(operator: String): Int {
        return when (operator) {
            CalculatorOperation.Add.symbol, CalculatorOperation.Subtract.symbol -> 1
            CalculatorOperation.Multiply.symbol, CalculatorOperation.Divide.symbol -> 2
            else -> -1
        }
    }

    private fun infixToPostfix(exp: MutableList<String>): MutableList<String> {
        val result: MutableList<String> = mutableListOf()
        val stack = Stack<String>()
        for (element in exp) {
            if (!isOperator(element))
                result.add(element)
            else {
                while (!stack.isEmpty() && precedence(element) <= precedence(stack.peek())) {
                    result += stack.peek()
                    stack.pop()
                }
                stack.push(element)
            }
        }
        while (!stack.isEmpty()) {
            result += stack.peek()
            stack.pop()
        }
        return result
    }

    private fun evaluatePostfix(exp: MutableList<String>): BigDecimal {
        val stack = Stack<BigDecimal>()
        for (element in exp) {
            if (!isOperator(element))
                stack.push(BigDecimal(element))
            else {
                val val1 = stack.pop()
                val val2 = stack.pop()
                when (element) {
                    CalculatorOperation.Add.symbol -> stack.push(val2 + val1)
                    CalculatorOperation.Subtract.symbol -> stack.push(val2 - val1)
                    CalculatorOperation.Divide.symbol -> stack.push(val2 / val1)
                    CalculatorOperation.Multiply.symbol -> stack.push(val2 * val1)
                }
            }
        }
        return stack.pop()
    }

    private fun calculate(statement: List<String>): BigDecimal? {
        return try {
             val postfix = infixToPostfix(statement.toMutableList())
              evaluatePostfix(postfix)
        }
        catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }
}
