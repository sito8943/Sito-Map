package com.inmersoft.ecommerce.presentation.products_screen

import com.inmersoft.printful_api.common.ResourcesError
import com.inmersoft.printful_api.domain.model.Products

data class ProductListState(
    val isLoading: Boolean = false,
    val products: Products? = null,
    val error: ResourcesError? = null
)
