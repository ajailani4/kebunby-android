package com.kebunby.kebunby.ui.common.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CircleIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    tint: Color,
    backgroundColor: Color,
    contentDescription: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            tint = tint,
            contentDescription = contentDescription
        )
    }
}