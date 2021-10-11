package com.inmersoft.ecommerce.presentation.common.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape


@Composable
fun CircularButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable RowScope.() -> Unit,
    color: Color = Color.Gray,
    backgroundColor: Color = Color.White,
    shape: Shape = CircleShape,
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    onClick: () -> Unit = {}
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        contentPadding = PaddingValues(),
        shape = shape,
        elevation = elevation,
        modifier = modifier,
        content = icon
    )
}