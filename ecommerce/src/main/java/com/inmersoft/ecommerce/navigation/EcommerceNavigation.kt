package com.inmersoft.ecommerce.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.statusBarsPadding
import com.inmersoft.ecommerce.presentation.products_chart.ProductsChartScreen
import com.inmersoft.ecommerce.presentation.products_details.DetailsScreen
import com.inmersoft.ecommerce.presentation.products_screen.ProductsScreen

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@Composable
fun EcommerceNavigation() {
    val navController = rememberNavController()
  //  val widthScreen = 1300
 //   val animationSpeed = 600

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.ProductsListScreen.route
    ) {
        composable(
            route = NavigationRoute.ProductsListScreen.route,
            /*exitTransition = { _, _ ->
                slideOutHorizontally(
                    targetOffsetX = { -widthScreen },
                    animationSpec = tween(
                        durationMillis = animationSpeed,
                    )
                ) + fadeOut(animationSpec = tween(animationSpeed))
            },
            popEnterTransition = { _, _ ->
                slideInHorizontally(
                    initialOffsetX = { -widthScreen },
                    animationSpec = tween(
                        durationMillis = animationSpeed,

                        )
                )
            },*/
        ) {
            ProductsScreen(
                modifier = Modifier.statusBarsPadding(),
                navController = navController
            )
        }
        composable(
            route = NavigationRoute.ProductsDetailScreen.route + "/{productID}",
            /*exitTransition = { _, target ->
                when (target.destination.route) {
                    NavigationRoute.ProductsChartScreen.route ->
                        slideOutHorizontally(
                            targetOffsetX = { -widthScreen },
                            animationSpec = tween(
                                durationMillis = animationSpeed,
                            )
                        ) + fadeOut(animationSpec = tween(animationSpeed))
                    else ->
                        slideOutHorizontally(
                            targetOffsetX = { widthScreen },
                            animationSpec = tween(
                                durationMillis = animationSpeed,
                            )
                        ) + fadeOut(animationSpec = tween(animationSpeed))
                }
            },
            enterTransition = { initial, _ ->
                when (initial.destination.route) {
                    NavigationRoute.ProductsListScreen.route ->
                        slideInHorizontally(
                            initialOffsetX = { widthScreen },
                            animationSpec = tween(
                                durationMillis = animationSpeed,
                            )
                        )
                    else ->
                        slideInHorizontally(
                            initialOffsetX = { -widthScreen },
                            animationSpec = tween(
                                durationMillis = animationSpeed,
                            )
                        )
                }
            },
            popEnterTransition = { _, _ ->
                slideInHorizontally(
                    initialOffsetX = { -widthScreen },
                    animationSpec = tween(
                        durationMillis = animationSpeed,
                    )
                )
            },*/
        ) {
            DetailsScreen(navController = navController)
        }
        composable(
            route = NavigationRoute.ProductsChartScreen.route,
            /*exitTransition = { _, _ ->
                slideOutHorizontally(
                    targetOffsetX = { widthScreen },
                    animationSpec = tween(
                        durationMillis = animationSpeed,
                    )
                ) + fadeOut(animationSpec = tween(animationSpeed))
            },
            enterTransition = { initial, _ ->
                when (initial.destination.route) {
                    NavigationRoute.ProductsDetailScreen.route ->
                        slideInHorizontally(
                            initialOffsetX = { -widthScreen },
                            animationSpec = tween(
                                durationMillis = animationSpeed,
                            )
                        )
                    else ->
                        slideInHorizontally(
                            initialOffsetX = { widthScreen },
                            animationSpec = tween(
                                durationMillis = animationSpeed,
                            )
                        )
                }
            },
            popEnterTransition = { _, _ ->
                slideInHorizontally(
                    initialOffsetX = { -widthScreen },
                    animationSpec = tween(
                        durationMillis = animationSpeed,

                        )
                )
            },*/
        ) {
            ProductsChartScreen(navController = navController)
        }
    }
}