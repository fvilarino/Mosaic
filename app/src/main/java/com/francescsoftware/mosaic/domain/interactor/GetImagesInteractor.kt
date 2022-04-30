package com.francescsoftware.mosaic.domain.interactor

import com.francescsoftware.mosaic.core.wrapper.ImageUrl
import com.francescsoftware.mosaic.core.wrapper.Result

interface GetImagesInteractor {
    suspend fun execute(size: Int): Result<List<ImageUrl>>
}
