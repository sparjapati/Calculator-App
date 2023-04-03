package com.parjapatSanjay1999.calculator.ui.presentation

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parjapatSanjay1999.calculator.data.db.CalculationEntity
import com.parjapatSanjay1999.calculator.domain.CalculatorRepository
import com.parjapatSanjay1999.calculator.ui.presentation.components.CalculatorEvent
import com.parjapatSanjay1999.calculator.ui.presentation.components.CalculatorOperation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject

private const val TAG = "CalculatorViewModel"

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, private val repository: CalculatorRepository
) : ViewModel() {
    companion object {
        private const val KEY_CALCULATOR_STATE = "CalculatorViewModel.calculatorState"
    }

    private val _state = MutableStateFlow(
        value = savedStateHandle[KEY_CALCULATOR_STATE] ?: CalculatorState()
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPreviousCalculations().collectLatest {
                updateState(state.value.copy(history = it))
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUnCalculatedExpressions().collectLatest {
                updateState(state.value.copy(expression = it))
            }
        }
    }

    private fun updateState(state: CalculatorState) {
        _state.update{
            state
        }
        savedStateHandle[KEY_CALCULATOR_STATE] = state
        val caller = Throwable().stackTrace[1].methodName
        Log.d(TAG, "$caller saved state = $state")
    }

    fun onEvent(event: CalculatorEvent) {
        val expression = state.value.expression.toMutableList()
        var result = state.value.result
        var isShowingHistory = state.value.isShowingHistory
        when (event) {
            CalculatorEvent.Calculate -> {
                if (expression.isNotEmpty()) result = calculate(expression)?.also { res ->
                    viewModelScope.launch(Dispatchers.IO) {
                        repository.saveCalculation(
                            CalculationEntity(
                                expr = expression, res = res
                            )
                        )
                        repository.saveUnCalculatedExpression(emptyList())
                        result = null
                        updateState(state.value.copy(result = res))
                    }
                }
            }
            is CalculatorEvent.CalculatorNum -> {
                when {
                    expression.isEmpty() -> {
                        expression.add(event.num.toString())
                    }
                    isOperator(expression.last()) -> {
                        expression.add(event.num.toString())
                    }
                    result != null -> {
                        expression.clear()
                        result = null
                        expression.add(event.num.toString())
                    }
                    else -> {
                        val last = expression.removeLast()
                        expression.add(last + event.num)
                    }
                }
            }
            CalculatorEvent.Clear -> {
                expression.clear()
                result = null
            }
            CalculatorEvent.Decimal -> {
                when {
                    expression.isEmpty() || isOperator(expression.last()) -> return
                    result != null -> {
                        expression.clear()
                        expression.add(
                            if (!result.toString().contains(".")) "${result}."
                            else result.toString()
                        )
                        result = null
                    }
                    !expression.last().contains(".") -> {
                        val last = expression.removeLast()
                        expression.add("$last.")
                    }
                }
            }
            CalculatorEvent.Delete -> {
                when {
                    expression.isEmpty() -> return
                    isOperator(expression.last()) -> expression.removeLast()
                    else -> {
                        val last = expression.removeLast()
                        val new = last.dropLast(1)
                        if (new.isNotBlank()) expression.add(new)
                    }
                }
            }
            is CalculatorEvent.Operation -> {
                when {
                    expression.isEmpty() -> { // Case for first negative number
                        if (event.operation == CalculatorOperation.Subtract) {
                            expression.add("-1")
                            expression.add(CalculatorOperation.Multiply.symbol)
                        }
                    }
                    isOperator(expression.last()) -> {
                        expression.removeLast()
                        expression.add(event.operation.symbol)
                    }
                    result != null -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            repository.saveCalculation(
                                CalculationEntity(
                                    expr = expression, res = result!!
                                )
                            )
                        }
                    }
                    else -> {
                        expression.add(event.operation.symbol)
                    }
                }
            }
            CalculatorEvent.Percentage -> {
                if (expression.isEmpty() || isOperator(expression.last())) return
                val last = expression.removeLast()
                val new = BigDecimal(last).divide(BigDecimal(100))
                expression.add(new.toString())
            }
            CalculatorEvent.ChangeSign -> {
                when {
                    expression.isEmpty() || isOperator(expression.last()) -> return
                    result != null -> {
                        expression.clear()
                        expression.add(result!!.multiply(BigDecimal(-1)).toString())
                        result = null
                    }
                    else -> {
                        val last = BigDecimal(expression.removeLast())
                        expression.add(last.multiply(BigDecimal(-1)).toString())
                    }
                }
            }
            CalculatorEvent.ToggleShowHistory -> {
                isShowingHistory = !isShowingHistory
            }
            CalculatorEvent.ClearHistory -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.clearCalculationHistory()
                    onEvent(CalculatorEvent.ToggleShowHistory)
                }
            }
        }
        updateState(
            state.value.copy(
                expression = expression, result = result, isShowingHistory = isShowingHistory
            )
        )
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUnCalculatedExpression(expression)
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
            if (!isOperator(element)) result.add(element)
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
            if (!isOperator(element)) stack.push(BigDecimal(element))
            else {
                val val1 = stack.pop()
                val val2 = stack.pop()
                when (element) {
                    CalculatorOperation.Add.symbol -> stack.push(val2.add(val1))
                    CalculatorOperation.Subtract.symbol -> stack.push(val2.subtract(val1))
                    CalculatorOperation.Divide.symbol -> stack.push(
                        val2.divide(
                            val1, 4, RoundingMode.CEILING
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

@Parcelize
data class CalculatorState(
    val expression: List<String> = emptyList(),
    val result: BigDecimal? = null,
    val isShowingHistory: Boolean = false,
    val history: List<CalculationEntity> = emptyList()
) : Parcelable
