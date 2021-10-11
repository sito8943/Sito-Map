package com.inmersoft.printful_api.data.remote.dto.details

import com.inmersoft.printful_api.data.remote.dto.common.ProductData

data class DetailsResult(
    val sync_product: ProductData,
    val sync_variants: List<SyncVariant>
)