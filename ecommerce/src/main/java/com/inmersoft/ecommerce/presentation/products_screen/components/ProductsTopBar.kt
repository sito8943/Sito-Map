package com.inmersoft.ecommerce.presentation.products_screen.components

import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.ShopTwo
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inmersoft.ecommerce.R
import com.inmersoft.ecommerce.common.WebUtils
import com.inmersoft.ecommerce.navigation.NavigationRoute
import com.inmersoft.printful_api.data.remote.dto.store_info.StoreResult


@Composable
fun ProductsTopBar(storeResult: StoreResult?, navController: NavController) {
    val context = LocalContext.current
    TopAppBar(
        contentPadding = PaddingValues(),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        Icon(
            Icons.Filled.ShopTwo,
            contentDescription = null,
            modifier = Modifier.weight(2f)
        )
        Text(
            text = storeResult?.name ?: "",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.weight(9f)
        )
        IconButton(modifier = Modifier
            .weight(2f),
            onClick = {
                navController.navigate(NavigationRoute.ProductsChartScreen.route)
            })
        {
            Icon(
                Icons.Filled.ShoppingCart,
                contentDescription = stringResource(id = R.string.go_cart)
            )
        }

        IconButton(modifier = Modifier.padding(end = 8.dp)
            .weight(2f),
            onClick = {
                WebUtils.openWeb(
                    context,
                    Uri.parse(storeResult?.website ?: ""),
                    R.color.design_default_color_on_primary
                )
            })
        {
            Icon(
                Icons.Filled.Link,
                contentDescription = stringResource(id = R.string.go_web)
            )
        }
    }

}