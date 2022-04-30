package com.francescsoftware.mosaic.core.random

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RandomGeneratorModule {
    @Binds
    @Singleton
    fun bindRandomGenerator(randomGeneratorImpl: RandomGeneratorImpl): RandomGenerator
}
