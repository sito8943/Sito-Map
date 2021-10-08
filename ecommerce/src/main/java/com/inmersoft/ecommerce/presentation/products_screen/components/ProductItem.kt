package com.inmersoft.ecommerce.presentation.products_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.inmersoft.printful_api.data.remote.dto.common.ProductData


@ExperimentalCoilApi
@Composable
fun ProductItem(
    resultItem: ProductData,
    isFavorite: Boolean,
    favoriteOnClickEvent: () -> Unit,
    onClick: (productId: String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        val painter =
            rememberImagePainter(data = resultItem.thumbnail_url, builder = {
                crossfade(true)
            })
        val state = painter.state is ImagePainter.State.Loading
        Box {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .placeholder(
                        color = MaterialTheme.colors.surface,
                        visible = state,
                        highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
                    )
                    .clickable { onClick(resultItem.id.toString()) }
            )

            IconButton(
                modifier = Modifier
                    .padding(16.dp)
                    .size(42.dp)
                    .align(Alignment.BottomEnd),
                onClick = {
                    favoriteOnClickEvent()
                }
            ) {
                Icon(
                    if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            text = resultItem.name,
            style = MaterialTheme.typography.body2
        )
    }
}
