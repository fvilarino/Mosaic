package com.francescsoftware.mosaic.ui.mosaic.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.francescsoftware.mosaic.core.wrapper.ImageUrl
import com.francescsoftware.mosaic.ui.shared.theme.HalfSpace
import com.francescsoftware.mosaic.ui.shared.theme.MosaicTheme
import com.francescsoftware.mosaic.ui.shared.theme.QuadSpace
import com.francescsoftware.mosaic.ui.shared.theme.SingleSpace

data class TileGroup(
    val image0: ImageUrl,
    val image1: ImageUrl,
    val image2: ImageUrl,
    val image3: ImageUrl,
)

@Composable
fun TileGroup(
    tileGroup: TileGroup,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.border(border = BorderStroke(
            width = SingleSpace,
            color = MaterialTheme.colors.surface
        )
        ),
    ) {
        TileRow(
            leftImage = tileGroup.image0,
            rightImage = tileGroup.image1,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        TileRow(
            leftImage = tileGroup.image2,
            rightImage = tileGroup.image3,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
private fun TileRow(
    leftImage: ImageUrl,
    rightImage: ImageUrl,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        Tile(
            imageUrl = leftImage,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            border = HalfSpace,
        )
        Tile(
            imageUrl = rightImage,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            border = HalfSpace,
        )
    }
}

@Preview
@Composable
private fun PreviewTileGroup() {
    MosaicTheme {
        Surface(
            color = MaterialTheme.colors.background,
        ) {
            TileGroup(
                tileGroup = TileGroup(
                    image0 = ImageUrl("https://placekitten.com/200/300"),
                    image1 = ImageUrl("https://placekitten.com/300/200"),
                    image2 = ImageUrl("https://placekitten.com/320/300"),
                    image3 = ImageUrl("https://placekitten.com/300/320"),
                ),
                modifier = Modifier
                    .padding(all = QuadSpace)
                    .size(
                        width = 300.dp,
                        height = 300.dp,
                    )
            )
        }
    }
}
