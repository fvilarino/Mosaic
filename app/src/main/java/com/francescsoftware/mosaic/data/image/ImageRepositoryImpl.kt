package com.francescsoftware.mosaic.data.image

import com.francescsoftware.mosaic.core.dispatcher.DispatcherProvider
import com.francescsoftware.mosaic.core.wrapper.ImageUrl
import com.francescsoftware.mosaic.core.wrapper.Result
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val IMAGE_DIMENSION_START = 300
private const val BASE_URL = "https://placekitten.com/"

class ImageRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
) : ImageRepository {
    override suspend fun getImages(size: Int): Result<List<ImageUrl>> =
        withContext(dispatcherProvider.Default) {
            Result.Success(
                List(size) { index ->
                    val width = IMAGE_DIMENSION_START + (10 * (index / 10))
                    val height = IMAGE_DIMENSION_START + (10 * (index % 10))
                    ImageUrl(
                        url = "$BASE_URL$width/$height"
                    )
                }
            )
        }
}
