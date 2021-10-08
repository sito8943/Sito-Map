package com.inmersoft.printful_api.data.remote.dto.common

import com.inmersoft.printful_api.data.local.model.FavoriteProduct

data class ProductData(
    val external_id: String,
    val id: Int,
    val is_ignored: Boolean,
    val name: String,
    val synced: Int,
    val thumbnail_url: String,
    val variants: Int
)

fun ProductData.toFavoriteProduct(): FavoriteProduct {
    return FavoriteProduct(
        name = name,
        productId = id,
        variantPreviewUrl = thumbnail_url
    )
}