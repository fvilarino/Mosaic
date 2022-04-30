package com.francescsoftware.mosaic.core.imageloader

import android.content.Context
import coil.Coil
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.francescsoftware.mosaic.core.wrapper.ImageUrl
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImageLoaderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ImageLoader {

    override fun preload(image: ImageUrl) {
        val request = ImageRequest.Builder(context)
            .data(image.url)
            .memoryCachePolicy(CachePolicy.DISABLED)
            .build()
        Coil.imageLoader(context).enqueue(request)
    }

    override fun preload(images: List<ImageUrl>) {
        images.forEach { image ->
            preload(image)
        }
    }
}
