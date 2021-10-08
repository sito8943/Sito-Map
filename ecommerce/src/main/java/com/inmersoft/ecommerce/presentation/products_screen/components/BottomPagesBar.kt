package com.inmersoft.ecommerce.presentation.products_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inmersoft.ecommerce.presentation.ui.theme.EcommerceTheme

@Composable
fun BottomPagesBar(
    modifier: Modifier = Modifier,
    nextOnclick: () -> Unit,
    previusOnclick: () -> Unit,
    isFirstPage: Boolean = false,
    isLastPage: Boolean = true
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.size(54.dp),
            enabled = isFirstPage,
            onClick = { previusOnclick() }) {
            Icon(Icons.Filled.ArrowBackIos, contentDescription = null)
        }

        IconButton(
            modifier = Modifier.size(54.dp),
            enabled = isLastPage,
            onClick = { nextOnclick() }) {
            Icon(Icons.Filled.ArrowForwardIos, contentDescription = null)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BottonPagesBarPreview() {
    EcommerceTheme() {
        val state by remember { mutableStateOf("Test") }
        BottomPagesBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            nextOnclick = {},
            previusOnclick = {}
        )
    }
}
