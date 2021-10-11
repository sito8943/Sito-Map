package com.inmersoft.ecommerce.presentation.products_details.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import coil.annotation.ExperimentalCoilApi
import com.inmersoft.printful_api.data.local.model.ProductInCart
import com.inmersoft.printful_api.data.remote.dto.details.SyncVariant
import com.inmersoft.printful_api.domain.model.Details

@ExperimentalCoilApi
@Composable
fun DetailComponent(
    details: Details, modifier: Modifier = Modifier,
    variant: MutableState<SyncVariant?>,
    productsInCart: List<ProductInCart>
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            ProductVariants(
                modifier = Modifier
                    .fillMaxWidth(),
                variants = details.result.sync_variants, onClickItem = { variantClicked ->
                    variant.value = variantClicked
                },
                productsInCart = productsInCart
            )
        }
    }
}