package com.francescsoftware.mosaic.ui.mosaic.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.francescsoftware.mosaic.R
import com.francescsoftware.mosaic.ui.shared.widget.ProgressIndicator

@Composable
fun LoadingMosaic(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        ProgressIndicator(
            modifier = Modifier.requiredSize(256.dp)
        )
        Text(
            text = stringResource(R.string.loading),
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primary,
        )
    }
}
