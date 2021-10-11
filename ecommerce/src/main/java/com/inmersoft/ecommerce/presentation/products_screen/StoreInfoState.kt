package com.inmersoft.ecommerce.presentation.products_screen

import com.inmersoft.printful_api.common.ResourcesError
import com.inmersoft.printful_api.data.remote.dto.store_info.StoreResult

data class StoreInfoState(
    val isLoading: Boolean = false,
    val storeInfo: StoreResult? = null,
    val error: ResourcesError? = null
)
