package com.inmersoft.printful_api.domain.model

import com.inmersoft.printful_api.data.remote.dto.common.ProductData
import com.inmersoft.printful_api.data.remote.dto.products.Paging

data class Products(
    val paging: Paging,
    val result: List<ProductData>
)