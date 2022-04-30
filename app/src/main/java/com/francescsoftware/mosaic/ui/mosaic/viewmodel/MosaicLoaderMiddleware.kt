package com.francescsoftware.mosaic.ui.mosaic.viewmodel

import com.francescsoftware.mosaic.core.wrapper.fold
import com.francescsoftware.mosaic.domain.interactor.GetImagesInteractor
import com.francescsoftware.mosaic.ui.shared.mvi.Middleware
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MaxItems = 200

class MosaicLoaderMiddleware @Inject constructor(
    private val getImagesInteractor: GetImagesInteractor,
    private val mosaicStateHolder: MosaicStateHolder,
) : Middleware<MosaicState, MosaicAction>() {

    override fun reduce(state: MosaicState, action: MosaicAction): MosaicState = when (action) {
        MosaicAction.LoadAll -> loadAll(state)
        else -> state
    }

    private fun loadAll(state: MosaicState): MosaicState {
        scope.launch {
            val response = getImagesInteractor.execute(MaxItems)
            response.fold(
                onSuccess = { items ->
                    mosaicStateHolder.setImageItems(items)
                    handleAction(MosaicAction.DataLoaded)
                },
                onFailure = {
                    handleAction(MosaicAction.LoadError)
                },
            )
        }
        return state.copy(
            loadState = LoadState.Loading,
        )
    }
}
