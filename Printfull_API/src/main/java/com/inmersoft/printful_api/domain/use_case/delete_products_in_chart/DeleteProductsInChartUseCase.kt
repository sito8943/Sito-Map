package com.inmersoft.printful_api.domain.use_case.delete_products_in_chart

import com.inmersoft.printful_api.domain.repository.PrintfulRepository

class DeleteProductsInChartUseCase(
    private val repository: PrintfulRepository
) {
    suspend operator fun invoke(
        variantId: Int
    ) {
        return repository.deleteVariantById(roomSyncVariantID = variantId)
    }
}