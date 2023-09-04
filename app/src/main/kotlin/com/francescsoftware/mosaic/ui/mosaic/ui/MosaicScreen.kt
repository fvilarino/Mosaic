package com.francescsoftware.mosaic.ui.mosaic.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.francescsoftware.mosaic.ui.mosaic.viewmodel.LoadState
import com.francescsoftware.mosaic.ui.mosaic.viewmodel.MosaicState
import com.francescsoftware.mosaic.ui.mosaic.viewmodel.MosaicTiles
import com.francescsoftware.mosaic.ui.mosaic.viewmodel.MosaicViewModel
import com.francescsoftware.mosaic.ui.shared.widget.CardFace

@Composable
fun MosaicScreen(
    viewModel: MosaicViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()

    MosaicScreen(
        state = state,
        retry = viewModel::retry,
        onCardRotated = viewModel::onCardRotated,
        modifier = modifier,
    )
}

@Composable
private fun MosaicScreen(
    state: MosaicState,
    retry: () -> Unit,
    onCardRotated: (MosaicTiles, CardFace) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.loadState) {
        LoadState.Idle -> {}
        LoadState.Loading -> LoadingMosaic(
            modifier = modifier,
        )
        LoadState.Loaded -> LoadedMosaic(
            state = state,
            onRotated = onCardRotated,
            modifier = modifier,
        )
        LoadState.Error -> LoadError(
            retry = retry,
            modifier = modifier,
        )
    }
}
