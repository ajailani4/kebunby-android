package com.kebunby.kebunby.ui.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.common.component.CustomToolbar
import com.kebunby.kebunby.ui.feature.profile.component.CountingText
import com.kebunby.kebunby.ui.theme.Grey
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.LogOut

@Composable
fun ProfileScreen(navController: NavController) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomToolbar(
                navController = navController,
                title = stringResource(id = R.string.profile),
                hasMenuIcon = true,
                menuIcons = listOf(
                    Pair(
                        {
                            Icon(
                                imageVector = EvaIcons.Fill.LogOut,
                                tint = Color.White,
                                contentDescription = "Logout icon"
                            )
                        },
                        {}
                    )
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            ProfileHeader()
        }
    }
}

@Composable
fun ProfileHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(20.dp)
    ) {
        Image(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            painter = /*rememberImagePainter("")*/painterResource(id = R.drawable.img_default_ava),
            contentDescription = "User avatar"
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text =  "George Zayvich",
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = "george_z",
                    color = Grey,
                    style = MaterialTheme.typography.body1
                )
            }
            Row {
                CountingText(count = 10, text = "Planting")
                Spacer(modifier = Modifier.width(20.dp))
                CountingText(count = 20, text = "Planted")
            }
        }
    }
}