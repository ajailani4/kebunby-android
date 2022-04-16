package com.kebunby.kebunby.ui.feature.home.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import com.kebunby.kebunby.R
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kebunby.kebunby.ui.theme.DecorativePlant

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantCategoryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp,
        onClick = {}
    ) {
        Row(modifier = Modifier.padding(15.dp)) {
            Surface(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                color = DecorativePlant
            ) {
                Icon(
                    modifier = Modifier
                        .size(34.dp)
                        .padding(5.dp),
                    painter = painterResource(id = R.drawable.ic_decorative),
                    tint = Color.White,
                    contentDescription = "Plant category icon"
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = "Tanaman Hias",
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}