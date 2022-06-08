package com.kebunby.app.ui.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kebunby.app.R
import com.kebunby.app.data.model.PlantItem
import com.kebunby.app.ui.theme.Grey
import com.kebunby.app.ui.theme.Red
import compose.icons.EvaIcons
import compose.icons.SimpleIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Heart
import compose.icons.evaicons.outline.Clock
import compose.icons.simpleicons.Rainmeter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantCard(
    plantItem: PlantItem?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp,
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(15.dp)) {
            Image(
                modifier = Modifier
                    .size(130.dp)
                    .clip(MaterialTheme.shapes.small),
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(plantItem?.image)
                        .placeholder(R.drawable.img_empty_plant)
                        .build()
                ),
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
                    style = MaterialTheme.typography.h4
                )
                Text(
                    text = plantItem?.category?.name ?: "-",
                    color = Grey,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(10.dp))
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(17.dp),
                        imageVector = EvaIcons.Fill.Heart,
                        tint = Red,
                        contentDescription = "Popularity icon"
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "${plantItem?.popularity}",
                        color = Grey,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}