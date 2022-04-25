package com.kebunby.kebunby.ui.feature.plant_list.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kebunby.kebunby.R
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.ui.theme.Grey
import compose.icons.EvaIcons
import compose.icons.SimpleIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Clock
import compose.icons.simpleicons.Rainmeter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantCard(plantItem: PlantItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp,
        onClick = {}
    ) {
        Row(modifier = Modifier.padding(15.dp)) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = /*rememberImagePainter()*/painterResource(id = R.drawable.img_default_ava),
                contentScale = ContentScale.Crop,
                contentDescription = "Plant image"
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(
                    text = plantItem.name,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = plantItem.category,
                    color = Grey,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(15.dp))
                Row {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = EvaIcons.Outline.Clock,
                        tint = Grey,
                        contentDescription = "Estimation time icon"
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = plantItem.growthEst,
                        color = Grey,
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Row {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = SimpleIcons.Rainmeter,
                        tint = Grey,
                        contentDescription = "Watering frequency icon"
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = plantItem.wateringFreq,
                        color = Grey,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPlantCard() {
    PlantCard(
        plantItem = PlantItem(
            id = 1,
            name = "Tanaman",
            image = "test",
            category = "Tanaman Hias",
            growthEst = "2-3 Tahun",
            wateringFreq = "3x Sehari",
            popularity = 10,
            isFavorited = true
        )
    )
}