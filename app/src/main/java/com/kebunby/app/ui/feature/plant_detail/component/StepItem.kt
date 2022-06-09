package com.kebunby.app.ui.feature.plant_detail.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StepItem(
    number: Int,
    step: String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$number.",
            color = MaterialTheme.colors.onBackground,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.subtitle2
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = step,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.body1
        )
    }
}