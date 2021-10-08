package com.inmersoft.printful_api.domain.use_case.add_to_chart

import com.inmersoft.printful_api.data.local.model.ProductInCart
import com.inmersoft.printful_api.domain.repository.PrintfulRepository

class AddToChartUseCase(
    private val repository: PrintfulRepository
) {
    suspend operator fun invoke(
        syncVariant: ProductInCart
    ) {
        repository.insertVariant(syncVariant)
    }
}