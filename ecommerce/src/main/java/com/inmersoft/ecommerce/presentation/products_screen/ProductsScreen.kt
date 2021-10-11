package com.inmersoft.ecommerce.presentation.products_screen

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.inmersoft.ecommerce.R
import com.inmersoft.ecommerce.navigation.NavigationRoute
import com.inmersoft.ecommerce.presentation.common.components.EcommerceTextField
import com.inmersoft.ecommerce.presentation.common.components.ErrorMessage
import com.inmersoft.ecommerce.presentation.products_screen.components.*
import com.inmersoft.printful_api.data.remote.dto.common.toFavoriteProduct
import kotlinx.coroutines.launch


@ExperimentalCoilApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    //GeneralStates
    val productsState = viewModel.productListState.value
    val favoriteProductsState = viewModel.favoriteProducts.collectAsState(initial = emptyList())
    val storeInfoState = viewModel.storeInfoState.value
    val listState = rememberLazyListState()

    val productSearchState = viewModel.productSearchListState.value
    val isRefreshing = productsState.isLoading
    val coroutineScope = rememberCoroutineScope()
    val searchTextState = remember { mutableStateOf(TextFieldValue()) }
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    val isNotLastPage = viewModel.isNotLastPageState
    val isNotFirstPage = viewModel.isNotFirstPageState

    Scaffold(
        modifier = modifier,
        topBar = {
            ProductsTopBar(
                storeInfoState.storeInfo,
                navController = navController
            )
        },
        content = {
            Divider()
            Box() {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {

                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        EcommerceTextField(
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = { viewModel.onSearchProducts(search = searchTextState.value.text) }
                            ),
                            value = searchTextState.value.text,
                            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                            onValueChange = { searchTextState.value = it },
                            modifier = Modifier.weight(6f),
                            placeholder = { Text(text = stringResource(id = R.string.search_text)) },
                            trailingIcon = {
                                if (searchTextState.value.text.isNotEmpty()) Icon(
                                    Icons.Filled.Close,
                                    modifier = Modifier.clickable {
                                        searchTextState.value = TextFieldValue()
                                        viewModel.clearSearchText()
                                    },
                                    contentDescription = null
                                )
                            })
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                    ) {
                        SwipeRefresh(
                            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                            onRefresh = { viewModel.getStartPage() },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            LazyVerticalGrid(
                                state = listState,
                                cells = GridCells.Adaptive(180.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                if (searchTextState.value.text.isNotEmpty()) {
                                    productSearchState.products?.let { productsFound ->
                                        items(productsFound.result) { resultItem ->
                                            val isFavorite =
                                                favoriteProductsState.value.any { it.productId == resultItem.id }
                                            ProductItem(
                                                resultItem,
                                                isFavorite = isFavorite,
                                                favoriteOnClickEvent = {
                                                    val favProduct = resultItem.toFavoriteProduct()
                                                    if (!isFavorite) {
                                                        viewModel.onAddFavorite(favProduct)
                                                    } else {
                                                        viewModel.onDeleteFavorite(favProduct)
                                                    }
                                                }
                                            ) { productId ->
                                                navController.navigate(NavigationRoute.ProductsDetailScreen.route + "/$productId")
                                            }
                                        }
                                    }
                                } else {
                                    productsState.products?.let { products ->
                                        items(products.result) { resultItem ->
                                            val isFavorite =
                                                favoriteProductsState.value.any { it.productId == resultItem.id }
                                            ProductItem(
                                                resultItem,
                                                isFavorite = isFavorite,
                                                favoriteOnClickEvent = {
                                                    val favProduct = resultItem.toFavoriteProduct()
                                                    if (!isFavorite) {
                                                        viewModel.onAddFavorite(favProduct)
                                                    } else {
                                                        viewModel.onDeleteFavorite(favProduct)
                                                    }
                                                }
                                            ) { productId ->
                                                navController.navigate(NavigationRoute.ProductsDetailScreen.route + "/$productId")
                                            }
                                        }
                                    }
                                }

                                if (productsState.isLoading) {
                                    items(10) {
                                        ProductItemPlaceholder()
                                    }
                                }

                                item {
                                    //Bottom separetor for the last itemsin the list dont stay behind bars
                                    Spacer(modifier = Modifier.height(120.dp))
                                }
                            }
                        }
                    }
                }

                if (productsState.error != null) {
                    ErrorMessage(productsState.error, onClick = { viewModel.getStartPage() })
                }


                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    visible = showButton,
                    enter = slideInVertically(
                        initialOffsetY = { 50 }
                    ) + fadeIn(),
                    exit = slideOutVertically(
                        targetOffsetY = { 50 }
                    ) + fadeOut()
                ) {
                    ScrollToTopButton(
                        modifier = Modifier.navigationBarsPadding(),
                        onClick = {
                            coroutineScope.launch {
                                // Animate scroll to the first item
                                listState.animateScrollToItem(index = 0)
                            }
                        }
                    )
                }
            }
        },
        bottomBar = {
            Box(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    visible = productsState.products != null,
                    enter = slideInVertically(
                        initialOffsetY = { 50 }
                    ) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { 50 }) + fadeOut()
                )
                {
                    BottomPagesBar(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .clip(RoundedCornerShape(topStart = 20.dp))
                            .background(color = MaterialTheme.colors.surface)
                            .padding(start = 8.dp, end = 8.dp),
                        nextOnclick = {
                            viewModel.onNextPage()
                        },
                        previusOnclick = {
                            viewModel.onPreviousPage()
                        },
                        isFirstPage = isNotFirstPage.value,
                        isLastPage = isNotLastPage.value
                    )
                }

            }
        }
    )
}