package com.kebunby.app.ui.feature.profile.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.kebunby.app.ui.theme.Grey

@Composable
fun CountingText(count: Int, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$count",
            color = MaterialTheme.colors.onBackground,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.h4
        )
        Text(
            text = text,
            color = Grey,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1
        )
    }
}