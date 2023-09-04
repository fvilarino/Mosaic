package com.francescsoftware.mosaic.ui.mosaic.viewmodel

import com.francescsoftware.mosaic.core.wrapper.ImageUrl
import com.francescsoftware.mosaic.ui.shared.widget.CardFace

sealed interface ImageTile {

    data class Single(
        val imageUrl: ImageUrl,
    ) : ImageTile

    data class Quad(
        val imageUrl0: ImageUrl,
        val imageUrl1: ImageUrl,
        val imageUrl2: ImageUrl,
        val imageUrl3: ImageUrl,
    ) : ImageTile
}

data class LargeImageTile(
    val tile: MosaicTiles,
    val face: CardFace,
    val front: ImageTile,
    val back: ImageTile,
)

data class SmallImageTile(
    val tile: MosaicTiles,
    val face: CardFace,
    val front: ImageTile.Single,
    val back: ImageTile.Single,
)
