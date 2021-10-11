package com.inmersoft.printful_api.domain.use_case

import com.inmersoft.printful_api.domain.use_case.add_favorite_products.AddFavoriteProductUseCase
import com.inmersoft.printful_api.domain.use_case.add_to_chart.AddToChartUseCase
import com.inmersoft.printful_api.domain.use_case.delete_all_favorite_products.DeleteAllFavoriteProductUseCase
import com.inmersoft.printful_api.domain.use_case.delete_favorite_products.DeleteFavoriteProductUseCase
import com.inmersoft.printful_api.domain.use_case.delete_products_in_chart.DeleteProductsInChartUseCase
import com.inmersoft.printful_api.domain.use_case.get_details.GetDetailsUseCase
import com.inmersoft.printful_api.domain.use_case.get_favorite_products.GetFavoriteProductsUseCase
import com.inmersoft.printful_api.domain.use_case.get_products.GetProductsUseCase
import com.inmersoft.printful_api.domain.use_case.get_products_in_chart.GetProductsInChartUseCase
import com.inmersoft.printful_api.domain.use_case.get_store_info.GetStoreInfoUseCase
import com.inmersoft.printful_api.domain.use_case.search_products.SearchProductsUseCase
import com.inmersoft.printful_api.domain.use_case.update_products_in_chart.UpdateProductsInChartUseCase

data class PrintfulUseCase(
    val getProductsUseCase: GetProductsUseCase,
    val getDetailsUseCase: GetDetailsUseCase,
    val searchProductsUseCase: SearchProductsUseCase,
    val getStoreInfoUseCase: GetStoreInfoUseCase,
    val addToChartUseCase: AddToChartUseCase,
    val getProductsInChartUseCase: GetProductsInChartUseCase,
    val deleteProductsInChartUseCase: DeleteProductsInChartUseCase,
    val updateProductsInChartUseCase: UpdateProductsInChartUseCase,
    val addFavoriteProductUseCase: AddFavoriteProductUseCase,
    val deleteFavoriteProductUseCase: DeleteFavoriteProductUseCase,
    val deleteAllFavoriteProductUseCase: DeleteAllFavoriteProductUseCase,
    val getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
)
