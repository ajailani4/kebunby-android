package com.kebunby.kebunby.ui.feature.plant_detail.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun InfoSurface(
    icon: ImageVector,
    info: String
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colors.secondaryVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = icon,
                tint = Color.White,
                contentDescription = "Info icon"
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = info,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.body2
            )
        }
    }
}