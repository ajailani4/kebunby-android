package com.kebunby.kebunby.ui.feature.profile.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.kebunby.kebunby.ui.theme.Grey

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
            style = MaterialTheme.typography.body1
        )
    }
}