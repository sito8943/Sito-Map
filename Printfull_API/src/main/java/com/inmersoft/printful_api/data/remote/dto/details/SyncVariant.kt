package com.inmersoft.printful_api.data.remote.dto.details

import com.inmersoft.printful_api.data.local.model.FavoriteProduct
import com.inmersoft.printful_api.data.local.model.ProductInCart

data class SyncVariant(
    val currency: String,
    val external_id: String,
    val files: List<File>,
    val id: Long,
    val is_ignored: Boolean,
    val name: String,
    val options: List<Any>,
    val product: Product,
    val retail_price: String,
    val sku: String,
    val sync_product_id: Int,
    val synced: Boolean,
    val variant_id: Int,
    val warehouse_product_variant_id: Any
)

fun SyncVariant.toProductInCart(): ProductInCart {
    return ProductInCart(
        name = name,
        currency = currency,
        retailPrice = retail_price,
        syncProductId = sync_product_id,
        variantId = variant_id,
        variantPreviewUrl = files[files.size - 1].preview_url
    )
}

fun SyncVariant.toFavoriteProduct(): FavoriteProduct {
    return FavoriteProduct(
        name = name,
        variantPreviewUrl = product.image,
        productId = sync_product_id
    )
}