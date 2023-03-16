package com.parjapatSanjay1999.calculator.ui.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.parjapatSanjay1999.calculator.ui.presentation.components.Calculator
import com.parjapatSanjay1999.calculator.ui.presentation.components.CalculatorEvent
import com.parjapatSanjay1999.calculator.ui.theme.Black
import com.parjapatSanjay1999.calculator.ui.theme.CalculatorTheme
import com.parjapatSanjay1999.calculator.ui.theme.Orange
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                val vm by viewModels<CalculatorViewModel>()
                val calculationHistory by vm.prevCalculations.collectAsState(initial = emptyList())
                val isButtonVisible = vm.isButtonsVisible
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Orange),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Calculator", color = Black)
                            },
                            actions = {
                                val angle by animateFloatAsState(
                                    targetValue = if (isButtonVisible) -45f else 0f
                                )
                                IconButton(onClick = { vm.onEvent(CalculatorEvent.ToggleButtons) }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Refresh,
                                        contentDescription = "Show history",
                                        tint = Orange,
                                        modifier = Modifier
                                            .rotate(angle)
                                            .padding(10.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                IconButton(onClick = { vm.onEvent(CalculatorEvent.ClearHistory) }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "Clear history",
                                        tint = Orange,
                                        modifier = Modifier
                                            .padding(10.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        )
                    },
                ) {
                    Calculator(
                        modifier = Modifier.padding(it),
                        calculationHistory = calculationHistory,
                        expression = vm.expression,
                        result = vm.result,
                        isButtonsVisible = isButtonVisible,
                        onEvent = vm::onEvent
                    )
                }
            }
        }
    }
}
