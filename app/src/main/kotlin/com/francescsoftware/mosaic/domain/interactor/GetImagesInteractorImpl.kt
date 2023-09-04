package com.francescsoftware.mosaic.domain.interactor

import com.francescsoftware.mosaic.core.wrapper.ImageUrl
import com.francescsoftware.mosaic.core.wrapper.Result
import com.francescsoftware.mosaic.data.image.ImageRepository
import javax.inject.Inject

class GetImagesInteractorImpl @Inject constructor(
    private val imageRepository: ImageRepository,
) : GetImagesInteractor {
    override suspend fun execute(size: Int): Result<List<ImageUrl>> =
        imageRepository.getImages(size)
}
