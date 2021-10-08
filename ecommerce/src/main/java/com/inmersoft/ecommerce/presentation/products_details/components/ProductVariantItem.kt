package com.inmersoft.ecommerce.presentation.products_details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.inmersoft.ecommerce.R
import com.inmersoft.ecommerce.presentation.ui.theme.EcommerceTheme
import com.inmersoft.printful_api.data.remote.dto.details.SyncVariant


@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProductVariantItem(
    variantItem: SyncVariant,
    modifier: Modifier = Modifier,
    variantSelected: Long,
    isInCart: Boolean,
) {
    val painter = rememberImagePainter(
        data = variantItem.files[variantItem.files.size - 1].preview_url,
        builder = {
            crossfade(true)
            error(R.drawable.error_loading_image_placeholder)
        })

    val painterState = painter.state is ImagePainter.State.Loading
    val tmpName = variantItem.name.split('-')
    var variantName = variantItem.name
    if (tmpName.isNotEmpty()) {
        variantName = tmpName[tmpName.size - 1]
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = modifier.fillMaxSize()) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(175.dp)
                    .width(120.dp)
                    .border(
                        if (variantItem.id == variantSelected) 2.dp else 0.dp,
                        color = if (variantItem.id == variantSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .placeholder(
                        color = MaterialTheme.colors.surface,
                        visible = painterState,
                        highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
                    )
            )
            Text(
                text = variantName,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .align(Alignment.Start)

            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${variantItem.retail_price} ${variantItem.currency} ",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .align(Alignment.Start),
                style = MaterialTheme.typography.caption
            )
        }
        if (isInCart) {
            Box(
                modifier = modifier
                    .size(16.dp)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = stringResource(
                        id = R.string.added_in_cart
                    )
                )
            }
        }
    }

}

@Preview(showBackground = true)

@Composable
fun PreviewTest() {
    EcommerceTheme(darkTheme = true) {
        Column(modifier = Modifier.size(400.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colors.background)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colors.surface)
            )

        }
    }
}