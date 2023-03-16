package com.parjapatSanjay1999.calculator.ui.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parjapatSanjay1999.calculator.R
import com.parjapatSanjay1999.calculator.ui.theme.Orange

@Composable
fun CalculatorButton(
    symbol: String,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable {
                onClick()
            }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            color = textColor,
            fontSize = 20.sp
        )
    }
}


@Composable
fun CalculatorButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    tintColor: Color,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable {
                onClick()
            }
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            colorFilter = ColorFilter.tint(tintColor),
            contentDescription = contentDescription,
            contentScale = ContentScale.Inside
        )
    }
}
