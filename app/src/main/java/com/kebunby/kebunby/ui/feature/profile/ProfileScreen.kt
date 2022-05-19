package com.kebunby.kebunby.ui.feature.profile

import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.common.component.CustomToolbar
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
        
    }
}