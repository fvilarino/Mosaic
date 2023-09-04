package com.francescsoftware.mosaic.ui.mosaic.ui

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.francescsoftware.mosaic.ui.mosaic.viewmodel.MosaicTiles
import com.francescsoftware.mosaic.ui.shared.theme.MosaicTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

enum class MosaicLayout {
    Layout0,
    Layout1,
    Layout2,
}

@Immutable
class LargeTiles private constructor(
    val tiles: Map<MosaicTiles, @Composable () -> Unit>,
) {
    companion object {
        fun build(
            tile0: @Composable () -> Unit,
            tile1: @Composable () -> Unit,
        ) = LargeTiles(
            buildMap {
                put(MosaicTiles.Large0, tile0)
                put(MosaicTiles.Large1, tile1)
            }
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LargeTiles

        if (tiles != other.tiles) return false

        return true
    }

    override fun hashCode(): Int {
        return tiles.hashCode()
    }
}

@Immutable
class SmallTiles private constructor(
    val tiles: Map<MosaicTiles, @Composable () -> Unit>,
) {
    companion object {
        fun build(
            tile0: @Composable () -> Unit,
            tile1: @Composable () -> Unit,
            tile2: @Composable () -> Unit,
            tile3: @Composable () -> Unit,
            tile4: @Composable () -> Unit,
            tile5: @Composable () -> Unit,
            tile6: @Composable () -> Unit,
        ) = SmallTiles(
            tiles = buildMap {
                put(MosaicTiles.Small0, tile0)
                put(MosaicTiles.Small1, tile1)
                put(MosaicTiles.Small2, tile2)
                put(MosaicTiles.Small3, tile3)
                put(MosaicTiles.Small4, tile4)
                put(MosaicTiles.Small5, tile5)
                put(MosaicTiles.Small6, tile6)
            }
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SmallTiles

        if (tiles != other.tiles) return false

        return true
    }

    override fun hashCode(): Int {
        return tiles.hashCode()
    }
}

@Stable
private class AnimatableOffset {
    val x = Animatable(0f)
    val y = Animatable(0f)
}

@Stable
interface MosaicState {
    fun getOffsetX(mosaicTiles: MosaicTiles): Float
    fun getOffsetY(mosaicTiles: MosaicTiles): Float

    suspend fun snapTo(layout: MosaicLayout)
    suspend fun animateTo(layout: MosaicLayout)
}

private fun <K, V> Map<K, V>.getOrThrow(key: K) =
    get(key) ?: throw IllegalStateException("No key [$key]")

@Stable
class MosaicStateImpl : MosaicState {

    private val animationOffsets: Map<MosaicTiles, AnimatableOffset> = MosaicTiles.values().zip(
        List(MosaicTiles.values().size) {
            AnimatableOffset()
        }
    ).toMap()

    private val spec = tween<Float>(
        durationMillis = 1000,
        easing = FastOutSlowInEasing,
    )

    override fun getOffsetX(mosaicTiles: MosaicTiles): Float =
        animationOffsets.getOrThrow(mosaicTiles).x.value

    override fun getOffsetY(mosaicTiles: MosaicTiles): Float =
        animationOffsets.getOrThrow(mosaicTiles).y.value

    private val offsetMapping = mapOf(
        MosaicLayout.Layout0 to mapOf(
            MosaicTiles.Large0 to Offset(0f, 0f),
            MosaicTiles.Large1 to Offset(3f, 1f),
            MosaicTiles.Small0 to Offset(2f, 0f),
            MosaicTiles.Small1 to Offset(3f, 0f),
            MosaicTiles.Small2 to Offset(4f, 0f),
            MosaicTiles.Small3 to Offset(2f, 1f),
            MosaicTiles.Small4 to Offset(0f, 2f),
            MosaicTiles.Small5 to Offset(1f, 2f),
            MosaicTiles.Small6 to Offset(2f, 2f),
        ),
        MosaicLayout.Layout1 to mapOf(
            MosaicTiles.Large0 to Offset(3f, 0f),
            MosaicTiles.Large1 to Offset(0f, 1f),
            MosaicTiles.Small0 to Offset(3f, 2f),
            MosaicTiles.Small1 to Offset(1f, 0f),
            MosaicTiles.Small2 to Offset(2f, 0f),
            MosaicTiles.Small3 to Offset(4f, 2f),
            MosaicTiles.Small4 to Offset(2f, 2f),
            MosaicTiles.Small5 to Offset(0f, 0f),
            MosaicTiles.Small6 to Offset(2f, 1f),
        ),
        MosaicLayout.Layout2 to mapOf(
            MosaicTiles.Large0 to Offset(3f, 1f),
            MosaicTiles.Large1 to Offset(1f, 0f),
            MosaicTiles.Small0 to Offset(0f, 1f),
            MosaicTiles.Small1 to Offset(3f, 0f),
            MosaicTiles.Small2 to Offset(0f, 0f),
            MosaicTiles.Small3 to Offset(4f, 0f),
            MosaicTiles.Small4 to Offset(1f, 2f),
            MosaicTiles.Small5 to Offset(2f, 2f),
            MosaicTiles.Small6 to Offset(0f, 2f),
        ),
    )

    override suspend fun snapTo(layout: MosaicLayout) {
        val offsets = offsetMapping.getOrThrow(layout)
        offsets.forEach { entry ->
            animationOffsets.getOrThrow(entry.key).x.snapTo(
                targetValue = entry.value.x,
            )
            animationOffsets.getOrThrow(entry.key).y.snapTo(
                targetValue = entry.value.y,
            )
        }
    }

    override suspend fun animateTo(layout: MosaicLayout) {
        coroutineScope {
            val offsets = offsetMapping.getOrThrow(layout)
            offsets.forEach { entry ->
                launch {
                    animationOffsets.getOrThrow(entry.key).x.animateTo(
                        targetValue = entry.value.x,
                        animationSpec = spec,
                    )
                }
                launch {
                    animationOffsets.getOrThrow(entry.key).y.animateTo(
                        targetValue = entry.value.y,
                        animationSpec = spec,
                    )
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MosaicStateImpl

        if (animationOffsets != other.animationOffsets) return false

        return true
    }

    override fun hashCode(): Int {
        return animationOffsets.hashCode()
    }
}

@Composable
private fun rememberMosaicState() = remember {
    MosaicStateImpl()
}

@Composable
fun Mosaic(
    largeTiles: LargeTiles,
    smallTiles: SmallTiles,
    layout: MosaicLayout,
    modifier: Modifier = Modifier,
    state: MosaicState = rememberMosaicState(),
) {
    val orientation = LocalConfiguration.current.orientation

    LaunchedEffect(key1 = Unit) {
        state.snapTo(layout)
    }
    LaunchedEffect(key1 = layout) {
        state.animateTo(layout)
    }

    Layout(
        modifier = modifier,
        content = {
            largeTiles.tiles.forEach { entry -> entry.value() }
            smallTiles.tiles.forEach { entry -> entry.value() }
        }
    ) { measurables, constraints ->
        val cellConstraints = Constraints.fixed(
            width = constraints.maxWidth / widthFactor(orientation),
            height = constraints.maxHeight / heightFactor(orientation),
        )
        val largeConstraints = Constraints.fixed(
            width = 2 * cellConstraints.maxWidth,
            height = 2 * cellConstraints.maxHeight,
        )
        val largePlaceables = measurables.take(2).map { measurable ->
            measurable.measure(largeConstraints)
        }
        val smallPlaceables = measurables.drop(2).map { measurable ->
            measurable.measure(cellConstraints)
        }
        val placeables = MosaicTiles.values().filter { tile -> tile.isLarge }
            .zip(largePlaceables).toMap() +
            MosaicTiles.values().filterNot { tile -> tile.isLarge }
                .zip(smallPlaceables).toMap()

        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight,
        ) {
            val size = getCellSize(orientation, cellConstraints)
            val offsetsMap = MosaicTiles.values().zip(
                MosaicTiles.values().map { tile ->
                    val offset = Offset(
                        x = size.width * state.getOffsetX(tile),
                        y = size.height * state.getOffsetY(tile),
                    )
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        IntOffset(
                            x = offset.x.toInt(),
                            y = offset.y.toInt(),
                        )
                    } else {
                        IntOffset(
                            x = offset.y.toInt(),
                            y = offset.x.toInt(),
                        )
                    }
                }
            ).toMap()
            placeables.forEach { entry ->
                val offset = offsetsMap.getOrThrow(entry.key)
                entry.value.placeRelative(
                    x = offset.x,
                    y = offset.y,
                )
            }
        }
    }
}

private fun getCellSize(
    orientation: Int,
    cellConstraints: Constraints,
): IntSize {
    val size = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        IntSize(
            width = cellConstraints.maxWidth,
            height = cellConstraints.maxHeight,
        )
    } else {
        IntSize(
            width = cellConstraints.maxHeight,
            height = cellConstraints.maxWidth,
        )
    }
    return size
}

private fun widthFactor(orientation: Int) =
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        5
    } else {
        3
    }

private fun heightFactor(orientation: Int) =
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        3
    } else {
        5
    }

@Preview(widthDp = 1920, heightDp = 1080)
@Composable
private fun PreviewMosaic() {
    MosaicTheme {
        Surface(
            color = MaterialTheme.colors.background,
        ) {
            Mosaic(
                largeTiles = LargeTiles.build(
                    tile0 = {
                        SampleTile(label = "Large Tile 0", Color.Red)
                    },
                    tile1 = {
                        SampleTile(label = "Large Tile 1", Color.Blue)
                    },
                ),
                smallTiles = SmallTiles.build(
                    tile0 = {
                        SampleTile(label = "Small Tile 0", Color.Green)
                    },
                    tile1 = {
                        SampleTile(label = "Small Tile 1", Color.Magenta)
                    },
                    tile2 = {
                        SampleTile(label = "Small Tile 2", Color.Yellow)
                    },
                    tile3 = {
                        SampleTile(label = "Small Tile 3", Color.Gray)
                    },
                    tile4 = {
                        SampleTile(label = "Small Tile 4", Color.Cyan)
                    },
                    tile5 = {
                        SampleTile(label = "Small Tile 5", Color.DarkGray)
                    },
                    tile6 = {
                        SampleTile(label = "Small Tile 6", Color.LightGray)
                    },
                ),
                layout = MosaicLayout.Layout2,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun SampleTile(
    label: String,
    color: Color,
) {
    Box(
        modifier = Modifier.background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.subtitle1,
        )
    }
}
