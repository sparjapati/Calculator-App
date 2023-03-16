package com.parjapatSanjay1999.calculator.ui.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.parjapatSanjay1999.calculator.ui.presentation.components.Calculator
import com.parjapatSanjay1999.calculator.ui.theme.Black
import com.parjapatSanjay1999.calculator.ui.theme.CalculatorTheme
import com.parjapatSanjay1999.calculator.ui.theme.Orange

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                val vm by viewModels<CalculatorViewModel>()
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
                                Image(
                                    painter = rememberVectorPainter(
                                        if (vm.isHistoryVisible) Icons.Outlined.Refresh
                                        else Icons.Outlined.Replay
                                    ),
                                    colorFilter = ColorFilter.tint(Orange),
                                    contentDescription = "Show history",
                                    modifier = Modifier
                                        .clickable {
                                            vm.toggleHistory()
                                        }
                                        .rotate(-45f)
                                        .padding(10.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        )
                    },
                ) {
                    Calculator(
                        modifier = Modifier.padding(it),
                        expression = vm.expression,
                        result = vm.result,
                        onEvent = vm::onEvent
                    )
                }
            }
        }
    }
}
