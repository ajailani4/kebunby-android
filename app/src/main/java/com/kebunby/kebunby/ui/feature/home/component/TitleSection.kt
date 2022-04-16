package com.kebunby.kebunby.ui.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.theme.Grey

@Composable
fun TitleSection(
    title: String,
    isViewAllEnabled: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = MaterialTheme.colors.onBackground,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.h3
        )

        if (isViewAllEnabled) {
            Text(
                text = stringResource(id = R.string.view_all),
                color = Grey,
                style = MaterialTheme.typography.body2
            )
        }
    }
}