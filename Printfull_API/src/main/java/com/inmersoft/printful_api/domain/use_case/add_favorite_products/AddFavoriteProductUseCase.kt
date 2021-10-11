package com.inmersoft.printful_api.domain.use_case.add_favorite_products

import com.inmersoft.printful_api.data.local.model.FavoriteProduct
import com.inmersoft.printful_api.domain.repository.PrintfulRepository

class AddFavoriteProductUseCase(
    private val repository: PrintfulRepository
) {
    suspend operator fun invoke(
        favorite: FavoriteProduct
    ) {
        repository.insertFavorite(favorite)
    }
}