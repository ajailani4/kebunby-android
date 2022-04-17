package com.kebunby.kebunby.ui.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import com.kebunby.kebunby.R
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kebunby.kebunby.data.model.PlantCategory
import com.kebunby.kebunby.ui.theme.DecorativePlantColor
import com.kebunby.kebunby.ui.theme.FruitsColor
import com.kebunby.kebunby.ui.theme.VegetablesColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantCategoryCard(plantCategory: PlantCategory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp,
        onClick = {}
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .padding(5.dp),
                painter = painterResource(
                    id = when (plantCategory.id) {
                        1 -> R.drawable.ic_decorative_plant
                        2 -> R.drawable.ic_vegetables
                        3 -> R.drawable.ic_fruits
                        else -> R.drawable.ic_decorative_plant
                    }
                ),
                contentDescription = "Plant category icon"
            )
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