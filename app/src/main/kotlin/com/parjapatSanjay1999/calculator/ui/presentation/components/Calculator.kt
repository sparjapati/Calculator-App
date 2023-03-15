package com.parjapatSanjay1999.calculator.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parjapatSanjay1999.calculator.R.drawable
import com.parjapatSanjay1999.calculator.ui.theme.Orange
import java.math.BigDecimal

private const val TAG = "Calculator"

@Composable
fun Calculator(expression: List<String>, result: BigDecimal?, onEvent: (CalculatorEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Bottom
        ) {
            val text = expression.joinToString(separator = "")
            Text(
                text = text,
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            result?.let { res ->
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "= $res",
                    color = Color.Black,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
        CalculatorButtons(onEvent = onEvent)
    }
}

@Composable
private fun CalculatorButtons(modifier: Modifier = Modifier, onEvent: (CalculatorEvent) -> Unit) {
    val operatorButtonBackgroundColor = Color(0xFFEBB8A8)
    val textColor = Color.Black
    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            CalculatorButton(
                symbol = "AC",
                textColor = textColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(operatorButtonBackgroundColor)
            ) {
                onEvent(CalculatorEvent.Clear)
            }
            CalculatorButton(
                symbolResId = drawable.ic_delete,
                tintColor = textColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(operatorButtonBackgroundColor)
            ) {
                onEvent(CalculatorEvent.Delete)
            }
            CalculatorButton(
                symbol = "%",
                textColor = textColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(operatorButtonBackgroundColor)
            ) {
                onEvent(CalculatorEvent.Percentage)
            }
            CalculatorButton(
                symbol = "/",
                textColor = textColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(operatorButtonBackgroundColor)
            ) {
                onEvent(CalculatorEvent.Operation(CalculatorOperation.Divide))
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            CalculatorButton(
                symbol = "7", textColor = textColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(7))
            }
            CalculatorButton(
                symbol = "8", textColor = textColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(8))
            }
            CalculatorButton(
                symbol = "9", textColor = textColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(9))
            }
            CalculatorButton(
                symbol = "*",
                textColor = textColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(operatorButtonBackgroundColor)
            ) {
                onEvent(CalculatorEvent.Operation(CalculatorOperation.Multiply))
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            CalculatorButton(
                symbol = "4", textColor = textColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(4))
            }
            CalculatorButton(
                symbol = "5", textColor = textColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(5))
            }
            CalculatorButton(
                symbol = "6", textColor = textColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(6))
            }
            CalculatorButton(
                symbol = "--",
                textColor = textColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(operatorButtonBackgroundColor)
            ) {
                onEvent(CalculatorEvent.Operation(CalculatorOperation.Subtract))
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            CalculatorButton(
                symbol = "1", textColor = textColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(1))
            }
            CalculatorButton(
                symbol = "2", textColor = textColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(2))
            }
            CalculatorButton(
                symbol = "3", textColor = textColor, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(3))
            }
            CalculatorButton(
                symbol = "+",
                textColor = textColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(operatorButtonBackgroundColor)
            ) {
                onEvent(CalculatorEvent.Operation(CalculatorOperation.Add))
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            CalculatorButton(
                symbol = "0",
                textColor = textColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(ratio = 1f)
                    .clip(CircleShape)
                    .background(operatorButtonBackgroundColor)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(0))
            }
            CalculatorButton(
                symbol = ".",
                textColor = textColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(operatorButtonBackgroundColor)
            ) {
                onEvent(CalculatorEvent.Decimal)
            }
            CalculatorButton(
                symbol = "=",
                textColor = textColor,
                modifier = Modifier
                    .weight(2f)
                    .aspectRatio(2f)
                    .clip(CircleShape)
                    .background(Orange)
            ) {
                onEvent(CalculatorEvent.Calculate)
            }
        }
    }
}
