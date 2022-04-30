package com.francescsoftware.mosaic.ui.mosaic.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.francescsoftware.mosaic.R
import com.francescsoftware.mosaic.core.wrapper.ImageUrl
import com.francescsoftware.mosaic.ui.shared.theme.MosaicTheme
import com.francescsoftware.mosaic.ui.shared.theme.QuadSpace
import com.francescsoftware.mosaic.ui.shared.theme.SingleSpace

@Composable
fun Tile(
    imageUrl: ImageUrl,
    modifier: Modifier = Modifier,
    border: Dp = SingleSpace,
) {
    Box(
        modifier = modifier.border(
            border = BorderStroke(
                width = border,
                color = MaterialTheme.colors.surface,
            )
        )
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl.url)
                .build(),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_downloading_24),
        )
    }
}

@Preview
@Composable
private fun PreviewTile() {
    MosaicTheme {
        Surface(
            color = MaterialTheme.colors.background,
        ) {
            Tile(
                imageUrl = ImageUrl(url = "https://placekitten.com/300/300"),
                modifier = Modifier
                    .padding(all = QuadSpace)
                    .size(
                        width = 150.dp,
                        height = 150.dp,
                    )
            )
        }
    }
}
