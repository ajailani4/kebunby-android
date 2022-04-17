package com.kebunby.kebunby.ui.feature.home.component

import com.kebunby.kebunby.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.ui.theme.Grey
import com.kebunby.kebunby.ui.theme.Red
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Heart
import compose.icons.evaicons.outline.Heart

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantMiniCard(plantItem: PlantItem) {
    Card(
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp,
        onClick = {}
    ) {
        Column {
            Box {
                Image(
                    modifier = Modifier.size(170.dp),
                    painter = /*rememberImagePainter(plantItem.image)*/painterResource(id = R.drawable.img_default_ava),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Plant image"
                )
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd),
                    shape = CircleShape,
                    color = Color.White
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(5.dp)
                            .size(20.dp),
                        imageVector = if (plantItem.isFavorited) {
                            EvaIcons.Fill.Heart
                        } else {
                            EvaIcons.Outline.Heart
                        },
                        tint = if (plantItem.isFavorited) Grey else Red,
                        contentDescription = "Love icon"
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 15.dp)
                    .padding(horizontal = 15.dp)
            ) {
                Text(
                    text = plantItem.category,
                    color = Grey,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = plantItem.name,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}