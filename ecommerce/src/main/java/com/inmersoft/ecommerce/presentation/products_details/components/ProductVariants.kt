package com.inmersoft.ecommerce.presentation.products_details.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inmersoft.printful_api.data.local.model.ProductInCart
import com.inmersoft.printful_api.data.remote.dto.details.SyncVariant


@Composable
fun ProductVariants(
    modifier: Modifier,
    variants: List<SyncVariant>,
    onClickItem: (variantClicked: SyncVariant) -> Unit,
    productsInCart: List<ProductInCart>
) {
    val variantSelected = remember { mutableStateOf(0L) }
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    )
    {
        items(variants) { variantItem ->
            Spacer(modifier = Modifier.width(5.dp))
            ProductVariantItem(
                variantItem = variantItem,
                variantSelected = variantSelected.value,
                modifier = Modifier.clickable {
                    variantSelected.value = variantItem.id
                    onClickItem(variantItem)
                },
                isInCart = productsInCart.any { currentVariant -> currentVariant.variantId == variantItem.variant_id },
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}