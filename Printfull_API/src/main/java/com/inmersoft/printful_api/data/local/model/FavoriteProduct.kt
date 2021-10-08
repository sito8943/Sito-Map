package com.inmersoft.printful_api.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_products")
data class FavoriteProduct(
    val name: String,
    @PrimaryKey val productId: Int,
    val variantPreviewUrl: String,
)