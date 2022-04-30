package com.francescsoftware.mosaic.ui.mosaic.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.francescsoftware.mosaic.ui.mosaic.viewmodel.ImageTile
import com.francescsoftware.mosaic.ui.mosaic.viewmodel.LargeImageTile
import com.francescsoftware.mosaic.ui.mosaic.viewmodel.MosaicState
import com.francescsoftware.mosaic.ui.mosaic.viewmodel.MosaicTiles
import com.francescsoftware.mosaic.ui.mosaic.viewmodel.SmallImageTile
import com.francescsoftware.mosaic.ui.shared.theme.SingleSpace
import com.francescsoftware.mosaic.ui.shared.widget.CardFace
import com.francescsoftware.mosaic.ui.shared.widget.RotatingCard

@Composable
fun LoadedMosaic(
    state: MosaicState,
    onRotated: (MosaicTiles, CardFace) -> Unit,
    modifier: Modifier = Modifier,
) {
    val largeTiles = state.largeTilesState
    val smallTiles = state.smallTilesState
    Mosaic(
        largeTiles = LargeTiles.build(
            tile0 = {
                RotatingLargeTile(largeTiles[MosaicTiles.Large0], onRotated)
            },
            tile1 = {
                RotatingLargeTile(largeTiles[MosaicTiles.Large1], onRotated)
            },
        ),
        smallTiles = SmallTiles.build(
            tile0 = {
                RotatingSmallTile(smallImageTile = smallTiles[MosaicTiles.Small0], onRotated)
            },
            tile1 = {
                RotatingSmallTile(smallImageTile = smallTiles[MosaicTiles.Small1], onRotated)
            },
            tile2 = {
                RotatingSmallTile(smallImageTile = smallTiles[MosaicTiles.Small2], onRotated)
            },
            tile3 = {
                RotatingSmallTile(smallImageTile = smallTiles[MosaicTiles.Small3], onRotated)
            },
            tile4 = {
                RotatingSmallTile(smallImageTile = smallTiles[MosaicTiles.Small4], onRotated)
            },
            tile5 = {
                RotatingSmallTile(smallImageTile = smallTiles[MosaicTiles.Small5], onRotated)
            },
            tile6 = {
                RotatingSmallTile(smallImageTile = smallTiles[MosaicTiles.Small6], onRotated)
            },
        ),
        layout = state.layout,
        modifier = modifier,
    )
}

@Composable
private fun RotatingLargeTile(
    largeImageTile: LargeImageTile,
    onRotated: (MosaicTiles, CardFace) -> Unit,
) {
    RotatingCard(
        onClick = {},
        enabled = false,
        onRotated = { cardFace ->
            onRotated(largeImageTile.tile, cardFace)
        },
        cardFace = largeImageTile.face,
        back = {
            LargeTile(largeImageTile.back)
        },
        front = {
            LargeTile(largeImageTile.front)
        },
        modifier = Modifier.padding(all = SingleSpace),
    )
}

@Composable
private fun LargeTile(
    tile: ImageTile,
) {
    when (tile) {
        is ImageTile.Quad -> TileGroup(
            tileGroup = tile.toTileGroup(),
        )
        is ImageTile.Single -> Tile(
            imageUrl = tile.imageUrl,
        )
    }
}

@Composable
private fun RotatingSmallTile(
    smallImageTile: SmallImageTile,
    onRotated: (MosaicTiles, CardFace) -> Unit,
) {
    RotatingCard(
        onClick = {},
        enabled = false,
        onRotated = { cardFace ->
            onRotated(smallImageTile.tile, cardFace)
        },
        cardFace = smallImageTile.face,
        back = {
            LargeTile(smallImageTile.back)
        },
        front = {
            LargeTile(smallImageTile.front)
        },
        modifier = Modifier.padding(all = SingleSpace),
    )
}

private fun ImageTile.Quad.toTileGroup() = TileGroup(
    image0 = imageUrl0,
    image1 = imageUrl1,
    image2 = imageUrl2,
    image3 = imageUrl3,
)
