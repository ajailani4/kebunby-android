package com.kebunby.kebunby.ui.feature.plant_detail.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kebunby.kebunby.ui.theme.Grey

@Composable
fun TopMenuButton(
    icon: ImageVector,
    tint: Color,
    contentDescription: String,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.size(42.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
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