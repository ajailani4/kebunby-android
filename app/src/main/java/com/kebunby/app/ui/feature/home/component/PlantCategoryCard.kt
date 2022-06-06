package com.kebunby.app.ui.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kebunby.app.R
import com.kebunby.app.data.model.PlantCategory

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantCategoryCard(
    modifier: Modifier = Modifier,
    plantCategory: PlantCategory,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(30.dp),
                painter = painterResource(
                    id = when (plantCategory.id) {
                        1 -> R.drawable.ic_decorative_plant
                        2 -> R.drawable.ic_vegetables
                        3 -> R.drawable.ic_fruits
                        else -> R.drawable.ic_decorative_plant
                    }
                ),
                contentDescription = "Plant name icon"
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = plantCategory.name,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}