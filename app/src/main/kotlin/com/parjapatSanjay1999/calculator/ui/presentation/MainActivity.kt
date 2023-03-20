@file:OptIn(ExperimentalAnimationApi::class)

package com.parjapatSanjay1999.calculator.ui.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.parjapatSanjay1999.calculator.ui.presentation.components.CalculatorScreen
import com.parjapatSanjay1999.calculator.ui.theme.CalculatorTheme
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                val vm by viewModels<CalculatorViewModel>()
                val calculationHistory by vm.prevCalculations.collectAsState(initial = emptyList())
                CalculatorScreen(
                    calculationHistory = calculationHistory,
                    isShowingHistory = vm.isShowingHistory,
                    expression = vm.expression,
                    result = vm.result,
                    onEvent = vm::onEvent
                )
            }
        }
    }
}
