package com.francescsoftware.mosaic.ui.mosaic.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.francescsoftware.mosaic.R
import com.francescsoftware.mosaic.ui.shared.theme.SingleSpace

@Composable
fun LoadError(
    retry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.loading_error),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(bottom = SingleSpace)
            )
            Button(
                onClick = retry,
                modifier = Modifier.padding(top = SingleSpace),
            ) {
                Text(
                    text = stringResource(R.string.retry),
                )
            }
        }
    }
}
