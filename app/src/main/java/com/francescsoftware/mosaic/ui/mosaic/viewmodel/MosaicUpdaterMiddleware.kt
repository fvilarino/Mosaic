package com.francescsoftware.mosaic.ui.mosaic.viewmodel

import com.francescsoftware.mosaic.core.dispatcher.DispatcherProvider
import com.francescsoftware.mosaic.ui.shared.mvi.Middleware
import com.francescsoftware.mosaic.ui.shared.widget.CardFace
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class UpdatedTiles(
    val largeTiles: LargeTilesState,
    val smallTiles: SmallTilesState,
)

class MosaicUpdaterMiddleware @Inject constructor(
    private val mosaicTileBuilder: MosaicTileBuilder,
    private val dispatcherProvider: DispatcherProvider,
) : Middleware<MosaicState, MosaicAction>() {

    override fun reduce(state: MosaicState, action: MosaicAction): MosaicState {
        return when (action) {
            is MosaicAction.TileRotated -> {
                scope.launch(dispatcherProvider.Default) {
                    val updatedTiles = updateTile(state, action.tile, action.visibleFace)
                    handleAction(
                        MosaicAction.TilesUpdated(
                            largeTilesState = updatedTiles.largeTiles,
                            smallTilesState = updatedTiles.smallTiles,
                        )
                    )
                }
                state
            }
            else -> state
        }
    }

    private fun updateTile(
        state: MosaicState,
        tile: MosaicTiles,
        visibleFace: CardFace,
    ): UpdatedTiles {
        var largeTiles: LargeTilesState = state.largeTilesState
        var smallTiles: SmallTilesState = state.smallTilesState
        if (tile.isLarge) {
            largeTiles = largeTiles.copy(
                tiles = largeTiles.tiles.toMutableMap().apply {
                    put(tile, largeTiles[tile].update(visibleFace))
                }
            )
        } else {
            smallTiles = smallTiles.copy(
                tiles = smallTiles.tiles.toMutableMap().apply {
                    put(tile, smallTiles[tile].update(visibleFace))
                }
            )
        }
        return UpdatedTiles(
            largeTiles = largeTiles,
            smallTiles = smallTiles,
        )
    }


    private fun LargeImageTile.update(visibleFace: CardFace): LargeImageTile {
        val tile = mosaicTileBuilder.buildLargeImageTile()
        return copy(
            front = if (visibleFace == CardFace.Back) {
                tile
            } else {
                front
            },
            back = if (visibleFace == CardFace.Front) {
                tile
            } else {
                back
            },
        )
    }

    private fun SmallImageTile.update(visibleFace: CardFace): SmallImageTile {
        val tile = mosaicTileBuilder.buildSmallImageTile()
        return copy(
            front = if (visibleFace == CardFace.Back) {
                tile
            } else {
                front
            },
            back = if (visibleFace == CardFace.Front) {
                tile
            } else {
                back
            },
        )
    }
}
