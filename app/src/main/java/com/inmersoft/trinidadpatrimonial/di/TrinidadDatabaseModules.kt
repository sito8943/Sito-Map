package com.inmersoft.trinidadpatrimonial.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.inmersoft.ecommerce.crypt.AESEncyption
import com.inmersoft.printful_api.common.Constants
import com.inmersoft.printful_api.data.local.PrintfulDataBase
import com.inmersoft.printful_api.data.local.dao.FavoriteProductsDao
import com.inmersoft.printful_api.data.local.dao.ProductInCartDao
import com.inmersoft.printful_api.data.remote.PrintfulApi
import com.inmersoft.printful_api.data.repository.PrintfulRepositoryImpl
import com.inmersoft.printful_api.domain.repository.PrintfulRepository
import com.inmersoft.printful_api.domain.use_case.PrintfulUseCase
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
import com.inmersoft.trinidadpatrimonial.MainApplication
import com.inmersoft.trinidadpatrimonial.database.data.AppDatabase
import com.inmersoft.trinidadpatrimonial.database.data.DataRepository
import com.inmersoft.trinidadpatrimonial.database.data.source.local.PlaceDao
import com.inmersoft.trinidadpatrimonial.database.data.source.local.PlaceTypeDao
import com.inmersoft.trinidadpatrimonial.database.data.source.local.RoutesDao
import com.inmersoft.trinidadpatrimonial.preferences.UserPreferencesRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrinidadDatabaseModules {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().addLast(
        KotlinJsonAdapterFactory()
    ).build()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun providePlaceDao(database: AppDatabase): PlaceDao = database.placesDao()

    @Singleton
    @Provides
    fun provideRoutesDao(database: AppDatabase): RoutesDao = database.routesDao()

    @Singleton
    @Provides
    fun providePlaceTypeDao(database: AppDatabase): PlaceTypeDao = database.placesTypeDao()

    @Singleton
    @Provides
    fun provideRepository(
        placeDao: PlaceDao,
        routesDao: RoutesDao,
        placeTypeDao: PlaceTypeDao,
    ) = DataRepository(placeDao, routesDao, placeTypeDao)

    @Singleton
    @Provides
    fun provideUserPrefs(@ApplicationContext appContext: Context): UserPreferencesRepository {
        return (appContext as MainApplication).userPrefsRepo
    }


    @Provides
    @Singleton
    fun providePrintfulRepository(
        api: PrintfulApi,
        productInCartDao: ProductInCartDao,
        favoriteProductsDao: FavoriteProductsDao
    ): PrintfulRepository {
        return PrintfulRepositoryImpl(
            apiPrintfulApi = api,
            productInCartDao = productInCartDao,
            favoriteProductsDao = favoriteProductsDao
        )
    }

    @Provides
    @Singleton
    fun providePrintfulApiService(): PrintfulApi {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        val defaultHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .addHeader(
                        "Authorization",
                        value = "Basic ${AESEncyption.decrypt(Constants.API_KEY)}"
                    )
                    .build()
                chain.proceed(request)
            }).build()

        return Retrofit.Builder()
            .baseUrl(PrintfulApi.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(defaultHttpClient)
            .build()
            .create(PrintfulApi::class.java)
    }

    @Provides
    @Singleton
    fun providePrintfulUseCases(repository: PrintfulRepository): PrintfulUseCase {
        return PrintfulUseCase(
            getDetailsUseCase = GetDetailsUseCase(repository = repository),
            getProductsUseCase = GetProductsUseCase(repository = repository),
            getStoreInfoUseCase = GetStoreInfoUseCase(repository = repository),
            searchProductsUseCase = SearchProductsUseCase(repository = repository),
            addToChartUseCase = AddToChartUseCase(repository = repository),
            getProductsInChartUseCase = GetProductsInChartUseCase(repository = repository),
            deleteProductsInChartUseCase = DeleteProductsInChartUseCase(repository = repository),
            updateProductsInChartUseCase = UpdateProductsInChartUseCase(repository = repository),
            addFavoriteProductUseCase = AddFavoriteProductUseCase(repository = repository),
            deleteFavoriteProductUseCase = DeleteFavoriteProductUseCase(repository = repository),
            deleteAllFavoriteProductUseCase = DeleteAllFavoriteProductUseCase(repository = repository),
            getFavoriteProductsUseCase = GetFavoriteProductsUseCase(repository = repository),
        )
    }

    @Singleton
    @Provides
    fun providePrintFulDatabase(@ApplicationContext appContext: Context) =
        PrintfulDataBase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideProductsInCartDao(database: PrintfulDataBase): ProductInCartDao =
        database.productsInCartDao()

    @Singleton
    @Provides
    fun provideFavoriteProductsDao(database: PrintfulDataBase): FavoriteProductsDao =
        database.favoriteProductsDao()
}
