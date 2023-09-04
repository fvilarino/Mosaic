package com.francescsoftware.mosaic.ui.mosaic.viewmodel

import androidx.compose.runtime.Immutable
import com.francescsoftware.mosaic.ui.mosaic.ui.MosaicLayout
import com.francescsoftware.mosaic.ui.shared.mvi.Action
import com.francescsoftware.mosaic.ui.shared.mvi.State
import com.francescsoftware.mosaic.ui.shared.widget.CardFace

enum class LoadState {
    Idle,
    Loading,
    Loaded,
    Error,
}

@Immutable
data class LargeTilesState(
    val tiles: Map<MosaicTiles, LargeImageTile>,
) {
    operator fun get(tile: MosaicTiles) =
        tiles[tile] ?: throw NullPointerException("No large tile for $tile")
}

@Immutable
data class SmallTilesState(
    val tiles: Map<MosaicTiles, SmallImageTile>,
) {
    operator fun get(tile: MosaicTiles) =
        tiles[tile] ?: throw NullPointerException("No small tile for $tile")
}

@Immutable
data class MosaicState(
    val loadState: LoadState,
    val largeTilesState: LargeTilesState,
    val smallTilesState: SmallTilesState,
    val layout: MosaicLayout,
) : State {
    companion object {
        val initialValue = MosaicState(
            loadState = LoadState.Idle,
            largeTilesState = LargeTilesState(tiles = emptyMap()),
            smallTilesState = SmallTilesState(tiles = emptyMap()),
            layout = MosaicLayout.Layout0,
        )
    }
}

sealed interface MosaicAction : Action {
    object LoadAll : MosaicAction
    object LoadError : MosaicAction
    object RotateTiles : MosaicAction
    object DataLoaded : MosaicAction
    data class TilesBuilt(
        val largeTilesState: LargeTilesState,
        val smallTilesState: SmallTilesState,
    ) : MosaicAction

    data class TilesShuffled(
        val largeTilesState: LargeTilesState,
        val smallTilesState: SmallTilesState,
    ) : MosaicAction

    data class TilesUpdated(
        val largeTilesState: LargeTilesState,
        val smallTilesState: SmallTilesState,
    ) : MosaicAction

    data class TileRotated(
        val tile: MosaicTiles,
        val visibleFace: CardFace,
    ) : MosaicAction

    data class UpdateLayout(
        val layout: MosaicLayout,
    ) : MosaicAction
}
