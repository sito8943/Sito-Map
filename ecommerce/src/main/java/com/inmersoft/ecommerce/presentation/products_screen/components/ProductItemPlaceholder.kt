package com.inmersoft.ecommerce.presentation.products_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer


@Composable
fun ProductItemPlaceholder() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(20.dp))
                .placeholder(
                    color = MaterialTheme.colors.surface,
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
                )
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "asdasdasd",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .placeholder(
                    color = MaterialTheme.colors.surface,
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)

                ),
            style = MaterialTheme.typography.body2
        )
    }
}

