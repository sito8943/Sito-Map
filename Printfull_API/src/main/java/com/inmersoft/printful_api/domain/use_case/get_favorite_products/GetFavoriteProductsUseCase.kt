package com.inmersoft.printful_api.domain.use_case.get_favorite_products

import com.inmersoft.printful_api.data.local.model.FavoriteProduct
import com.inmersoft.printful_api.domain.repository.PrintfulRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteProductsUseCase(
    private val repository: PrintfulRepository
) {
    operator fun invoke(
    ): Flow<List<FavoriteProduct>> {
        return repository.getAllFavorites()
    }
}