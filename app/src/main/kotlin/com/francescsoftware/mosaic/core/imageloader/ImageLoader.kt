package com.francescsoftware.mosaic.core.imageloader

import com.francescsoftware.mosaic.core.wrapper.ImageUrl

interface ImageLoader {
    fun preload(image: ImageUrl)
    fun preload(images: List<ImageUrl>)
}
