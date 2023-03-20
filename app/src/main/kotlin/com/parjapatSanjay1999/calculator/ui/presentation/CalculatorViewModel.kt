package com.parjapatSanjay1999.calculator.ui.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parjapatSanjay1999.calculator.data.CalculationEntity
import com.parjapatSanjay1999.calculator.data.CalculatorDao
import com.parjapatSanjay1999.calculator.ui.presentation.components.CalculatorEvent
import com.parjapatSanjay1999.calculator.ui.presentation.components.CalculatorOperation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject

private const val TAG = "CalculatorViewModel"

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val db: CalculatorDao
) : ViewModel() {

    var result by mutableStateOf<BigDecimal?>(null)
        private set
    private val _expression = mutableStateListOf<String>()
    val expression = _expression

    var isShowingHistory by mutableStateOf(false)
        private set

    val prevCalculations = db.getAllCalculations()

    fun onEvent(event: CalculatorEvent) {
        when (event) {
            CalculatorEvent.Calculate -> {
                if (_expression.isNotEmpty())
                    result = calculate(_expression)?.also { res ->
                        viewModelScope.launch(Dispatchers.IO) {
                            db.insertCalculation(CalculationEntity(expr = expression, res = res))
                        }
                    }
            }
            is CalculatorEvent.CalculatorNum -> {
                when {
                    _expression.isEmpty() -> {
                        _expression.add(event.num.toString())
                    }
                    isOperator(_expression.last()) -> {
                        _expression.add(event.num.toString())
                    }
                    result != null -> {
                        _expression.clear()
                        result = null
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
                        val new = last.dropLast(1)
                        if (new.isNotBlank())
                            _expression.add(new)
                    }
                }
            }
            is CalculatorEvent.Operation -> {
                when {
                    _expression.isEmpty() -> {
                        // Case for first negative number
                        if (event.operation == CalculatorOperation.Subtract) {
                            _expression.add("-1")
                            _expression.add(CalculatorOperation.Multiply.symbol)
                        }
                    }
                    isOperator(_expression.last()) -> {
                        _expression.removeLast()
                        _expression.add(event.operation.symbol)
                    }
                    result != null -> {
                        result?.let { res ->
                            viewModelScope.launch(Dispatchers.IO) {
                                db.insertCalculation(
                                    CalculationEntity(
                                        expr = expression,
                                        res = res
                                    )
                                )
                                _expression.clear()
                                _expression.add(result.toString())
                                _expression.add(event.operation.symbol)
                                result = null
                            }
                        }
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
            CalculatorEvent.ChangeSign -> {
                when {
                    expression.isEmpty() || isOperator(expression.last()) -> return
                    result != null -> {
                        _expression.clear()
                        _expression.add(result!!.multiply(BigDecimal(-1)).toString())
                        result = null
                    }
                    else -> {
                        val last = BigDecimal(expression.removeLast())
                        _expression.add(last.multiply(BigDecimal(-1)).toString())
                    }
                }
            }
            CalculatorEvent.ToggleShowHistory -> {
                isShowingHistory = !isShowingHistory
            }
            CalculatorEvent.ClearHistory -> {
                viewModelScope.launch(Dispatchers.IO) {
                    db.clearAllCalculations()
                    onEvent(CalculatorEvent.ToggleShowHistory)
                }
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
                    CalculatorOperation.Add.symbol -> stack.push(val2.add(val1))
                    CalculatorOperation.Subtract.symbol -> stack.push(val2.subtract(val1))
                    CalculatorOperation.Divide.symbol -> stack.push(
                        val2.divide(
                            val1,
                            4,
                            RoundingMode.CEILING
                        )
                    )
                    CalculatorOperation.Multiply.symbol -> stack.push(val2.multiply(val1))
                }
            }
        }
        return stack.pop()
    }

    private fun calculate(statement: List<String>): BigDecimal? {
        return try {
            val postfix = infixToPostfix(statement.toMutableList())
            evaluatePostfix(postfix)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }
}
