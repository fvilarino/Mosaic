package com.francescsoftware.mosaic.domain.interactor

import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface InteractorModule {
    @Binds
    @Reusable
    fun bindGetImagesInteractor(
        getImagesInteractorImpl: GetImagesInteractorImpl,
    ): GetImagesInteractor
}
