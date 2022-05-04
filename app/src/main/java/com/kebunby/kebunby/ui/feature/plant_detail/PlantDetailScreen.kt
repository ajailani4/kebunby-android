package com.kebunby.kebunby.ui.feature.plant_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.feature.plant_detail.component.InfoSurface
import com.kebunby.kebunby.ui.feature.plant_detail.component.StepItem
import com.kebunby.kebunby.ui.theme.Grey
import com.kebunby.kebunby.ui.theme.Red
import compose.icons.EvaIcons
import compose.icons.SimpleIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.ArrowBack
import compose.icons.evaicons.fill.Heart
import compose.icons.evaicons.outline.Clock
import compose.icons.evaicons.outline.Heart
import compose.icons.simpleicons.Rainmeter

@Composable
fun PlantDetailScreen(navController: NavController) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            // Plant Image
            Box(
                modifier = Modifier.weight(2f)
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = /*rememberImagePainter()*/painterResource(id = R.drawable.img_default_ava),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Plant image"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        modifier = Modifier
                            .background(color = Color.White, shape = CircleShape),
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = EvaIcons.Fill.ArrowBack,
                            tint = Grey,
                            contentDescription = "Back button"
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .background(color = Color.White, shape = CircleShape),
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = EvaIcons.Outline.Heart,
                            tint = Grey,
                            contentDescription = "Love button"
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(4f)
                    .padding(20.dp)
            ) {
                // Short Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Montserra Obliqua",
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.h3
                        )
                        Text(
                            text = "Tanaman Hias",
                            color = Grey,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Row {
                        Icon(
                            imageVector = EvaIcons.Fill.Heart,
                            tint = Red,
                            contentDescription = "Heart icon"
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "100",
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row {
                    InfoSurface(
                        icon = EvaIcons.Outline.Clock,
                        info = "3-5 Tahun"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    InfoSurface(
                        icon = SimpleIcons.Rainmeter,
                        info = "2x Sehari"
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                // Tools and Materials
                Text(
                    text = stringResource(id = R.string.tools_and_materials),
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Pot 1 buah",
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(20.dp))
                // Steps
                Text(
                    text = stringResource(id = R.string.steps),
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.padding(5.dp))
                StepItem(number = 1, step = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = MaterialTheme.shapes.medium,
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = stringResource(id = R.string.plant),
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}