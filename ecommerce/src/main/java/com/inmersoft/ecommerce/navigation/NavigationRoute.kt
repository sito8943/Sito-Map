package com.inmersoft.ecommerce.navigation

sealed class NavigationRoute(val route: String) {
    object ProductsListScreen : NavigationRoute(route = "products_list")
    object ProductsChartScreen : NavigationRoute(route = "products_chart")
    object ProductsDetailScreen : NavigationRoute(route = "product_detail")
}