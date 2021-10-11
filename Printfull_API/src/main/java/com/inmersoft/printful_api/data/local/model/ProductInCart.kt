package com.inmersoft.printful_api.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_variant_chart")
data class ProductInCart(
    val name: String,
    @PrimaryKey val variantId: Int,
    val syncProductId: Int,
    val currency: String,
    val retailPrice: String,
    val variantPreviewUrl: String,
    var cantInChart: Int = 1,
)