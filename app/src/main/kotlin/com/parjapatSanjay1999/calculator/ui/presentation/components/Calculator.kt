package com.parjapatSanjay1999.calculator.ui.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parjapatSanjay1999.calculator.data.CalculationEntity
import com.parjapatSanjay1999.calculator.ui.theme.Black
import com.parjapatSanjay1999.calculator.ui.theme.Grey
import com.parjapatSanjay1999.calculator.ui.theme.Orange
import java.math.BigDecimal

private const val TAG = "Calculator"

@Composable
fun Calculator(
    modifier: Modifier = Modifier,
    calculationHistory: List<CalculationEntity>,
    expression: List<String>,
    result: BigDecimal?,
    isButtonsVisible: Boolean,
    onEvent: (CalculatorEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        val listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Bottom),
            state = listState
        ) {
            val historyItemTextColors = Grey
            items(calculationHistory) {
                Text(
                    text = it.expr.joinToString(""),
                    color = historyItemTextColors,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "= ${it.res}",
                    color = historyItemTextColors,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            val textColor = Black
            item {
                Column(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = Orange,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(5.dp)
                ) {
                    val text = expression.joinToString(separator = "")
                    Text(
                        text = text,
                        color = textColor,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                    result?.let { res ->
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "= $res",
                            color = textColor,
                            fontSize = 35.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        LaunchedEffect(key1 = calculationHistory, key2 = isButtonsVisible) {
            if (calculationHistory.isNotEmpty())
                listState.scrollToItem(calculationHistory.lastIndex)
        }
        AnimatedVisibility(visible = isButtonsVisible) {
            CalculatorButtons(onEvent = onEvent)
        }
    }
}

@Composable
private fun CalculatorButtons(modifier: Modifier = Modifier, onEvent: (CalculatorEvent) -> Unit) {
    val operationColor = Orange
    val numberTextColor = Black
    val aspectRatio = 1.3f
    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CalculatorButton(
                symbol = "AC",
                textColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.Clear)
            }
            CalculatorButton(
                painter = rememberVectorPainter(image = Icons.Outlined.Backspace),
                tintColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.Delete)
            }
            CalculatorButton(
                painter = rememberVectorPainter(image = Icons.Outlined.Percent),
                tintColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.Percentage)
            }
            CalculatorButton(
                symbol = "/",
                textColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.Operation(CalculatorOperation.Divide))
            }
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CalculatorButton(
                symbol = "7", textColor = numberTextColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(7))
            }
            CalculatorButton(
                symbol = "8", textColor = numberTextColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(8))
            }
            CalculatorButton(
                symbol = "9", textColor = numberTextColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(9))
            }
            CalculatorButton(
                painter = rememberVectorPainter(image = Icons.Outlined.Close),
                tintColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.Operation(CalculatorOperation.Multiply))
            }
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CalculatorButton(
                symbol = "4", textColor = numberTextColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(4))
            }
            CalculatorButton(
                symbol = "5", textColor = numberTextColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(5))
            }
            CalculatorButton(
                symbol = "6", textColor = numberTextColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(6))
            }
            CalculatorButton(
                painter = rememberVectorPainter(image = Icons.Outlined.Remove),
                tintColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.Operation(CalculatorOperation.Subtract))
            }
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CalculatorButton(
                symbol = "1", textColor = numberTextColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(1))
            }
            CalculatorButton(
                symbol = "2", textColor = numberTextColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(2))
            }
            CalculatorButton(
                symbol = "3", textColor = numberTextColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(3))
            }
            CalculatorButton(
                painter = rememberVectorPainter(image = Icons.Outlined.Add),
                tintColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.Operation(CalculatorOperation.Add))
            }
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CalculatorButton(
                symbol = "0",
                textColor = numberTextColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(ratio = 1f)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(0))
            }
            CalculatorButton(
                painter = rememberVectorPainter(image = Icons.Filled.FiberManualRecord),
                tintColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
                    .padding(17.dp)
            ) {
                onEvent(CalculatorEvent.Decimal)
            }

            CalculatorButton(
                symbol = "+/-",
                textColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.ChangeSign)
            }
            CalculatorButton(
                painter = rememberVectorPainter(image = Icons.Filled.Calculate),
                tintColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(aspectRatio)
            ) {
                onEvent(CalculatorEvent.Calculate)
            }
        }
    }
}
