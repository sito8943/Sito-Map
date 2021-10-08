package com.inmersoft.printful_api.domain.use_case.get_products_in_chart

import com.inmersoft.printful_api.data.local.model.ProductInCart
import com.inmersoft.printful_api.domain.repository.PrintfulRepository
import kotlinx.coroutines.flow.Flow

class GetProductsInChartUseCase(
    private val repository: PrintfulRepository
) {
    operator fun invoke(
    ): Flow<List<ProductInCart>> {
        return repository.getAllProductsInChart()
    }
}