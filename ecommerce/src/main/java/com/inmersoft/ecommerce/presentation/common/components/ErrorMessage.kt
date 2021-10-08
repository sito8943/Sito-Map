package com.inmersoft.ecommerce.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inmersoft.printful_api.common.ResourcesError


@Composable
fun ErrorMessage(dataResultError: ResourcesError, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = dataResultError.errorType.toString(),
            color = MaterialTheme.colors.error,
            textAlign = TextAlign.Center,

            )
        Text(
            text = dataResultError.message,
            color = MaterialTheme.colors.error,
            textAlign = TextAlign.Center,

            )
        Button(onClick = { onClick() }) {
            Icon(Icons.Filled.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Recargar")
        }
    }
}