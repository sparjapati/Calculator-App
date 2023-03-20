package com.parjapatSanjay1999.calculator.ui.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parjapatSanjay1999.calculator.data.CalculationEntity
import java.math.BigDecimal

private const val TAG = "Calculator"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    calculationHistory: List<CalculationEntity>,
    isShowingHistory: Boolean,
    expression: List<String>,
    result: BigDecimal?,
    onEvent: (CalculatorEvent) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 5.dp)
    ) {
        val angle by animateFloatAsState(
            targetValue = if (isShowingHistory) -45f else 0f
        )
        val primaryColor = MaterialTheme.colorScheme.primary
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            val listState = rememberLazyListState()
            val textColor = MaterialTheme.colorScheme.onBackground
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Bottom),
                state = listState
            ) {
                items(calculationHistory) {
                    Text(
                        text = it.expr.joinToString(""),
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "= ${it.res}",
                        color = textColor,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (!isShowingHistory) {
                    item {
                        Column(
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary,
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
            }
            LaunchedEffect(key1 = calculationHistory, key2 = isShowingHistory) {
                if (calculationHistory.isNotEmpty()) listState.scrollToItem(calculationHistory.lastIndex)
            }
            AnimatedVisibility(visible = !isShowingHistory) {
                CalculatorButtons(
                    modifier = Modifier.animateEnterExit(
                        enter = slideInVertically(), exit = slideOutVertically()
                    ), onEvent = onEvent
                )
            }
        }
        AnimatedVisibility(
            visible = isShowingHistory,
            modifier = Modifier
                .clickable {
                    onEvent(CalculatorEvent.ClearHistory)
                }
                .padding(bottom = 10.dp)
                .align(Alignment.BottomCenter),
            enter = slideInVertically(),
            exit = slideOutVertically()) {
            Icon(
                imageVector = Icons.Outlined.ClearAll,
                contentDescription = "Clear history",
                tint = primaryColor,
            )
        }
        Icon(imageVector = Icons.Outlined.Update,
            contentDescription = "Show history",
            tint = primaryColor,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .rotate(angle)
                .clickable {
                    onEvent(CalculatorEvent.ToggleShowHistory)
                }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp)
        )
    }
}

@Composable
private fun CalculatorButtons(modifier: Modifier = Modifier, onEvent: (CalculatorEvent) -> Unit) {
    val operationColor = MaterialTheme.colorScheme.primary
    val numberTextColor = MaterialTheme.colorScheme.onBackground
    val buttonsAspectRatio = 1.3f
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
                    .aspectRatio(buttonsAspectRatio)
            ) {
                onEvent(CalculatorEvent.Clear)
            }
            CalculatorButton(
                painter = rememberVectorPainter(image = Icons.Outlined.Backspace),
                tintColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
            ) {
                onEvent(CalculatorEvent.Delete)
            }
            CalculatorButton(
                painter = rememberVectorPainter(image = Icons.Outlined.Percent),
                tintColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
            ) {
                onEvent(CalculatorEvent.Percentage)
            }
            CalculatorButton(
                symbol = "/",
                textColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
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
                symbol = "7",
                textColor = numberTextColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(7))
            }
            CalculatorButton(
                symbol = "8",
                textColor = numberTextColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(8))
            }
            CalculatorButton(
                symbol = "9",
                textColor = numberTextColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(9))
            }
            CalculatorButton(
                painter = rememberVectorPainter(image = Icons.Outlined.Close),
                tintColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
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
                symbol = "4",
                textColor = numberTextColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(4))
            }
            CalculatorButton(
                symbol = "5",
                textColor = numberTextColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(5))
            }
            CalculatorButton(
                symbol = "6",
                textColor = numberTextColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(6))
            }
            CalculatorButton(
                painter = rememberVectorPainter(image = Icons.Outlined.Remove),
                tintColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
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
                symbol = "1",
                textColor = numberTextColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(1))
            }
            CalculatorButton(
                symbol = "2",
                textColor = numberTextColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(2))
            }
            CalculatorButton(
                symbol = "3",
                textColor = numberTextColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
            ) {
                onEvent(CalculatorEvent.CalculatorNum(3))
            }
            CalculatorButton(
                painter = rememberVectorPainter(image = Icons.Outlined.Add),
                tintColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
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
                    .aspectRatio(buttonsAspectRatio)
                    .padding(17.dp)
            ) {
                onEvent(CalculatorEvent.Decimal)
            }

            CalculatorButton(
                symbol = "+/-",
                textColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
            ) {
                onEvent(CalculatorEvent.ChangeSign)
            }
            CalculatorButton(
                painter = rememberVectorPainter(image = Icons.Filled.Calculate),
                tintColor = operationColor,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(buttonsAspectRatio)
            ) {
                onEvent(CalculatorEvent.Calculate)
            }
        }
    }
}
