package com.inmersoft.ecommerce.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun ForegroundGradientEffect(
    vertical: Boolean = true,
    start: Float = 0.75f,
    middle: Float = 0f,
    end: Float = 1f,
    startColor: Color = Color.Transparent,
    middleColor: Color = Color.Transparent,
    endColor: Color = MaterialTheme.colors.background
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (vertical)
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            Pair(start, startColor),
                            Pair(middle, middleColor),
                            Pair(end, endColor)
                        )
                    )
                else
                    Brush.horizontalGradient(
                        colorStops = arrayOf(
                            Pair(start, startColor),
                            Pair(middle, middleColor),
                            Pair(end, endColor)
                        )
                    )
            )
    )
}