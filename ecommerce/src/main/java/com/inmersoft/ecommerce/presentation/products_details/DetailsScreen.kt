package com.inmersoft.ecommerce.presentation.products_details

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.insets.navigationBarsPadding
import com.inmersoft.ecommerce.R
import com.inmersoft.ecommerce.presentation.products_details.components.DetailComponent
import com.inmersoft.ecommerce.presentation.products_details.components.DetailsPlaceholder
import com.inmersoft.ecommerce.presentation.products_details.components.DetailsTopBar
import com.inmersoft.printful_api.data.remote.dto.details.SyncVariant

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun DetailsScreen(
    navController: NavController,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val detailsState = viewModel.detailsProductState
    val productsInCart = viewModel.productsInChart.collectAsState(initial = emptyList())

    val stateAddToCartDialog = remember { mutableStateOf(false) }
    val stateProductInCartDialog = remember { mutableStateOf(false) }

    val variantToChartState = remember {
        mutableStateOf<SyncVariant?>(null)
    }
    val variantState = remember {
        mutableStateOf<SyncVariant?>(null)
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            if (detailsState.value.isLoading) {
                DetailsPlaceholder()
            } else {
                DetailsTopBar(
                    navController = navController,
                    variant = variantState.value,
                    viewModel=viewModel
                )

                Spacer(modifier = Modifier.height(20.dp))
                detailsState.value.products?.let { details ->
                    variantState.value = variantState.value ?: details.result.sync_variants[0]
                    variantToChartState.value = variantState.value

                    DetailComponent(
                        details = details,
                        variant = variantState,
                        productsInCart = productsInCart.value
                    )
                }

            }
        }


        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomEnd),
            visible = variantToChartState.value != null,
            enter = slideInVertically(
                initialOffsetY = { 50 }
            ) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { 50 }) + fadeOut()
        ) {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .padding(bottom = 16.dp, end = 16.dp)
                    .navigationBarsPadding(),
                text = { Text(text = stringResource(id = R.string.add_to_cart)) },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                onClick = {
                    variantToChartState.value?.let { currentVariant ->
                        if (productsInCart.value.none { productInCart -> productInCart.variantId == currentVariant.variant_id }) {
                            stateAddToCartDialog.value = true
                        } else {
                            stateProductInCartDialog.value = true
                        }
                    }
                },
                icon = {
                    Icon(Icons.Filled.AddShoppingCart, contentDescription = null)
                }
            )
        }

        if (stateAddToCartDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    stateAddToCartDialog.value = false
                },
                title = {
                    Row {
                        Icon(
                            imageVector = Icons.Filled.AddShoppingCart,
                            contentDescription = stringResource(id = R.string.add_to_cart)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = stringResource(id = R.string.add_to_cart_dialog_title))
                    }
                },
                text = {
                    Text(stringResource(id = R.string.add_to_cart_dialog_message))
                },
                confirmButton = {
                    Button(
                        onClick = {
                            stateAddToCartDialog.value = false
                            variantToChartState.value?.let {
                                viewModel.onAddToChart(it)
                            }
                        }) {
                        Text(stringResource(id = R.string.yes))
                    }
                },
                dismissButton = {
                    Button(

                        onClick = {
                            stateAddToCartDialog.value = false
                        }) {
                        Text(stringResource(id = R.string.no))
                    }
                }
            )
        }


        if (stateProductInCartDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    stateProductInCartDialog.value = false
                },
                title = {
                    Row {
                        Icon(
                            imageVector = Icons.Filled.AddShoppingCart,
                            contentDescription = stringResource(id = R.string.add_to_cart)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = stringResource(id = R.string.product_in_store_title))
                    }
                },
                text = {
                    Text(stringResource(id = R.string.product_in_store))
                },
                confirmButton = {
                    Button(
                        onClick = {
                            stateProductInCartDialog.value = false
                        }) {
                        Text(stringResource(id = R.string.ok))
                    }
                }
            )
        }
    }

}



