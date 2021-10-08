package com.inmersoft.printful_api.domain.use_case.delete_favorite_products

import com.inmersoft.printful_api.domain.repository.PrintfulRepository

class DeleteFavoriteProductUseCase(
    private val repository: PrintfulRepository
) {
    suspend operator fun invoke(
        favoriteId: Int
    ) {
        repository.deleteFavorite(favoriteId)
    }
}