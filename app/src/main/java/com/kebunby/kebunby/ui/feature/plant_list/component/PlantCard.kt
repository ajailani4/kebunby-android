package com.kebunby.kebunby.ui.feature.plant_list.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.kebunby.kebunby.R
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.ui.theme.Grey
import compose.icons.EvaIcons
import compose.icons.SimpleIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Clock
import compose.icons.simpleicons.Rainmeter

@ExperimentalCoilApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantCard(plantItem: PlantItem?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp,
        onClick = {}
    ) {
        Row(modifier = Modifier.padding(15.dp)) {
            Image(
                modifier = Modifier
                    .size(110.dp)
                    .clip(MaterialTheme.shapes.small),
                painter = rememberImagePainter(plantItem?.image),
                contentScale = ContentScale.Crop,
                contentDescription = "Plant image"
            )
            Spacer(modifier = Modifier.width(17.dp))
            Column {
                Text(
                    text = plantItem?.name ?: "-",
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = plantItem?.category ?: "-",
                    color = Grey,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(17.dp),
                        imageVector = EvaIcons.Outline.Clock,
                        tint = Grey,
                        contentDescription = "Estimation time icon"
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = plantItem?.growthEst ?: "-",
                        color = Grey,
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(17.dp),
                        imageVector = SimpleIcons.Rainmeter,
                        tint = Grey,
                        contentDescription = "Watering frequency icon"
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = plantItem?.wateringFreq ?: "-",
                        color = Grey,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}