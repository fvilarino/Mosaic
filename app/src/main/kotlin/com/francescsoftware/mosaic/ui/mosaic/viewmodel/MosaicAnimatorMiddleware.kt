package com.francescsoftware.mosaic.ui.mosaic.viewmodel

import com.francescsoftware.mosaic.core.dispatcher.DispatcherProvider
import com.francescsoftware.mosaic.core.random.RandomGenerator
import com.francescsoftware.mosaic.ui.mosaic.ui.MosaicLayout
import com.francescsoftware.mosaic.ui.shared.mvi.Middleware
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private val RotationDelay = 8.toDuration(DurationUnit.SECONDS)
private const val AnimationLoops = 5

private fun MosaicLayout.next(): MosaicLayout {
    val layouts = MosaicLayout.values()
    val nextLayoutIndex = (layouts.indexOf(this) + 1) % layouts.size
    return layouts[nextLayoutIndex]
}

class MosaicAnimatorMiddleware @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val randomGenerator: RandomGenerator,
) : Middleware<MosaicState, MosaicAction>() {

    private lateinit var largeTiles: LargeTilesState
    private lateinit var smallTiles: SmallTilesState
    private var loops = 0

    override fun reduce(state: MosaicState, action: MosaicAction): MosaicState = when (action) {
        is MosaicAction.TilesBuilt -> {
            loops = 0
            scope.launch {
                delay(RotationDelay)
                handleAction(MosaicAction.RotateTiles)
            }
            state
        }
        MosaicAction.RotateTiles -> rotateTiles(state)
        else -> state
    }

    private fun rotateTiles(state: MosaicState): MosaicState {
        scope.launch(dispatcherProvider.Default) {
            largeTiles = state.largeTilesState
            smallTiles = state.smallTilesState
            val numTilesToRotate = 1 + randomGenerator.nextInt(3)
            val tilesToRotate = MosaicTiles.values().toList().shuffled().take(numTilesToRotate)
            tilesToRotate.forEachIndexed { index, tile ->
                rotateTile(tile)
                handleAction(
                    MosaicAction.TilesShuffled(
                        largeTilesState = largeTiles,
                        smallTilesState = smallTiles,
                    )
                )
                if (index != tilesToRotate.lastIndex) {
                    delay(400L)
                }
            }

            if (++loops >= AnimationLoops) {
                loops = 0
                if (isActive) {
                    delay(RotationDelay)
                    handleAction(
                        MosaicAction.UpdateLayout(
                            layout = state.layout.next(),
                        )
                    )
                }
            }
            if (isActive) {
                delay(RotationDelay)
                handleAction(MosaicAction.RotateTiles)
            }
        }
        return state
    }

    private fun rotateTile(tile: MosaicTiles) {
        if (tile.isLarge) {
            largeTiles = largeTiles.copy(
                tiles = largeTiles.tiles.toMutableMap().apply {
                    put(tile, largeTiles[tile].rotate())
                }
            )
        } else {
            smallTiles = smallTiles.copy(
                tiles = smallTiles.tiles.toMutableMap().apply {
                    put(tile, smallTiles[tile].rotate())
                }
            )
        }
    }

    private fun LargeImageTile.rotate(): LargeImageTile = copy(
        face = face.next,
    )

    private fun SmallImageTile.rotate() = copy(
        face = face.next,
    )
}
