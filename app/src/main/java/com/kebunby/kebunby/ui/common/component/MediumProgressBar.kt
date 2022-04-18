package com.kebunby.kebunby.ui.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A medium size progress bar with top 70dp padding
 * */
@Composable
fun MediumProgressBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 70.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.size(60.dp))
    }
}