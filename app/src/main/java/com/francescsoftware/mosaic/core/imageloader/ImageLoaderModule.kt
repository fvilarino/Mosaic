package com.francescsoftware.mosaic.core.imageloader

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ImageLoaderModule {
    @Binds
    @Singleton
    fun bindImageLoader(imageLoaderImpl: ImageLoaderImpl): ImageLoader
}
