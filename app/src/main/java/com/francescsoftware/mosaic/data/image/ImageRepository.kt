package com.francescsoftware.mosaic.data.image

import com.francescsoftware.mosaic.core.wrapper.ImageUrl
import com.francescsoftware.mosaic.core.wrapper.Result

interface ImageRepository {
    suspend fun getImages(size: Int): Result<List<ImageUrl>>
}
