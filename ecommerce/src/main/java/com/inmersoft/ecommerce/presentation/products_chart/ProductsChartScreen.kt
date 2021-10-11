package com.inmersoft.ecommerce.presentation.products_chart

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.inmersoft.ecommerce.R
import com.inmersoft.ecommerce.presentation.common.AppBarCollapsedHeight
import com.inmersoft.printful_api.data.local.model.ProductInCart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun ProductsChartScreen(
    navController: NavController,
    productsCartViewModel: ProductsCartViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = { ChartTopApp(navController = navController) },
        content = {
            ProductsInCart(
                productsCartViewModel,
                coroutineScope = scope,
                scaffoldState = scaffoldState
            )
        },
        snackbarHost = {
            SnackbarHost(it) { data ->
                // custom snackbar with the custom border
                Snackbar(
                    snackbarData = data
                )
            }
        }
    )
}

@ExperimentalAnimationApi
@Composable
fun ProductsInCart(
    productsCartViewModel: ProductsCartViewModel,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    val productsCartState =
        productsCartViewModel.productsInChart.collectAsState(initial = emptyList())
    val itemToDeleteState = remember { mutableStateOf<ProductInCart?>(null) }
    val deleteDialogState = remember { mutableStateOf(false) }
    val snackItemDeleteMessage: String = stringResource(id = R.string.snack_item_delete_message)
    val actionLevel = stringResource(id = R.string.cancel_action)
    var totalPrice by remember { mutableStateOf(0.0) }

    Log.d("ProductInCart", "ProductInCart COMPOSITION")

    val onShowSnackbar: (ProductInCart) -> Unit = { deletedItem ->
        productsCartViewModel.onDeleteProductInChart(deletedItem.variantId)
        coroutineScope.launch {
            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = "${deletedItem.name} $snackItemDeleteMessage",
                actionLabel = actionLevel,
                duration = SnackbarDuration.Short,
            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> Log.d("DeletedItem", "Snackbar dismissed")
                SnackbarResult.ActionPerformed -> productsCartViewModel.onInsertProductInChart(
                    deletedItem
                )
            }
        }
    }

    totalPrice = productsCartState.value.sumOf { it.retailPrice.toDouble() * it.cantInChart }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.9f)
                .padding(horizontal = 16.dp)
        ) {
            items(productsCartState.value) { currentItem ->
                ProductChartItem(
                    currentItem,
                    onUpdateEvent = { productChanged ->
                        totalPrice =
                            productsCartState.value.sumOf { it.retailPrice.toDouble() * it.cantInChart }
                        productsCartViewModel.onUpdateProductInChart(productChanged)
                    },
                    onDeleteItem = {
                        deleteDialogState.value = true
                        itemToDeleteState.value = it
                    }
                )
            }

        }
        BottomPaySheet(
            totalPrice, modifier = Modifier
                .fillMaxSize()
                .weight(0.4f)
        )
    }

    if (deleteDialogState.value) {
        AlertDialog(
            onDismissRequest = {
                deleteDialogState.value = true
            },
            title = {
                Row {
                    Icon(
                        imageVector = Icons.Filled.RemoveShoppingCart,
                        contentDescription = stringResource(id = R.string.delete_product_cart_icon)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = stringResource(id = R.string.delete_product_cart_title))
                }
            },
            text = {
                Text(stringResource(id = R.string.delete_product_in_cart_dialog_message))
            },
            confirmButton = {
                Button(
                    onClick = {
                        deleteDialogState.value = false
                        itemToDeleteState.value?.let { productItem ->
                            onShowSnackbar(productItem)
                        }
                    }) {
                    Text(stringResource(id = R.string.yes))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        itemToDeleteState.value = null
                        deleteDialogState.value = false
                    }) {
                    Text(stringResource(id = R.string.no))
                }
            }
        )
    }
}

@Composable
fun BottomPaySheet(totalPrice: Double, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = 8.dp,
        shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(textAlign = TextAlign.Start, text = "Total a pagar", fontSize = 18.sp)
                val finalResult = String.format("%.2f", totalPrice)
                Text(textAlign = TextAlign.End, text = "$$finalResult", fontSize = 22.sp)
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                onClick = {

                }) {
                Icon(Icons.Filled.Payment, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.create_order), fontSize = 18.sp)
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun ProductChartItem(
    currentItem: ProductInCart,
    onUpdateEvent: (ProductInCart) -> Unit,
    onDeleteItem: (ProductInCart) -> Unit
) {
    val imagePainter = rememberImagePainter(
        data = currentItem.variantPreviewUrl,
        builder = {
            crossfade(true)
        })

    val loadingImageState = imagePainter.state is ImagePainter.State.Loading

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Image(
            painter = imagePainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(20.dp))
                .placeholder(
                    color = MaterialTheme.colors.surface,
                    visible = loadingImageState,
                    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
                )
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(6f)
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = currentItem.name, overflow = TextOverflow.Ellipsis)
            Text(
                text = "${currentItem.retailPrice} ${currentItem.currency} ",
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle2
            )
            Row(
                modifier = Modifier.width(120.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                var countOfProductItem by remember { mutableStateOf(currentItem.cantInChart) }

                IconButton(
                    modifier = Modifier
                        .size(50.dp)
                        .weight(4f)
                        .padding(end = 4.dp),
                    onClick = {
                        if (countOfProductItem > 1) {
                            countOfProductItem--
                            currentItem.cantInChart = countOfProductItem
                            onUpdateEvent(currentItem)
                        }
                    }) {
                    Icon(
                        Icons.Filled.Remove,
                        contentDescription = null,
                    )

                }

                AnimatedContent(
                    modifier = Modifier.weight(4f),
                    targetState = countOfProductItem,
                    transitionSpec = {
                        // Compare the incoming number with the previous number.
                        if (targetState > initialState) {
                            // If the target number is larger, it slides up and fades in
                            // while the initial (smaller) number slides up and fades out.
                            slideInVertically(
                                initialOffsetY = { height -> height }) + fadeIn() with
                                    slideOutVertically(targetOffsetY = { height -> -height }) + fadeOut()
                        } else {
                            // If the target number is smaller, it slides down and fades in
                            // while the initial number slides down and fades out.
                            slideInVertically(initialOffsetY = { height -> -height }) + fadeIn() with
                                    slideOutVertically(targetOffsetY = { height -> height }) + fadeOut()
                        }.using(
                            // Disable clipping since the faded slide-in/out should
                            // be displayed out of bounds.
                            SizeTransform(clip = false)
                        )
                    }
                ) { targetCount ->
                    Text(
                        text = "$targetCount",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(2f)
                    )
                }

                IconButton(
                    modifier = Modifier
                        .size(50.dp)
                        .weight(4f)
                        .padding(end = 4.dp),
                    onClick = {
                        countOfProductItem++
                        currentItem.cantInChart = countOfProductItem
                        onUpdateEvent(currentItem)
                    }) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null,
                    )
                }
            }
        }
        IconButton(
            modifier = Modifier
                .size(42.dp)
                .weight(1f)
                .padding(end = 4.dp),
            onClick = {
                onDeleteItem(currentItem)
            }
        )
        { Icon(Icons.Filled.DeleteForever, contentDescription = null, tint = Color.Red) }
    }
}

@Composable
fun ChartTopApp(navController: NavController) {
    TopAppBar(
        contentPadding = PaddingValues(),
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .statusBarsPadding()
            .height(AppBarCollapsedHeight),
        elevation = 0.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            //Back Icon
            IconButton(
                modifier = Modifier.size(42.dp),
                onClick = { navController.popBackStack() }
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.cart_title),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

