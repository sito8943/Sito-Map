package com.inmersoft.ecommerce.presentation.products_details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.inmersoft.ecommerce.R
import com.inmersoft.ecommerce.navigation.NavigationRoute
import com.inmersoft.ecommerce.presentation.common.AppBarCollapsedHeight
import com.inmersoft.ecommerce.presentation.common.AppBarExpendedHeight
import com.inmersoft.ecommerce.presentation.common.components.ForegroundGradientEffect
import com.inmersoft.ecommerce.presentation.products_details.DetailsViewModel
import com.inmersoft.printful_api.data.remote.dto.details.SyncVariant
import com.inmersoft.printful_api.data.remote.dto.details.toFavoriteProduct


@ExperimentalCoilApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailsTopBar(
    variant: SyncVariant?,
    navController: NavController,
    viewModel: DetailsViewModel
) {
    val imagePainter = rememberImagePainter(
        data = variant?.let { it.files[variant.files.size - 1].preview_url },
        builder = {
            crossfade(true)
            error(R.drawable.error_loading_image_placeholder)
        })

    val favoriteProductsState = viewModel.favoriteProducts.collectAsState(initial = emptyList())
    val isFavorite = remember { mutableStateOf(false) }

    val loadingImageState = imagePainter.state is ImagePainter.State.Loading

    TopAppBar(
        contentPadding = PaddingValues(),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .height(
                AppBarExpendedHeight
            ),
        elevation = 0.dp
    ) {
        Box(Modifier.fillMaxSize()) {
            Column {
                Box(
                    Modifier
                        .height(AppBarExpendedHeight)
                ) {
                    Image(
                        painter = imagePainter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .placeholder(
                                color = MaterialTheme.colors.surface,
                                visible = loadingImageState,
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
                            )
                    )

                    ForegroundGradientEffect()

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    ) {

                        variant?.let { syncVariant ->
                            Column(
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 8.dp)
                                    .weight(6f),

                                ) {

                                isFavorite.value =
                                    favoriteProductsState.value.filter { currentItem -> currentItem.productId == syncVariant.sync_product_id }
                                        .isNotEmpty()

                                var productName = syncVariant.product.name
                                var varianName = ""
                                val tmpName = syncVariant.name.split("-")
                                if (tmpName.isNotEmpty()) {
                                    productName = tmpName[0]
                                    varianName = tmpName[tmpName.size - 1].trim()
                                }
                                Text(
                                    text = productName,
                                    style = MaterialTheme.typography.h5,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = varianName,
                                    style = MaterialTheme.typography.subtitle2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }

                            Text(
                                text = "${syncVariant.retail_price} ${syncVariant.currency} ",
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .weight(3f)
                            )
                        }

                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(AppBarCollapsedHeight)
                    .padding(horizontal = 16.dp)
            ) {
                //Back Icon
                IconButton(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.surface.copy(alpha = 0.3f)),
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.weight(4f)) {
                    IconButton(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.surface.copy(alpha = 0.3f)),
                        onClick = { navController.navigate(NavigationRoute.ProductsChartScreen.route) },
                    ) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                    }
                    Spacer(Modifier.width(10.dp))

                    IconButton(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.surface.copy(alpha = 0.3f)),
                        onClick = {
                            variant?.let {currentItem->
                                if (!isFavorite.value)
                                    viewModel.onAddFavorite(currentItem.toFavoriteProduct())
                                else
                                    viewModel.onDeleteFavorite(currentItem.toFavoriteProduct())
                            }

                        }
                    ) {
                        Icon(
                            if (isFavorite.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                }

            }
        }

    }

}

