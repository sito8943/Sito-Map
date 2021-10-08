package com.inmersoft.ecommerce.presentation.products_chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inmersoft.printful_api.data.local.model.ProductInCart
import com.inmersoft.printful_api.domain.use_case.PrintfulUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsCartViewModel @Inject constructor(
    private val printfulUseCase: PrintfulUseCase
) :
    ViewModel() {

    val productsInChart = printfulUseCase.getProductsInChartUseCase()


    fun onDeleteProductInChart(roomVariantId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            printfulUseCase.deleteProductsInChartUseCase(roomVariantId)
        }
    }

    fun onUpdateProductInChart(productInCart: ProductInCart) {
        viewModelScope.launch(Dispatchers.IO) {
            printfulUseCase.updateProductsInChartUseCase(productInCart = productInCart)
        }
    }

    fun onInsertProductInChart(productInCart: ProductInCart) {
        viewModelScope.launch(Dispatchers.IO) {
            printfulUseCase.addToChartUseCase(syncVariant = productInCart)
        }
    }

}