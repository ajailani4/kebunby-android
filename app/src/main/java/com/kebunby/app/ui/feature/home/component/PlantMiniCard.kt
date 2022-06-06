package com.kebunby.app.ui.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Heart
import compose.icons.evaicons.outline.Heart

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantMiniCard(
    plantItem: PlantItem,
    onFavorited: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp,
        onClick = onClick
    ) {
        Column {
            Box {
                Image(
                    modifier = Modifier.size(160.dp),
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(plantItem.image)
                            .placeholder(R.drawable.img_empty_plant)
                            .build()
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Plant image"
                )
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = CircleShape,
                    color = MaterialTheme.colors.background,
                    onClick = onFavorited
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
                        tint = if (plantItem.isFavorited) Red else Grey,
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
                    text = plantItem.category.name,
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