package com.kebunby.kebunby.ui.common.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.kebunby.kebunby.R

@Composable
fun CustomAlertDialog(
    onVisibilityChanged: (Boolean) -> Unit,
    title: String,
    message: String,
    onConfirmed: () -> Unit,
    onDismissed: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onVisibilityChanged(false) },
        title = {
            Text(
                text = title,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h3
            )
        },
        text = {
            Text(
                text = message,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.subtitle1
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmed) {
                Text(
                    text = stringResource(R.string.yes),
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.body1
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissed) {
                Text(
                    text = stringResource(R.string.no),
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.body1
                )
            }
        },
        backgroundColor = MaterialTheme.colors.background
    )
}