package com.inmersoft.ecommerce.presentation.products_details

import com.inmersoft.printful_api.common.ResourcesError
import com.inmersoft.printful_api.domain.model.Details

data class DetailsState(
    val isLoading: Boolean = false,
    val products: Details? = null,
    val error: ResourcesError? = null
)
