package com.inmersoft.printful_api.domain.use_case.update_products_in_chart

import com.inmersoft.printful_api.data.local.model.ProductInCart
import com.inmersoft.printful_api.domain.repository.PrintfulRepository

class UpdateProductsInChartUseCase(
    private val repository: PrintfulRepository
) {
    suspend operator fun invoke(
        productInCart: ProductInCart
    ) {
        return repository.updateVariant(productInCart = productInCart)
    }
}