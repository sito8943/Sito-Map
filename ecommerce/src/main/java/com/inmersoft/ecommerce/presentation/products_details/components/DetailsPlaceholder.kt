package com.inmersoft.ecommerce.presentation.products_details.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.inmersoft.ecommerce.presentation.common.AppBarExpendedHeight
import com.inmersoft.ecommerce.presentation.ui.theme.EcommerceTheme

@Composable
fun DetailsPlaceholder() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppBarExpendedHeight)
                .padding(8.dp)
                .clip(RoundedCornerShape(20.dp))
                .placeholder(
                    color = MaterialTheme.colors.surface,
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
                )
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(3) {
                Box(
                    modifier = Modifier
                        .height(175.dp)
                        .width(120.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .placeholder(
                            color = MaterialTheme.colors.surface,
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPlaceholder() {
    EcommerceTheme(darkTheme = true) {
        DetailsPlaceholder()
    }
}

