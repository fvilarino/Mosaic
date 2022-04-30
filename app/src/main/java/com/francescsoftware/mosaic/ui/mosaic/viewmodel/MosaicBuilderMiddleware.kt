package com.francescsoftware.mosaic.ui.mosaic.viewmodel

import com.francescsoftware.mosaic.core.dispatcher.DispatcherProvider
import com.francescsoftware.mosaic.ui.shared.mvi.Middleware
import com.francescsoftware.mosaic.ui.shared.widget.CardFace
import kotlinx.coroutines.launch
import javax.inject.Inject

class MosaicBuilderMiddleware @Inject constructor(
    private val mosaicTileBuilder: MosaicTileBuilder,
    private val dispatcherProvider: DispatcherProvider,
) : Middleware<MosaicState, MosaicAction>() {

    override fun reduce(state: MosaicState, action: MosaicAction): MosaicState = when (action) {
        MosaicAction.DataLoaded -> buildTiles(state)
        else -> state
    }

    private fun buildTiles(
        state: MosaicState,
    ): MosaicState {
        scope.launch(dispatcherProvider.Default) {
            val (largeTilesState, smallTilesState) = buildTiles()
            handleAction(
                MosaicAction.TilesBuilt(
                    largeTilesState = largeTilesState,
                    smallTilesState = smallTilesState,
                )
            )
        }
        return state
    }

    private fun buildTiles(): Pair<LargeTilesState, SmallTilesState> {
        val largeTilesState = LargeTilesState(
            tiles = buildMap {
                MosaicTiles.values().filter { tile -> tile.isLarge }.forEach { tile ->
                    put(tile, buildLargeImageTile(tile))
                }
            }
        )
        val smallTilesState = SmallTilesState(
            tiles = buildMap {
                MosaicTiles.values().filterNot { tile -> tile.isLarge }.forEach { tile ->
                    put(tile, buildSmallImageTile(tile))
                }
            }
        )
        return Pair(largeTilesState, smallTilesState)
    }

    private fun buildLargeImageTile(mosaicTiles: MosaicTiles): LargeImageTile = LargeImageTile(
        tile = mosaicTiles,
        face = CardFace.Front,
        front = mosaicTileBuilder.buildLargeImageTile(),
        back = mosaicTileBuilder.buildLargeImageTile(),
    )

    private fun buildSmallImageTile(mosaicTiles: MosaicTiles): SmallImageTile = SmallImageTile(
        tile = mosaicTiles,
        face = CardFace.Front,
        front = mosaicTileBuilder.buildSmallImageTile(),
        back = mosaicTileBuilder.buildSmallImageTile(),
    )
}
