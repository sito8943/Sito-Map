package com.inmersoft.printful_api.domain.use_case.delete_all_favorite_products

import com.inmersoft.printful_api.domain.repository.PrintfulRepository

class DeleteAllFavoriteProductUseCase(
    private val repository: PrintfulRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllFavorite()
    }
}