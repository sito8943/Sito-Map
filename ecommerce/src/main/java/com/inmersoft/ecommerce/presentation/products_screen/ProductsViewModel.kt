package com.inmersoft.ecommerce.presentation.products_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inmersoft.printful_api.common.Constants
import com.inmersoft.printful_api.common.DataResult
import com.inmersoft.printful_api.data.local.model.FavoriteProduct
import com.inmersoft.printful_api.domain.model.Products
import com.inmersoft.printful_api.domain.use_case.PrintfulUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    val printfulUseCase: PrintfulUseCase
) : ViewModel() {

    companion object {
        const val TAG = "PAGE-ECOMMERCE"
    }

    private val _productListState = mutableStateOf(ProductListState())
    val productListState: State<ProductListState> = _productListState

    private val _productSearchListState = mutableStateOf(ProductListState())
    val productSearchListState: State<ProductListState> = _productSearchListState

    private val _storeInfoState = mutableStateOf(StoreInfoState())
    val storeInfoState: State<StoreInfoState> = _storeInfoState

    private val _isNotLastPageState = mutableStateOf(true)
    val isNotLastPageState: State<Boolean> = _isNotLastPageState

    private val _isNotFirstPageState = mutableStateOf(false)
    val isNotFirstPageState: State<Boolean> = _isNotFirstPageState


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

    private var vmoffset: Int = 0
    private var vmtotal: Int = 0
    private var vmlimit: Int = 0

    private var lastSearch = ""

    init {
        //Get the first page
        getStoreInfo()
        getStartPage()
    }

    fun getStartPage() {
        getProducts()
    }

    private var lastProducts: Products? = null

    fun onNextPage() {
        lastProducts = productListState.value.products
        val nextPage = vmoffset + vmlimit
        if (lastSearch.isEmpty())
            getProducts(offset = nextPage)
        else
            onSearchProducts(search = lastSearch, offset = nextPage)
    }

    fun clearSearchText() {
        lastSearch = ""
        getProducts()
    }

    fun onPreviousPage() {
        if (vmoffset >= vmlimit) {
            val previusPage = vmoffset - vmlimit
            if (lastSearch.isEmpty())
                getProducts(offset = previusPage)
            else
                onSearchProducts(search = lastSearch, offset = previusPage)
        }

    }

    private fun getProducts(
        offset: Int = 0,
        limit: Int = Constants.MAX_PRODUCT_LIMIT
    ) {
        printfulUseCase.getProductsUseCase(offset = offset, limit = limit).onEach { result ->
            when (result) {
                is DataResult.Success -> {
                    result.data?.let {

                        vmoffset = it.paging.offset
                        vmtotal = it.paging.total
                        vmlimit = it.paging.limit

                        _isNotLastPageState.value = (vmoffset + vmlimit) < vmtotal

                        if (it.result.isNotEmpty()) {
                            _productListState.value = ProductListState(products = result.data)
                            _isNotFirstPageState.value = offset != 0
                        } else {
                            _productListState.value =
                                ProductListState(products = lastProducts)
                            _isNotLastPageState.value = false
                        }
                    }
                }
                is DataResult.Error -> {
                    _productListState.value = ProductListState(error = result.error)
                }
                is DataResult.Loading -> {
                    _productListState.value = ProductListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onSearchProducts(
        search: String,
        offset: Int = 0,
        limit: Int = Constants.MAX_PRODUCT_LIMIT
    ) {
        lastSearch = search
        printfulUseCase.searchProductsUseCase(search = search, offset = offset, limit = limit)
            .onEach { result ->
                when (result) {
                    is DataResult.Success -> {
                        result.data?.let {
                            vmoffset = it.paging.offset
                            vmtotal = it.paging.total
                            vmlimit = it.paging.limit
                            _isNotLastPageState.value =
                                (it.paging.offset + it.paging.limit) < it.paging.total
                            if (it.result.isNotEmpty()) {
                                _productSearchListState.value =
                                    ProductListState(products = result.data)
                                _isNotFirstPageState.value = offset != 0
                            } else {
                                _productSearchListState.value =
                                    ProductListState(products = lastProducts)
                                _isNotLastPageState.value = false
                            }
                        }
                    }
                    is DataResult.Error -> {
                        _productSearchListState.value = ProductListState(error = result.error)
                    }
                    is DataResult.Loading -> {
                        _productSearchListState.value = ProductListState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun getStoreInfo(
    ) {
        printfulUseCase.getStoreInfoUseCase().onEach { result ->
            when (result) {
                is DataResult.Success -> {
                    result.data?.let {
                        _storeInfoState.value = StoreInfoState(storeInfo = it.result)
                    }
                }
                is DataResult.Error -> {
                    _storeInfoState.value = StoreInfoState(error = result.error)
                }
                is DataResult.Loading -> {
                    _storeInfoState.value = StoreInfoState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}