package com.francescsoftware.mosaic.ui.mosaic.viewmodel

import com.francescsoftware.mosaic.core.imageloader.ImageLoader
import com.francescsoftware.mosaic.core.random.RandomGenerator
import com.francescsoftware.mosaic.core.wrapper.ImageUrl
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class MosaicTileBuilder @Inject constructor(
    private val mosaicStateHolder: MosaicStateHolder,
    private val randomGenerator: RandomGenerator,
    private val imageLoader: ImageLoader,
) {

    fun buildLargeImageTile(): ImageTile {
        val random = randomGenerator.nextInt(4)
        return if (random == 0) {
            ImageTile.Quad(
                imageUrl0 = mosaicStateHolder.getAndPreload(),
                imageUrl1 = mosaicStateHolder.getAndPreload(),
                imageUrl2 = mosaicStateHolder.getAndPreload(),
                imageUrl3 = mosaicStateHolder.getAndPreload(),
            )
        } else {
            ImageTile.Single(
                imageUrl = mosaicStateHolder.getAndPreload(),
            )
        }
    }

    fun buildSmallImageTile() = ImageTile.Single(
        imageUrl = mosaicStateHolder.getAndPreload(),
    )

    private fun MosaicStateHolder.getAndPreload(): ImageUrl =
        getNext().also { imageLoader.preload(it) }
}
