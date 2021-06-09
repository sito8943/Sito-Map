package com.inmersoft.trinidadpatrimonial.di

import com.inmersoft.trinidadpatrimonial.core.imageloader.GlideImageLoader
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class ImageLoaderModule {
    @Binds
    abstract fun bindGlideImageLoader(impl: GlideImageLoader): ImageLoader
}
