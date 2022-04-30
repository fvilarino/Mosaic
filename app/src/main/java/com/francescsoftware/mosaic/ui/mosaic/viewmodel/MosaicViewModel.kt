package com.francescsoftware.mosaic.ui.mosaic.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.francescsoftware.mosaic.ui.shared.mvi.MviViewModel
import com.francescsoftware.mosaic.ui.shared.widget.CardFace
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MosaicViewModel @Inject constructor(
    reducer: MosaicReducer,
    loaderMiddleware: MosaicLoaderMiddleware,
    builderMiddleware: MosaicBuilderMiddleware,
    animatorMiddleware: MosaicAnimatorMiddleware,
    updatedMiddleware: MosaicUpdaterMiddleware,
) : MviViewModel<MosaicState, MosaicAction>(
    reducer = reducer,
    middlewares = listOf(
        loaderMiddleware,
        builderMiddleware,
        animatorMiddleware,
        updatedMiddleware,
    ),
    initialState = MosaicState.initialValue,
), DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        handleAction(MosaicAction.LoadAll)
    }

    fun retry() {
        handleAction(MosaicAction.LoadAll)
    }

    fun onCardRotated(
        mosaicTiles: MosaicTiles,
        cardFace: CardFace,
    ) {
        handleAction(
            MosaicAction.TileRotated(
                tile = mosaicTiles,
                visibleFace = cardFace,
            )
        )
    }
}
