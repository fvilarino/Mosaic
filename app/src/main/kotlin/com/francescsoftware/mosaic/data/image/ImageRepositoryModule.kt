package com.francescsoftware.mosaic.data.image

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ImageRepositoryModule {
    @Binds
    @Singleton
    fun bindImageRepository(imageRepositoryImpl: ImageRepositoryImpl): ImageRepository
}
