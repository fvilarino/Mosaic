package com.francescsoftware.mosaic.ui.mosaic.viewmodel

import com.francescsoftware.mosaic.ui.shared.mvi.Reducer
import javax.inject.Inject

class MosaicReducer @Inject constructor() : Reducer<MosaicState, MosaicAction> {
    override fun reduce(state: MosaicState, action: MosaicAction): MosaicState = when (action) {
        MosaicAction.LoadError -> state.copy(
            loadState = LoadState.Error,
        )
        is MosaicAction.TilesBuilt -> state.copy(
            loadState = LoadState.Loaded,
            largeTilesState = action.largeTilesState,
            smallTilesState = action.smallTilesState,
        )
        is MosaicAction.TilesShuffled -> state.copy(
            loadState = LoadState.Loaded,
            largeTilesState = action.largeTilesState,
            smallTilesState = action.smallTilesState,
        )
        is MosaicAction.TilesUpdated -> state.copy(
            largeTilesState = action.largeTilesState,
            smallTilesState = action.smallTilesState,
        )
        is MosaicAction.UpdateLayout -> state.copy(
            layout = action.layout,
        )
        else -> state
    }
}
