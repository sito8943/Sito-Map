package com.inmersoft.ecommerce.presentation.products_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inmersoft.printful_api.common.Constants
import com.inmersoft.printful_api.common.DataResult
import com.inmersoft.printful_api.data.local.model.FavoriteProduct
import com.inmersoft.printful_api.data.remote.dto.details.SyncVariant
import com.inmersoft.printful_api.data.remote.dto.details.toProductInCart
import com.inmersoft.printful_api.domain.use_case.PrintfulUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val printfulUseCase: PrintfulUseCase
) :
    ViewModel() {

    private val _detailsProductState = mutableStateOf(DetailsState())
    val detailsProductState: State<DetailsState> = _detailsProductState

    val productsInChart = printfulUseCase.getProductsInChartUseCase()

    //Favorite Products
    val favoriteProducts = printfulUseCase.getFavoriteProductsUseCase()
    fun onAddFavorite(favoriteProduct: FavoriteProduct) {
        viewModelScope.launch(Dispatchers.IO) {
            printfulUseCase.addFavoriteProductUseCase(favorite = favoriteProduct)
        }
    }

    fun onDeleteFavorite(favoriteProduct: FavoriteProduct) {
        viewModelScope.launch(Dispatchers.IO) {
            printfulUseCase.deleteFavoriteProductUseCase(favoriteProduct.productId)
        }
    }

    init {
        savedStateHandle.get<String>(Constants.PARAM_PRODUCT_ID)?.let { productID ->
            getDetailsProduct(productId = productID)
        }
    }

    private fun getDetailsProduct(
        productId: String
    ) {
        printfulUseCase.getDetailsUseCase(productID = productId).onEach { result ->
            when (result) {
                is DataResult.Success -> {
                    _detailsProductState.value = DetailsState(products = result.data)
                }
                is DataResult.Error -> {
                    _detailsProductState.value = DetailsState(error = result.error)
                }
                is DataResult.Loading -> {
                    _detailsProductState.value = DetailsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onAddToChart(syncVariant: SyncVariant) {
        viewModelScope.launch(Dispatchers.IO) {
            printfulUseCase.addToChartUseCase(syncVariant = syncVariant.toProductInCart())
        }
    }

}