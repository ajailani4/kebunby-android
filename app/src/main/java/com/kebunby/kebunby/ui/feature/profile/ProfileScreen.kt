package com.kebunby.kebunby.ui.feature.profile

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.common.component.CustomToolbar
import com.kebunby.kebunby.ui.feature.profile.component.CountingText
import com.kebunby.kebunby.ui.feature.profile.planted.PlantedScreen
import com.kebunby.kebunby.ui.feature.profile.planting.PlantingScreen
import com.kebunby.kebunby.ui.theme.Grey
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.LogOut
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val profileTabMenus = listOf(
        stringResource(R.string.planting),
        stringResource(R.string.planted),
        stringResource(R.string.uploaded)
    )

    val pagerState = rememberPagerState(initialPage = 0)
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return if (available.y > 0) {
                    Offset.Zero
                } else {
                    Offset(
                        x = 0f,
                        y = -scrollState.dispatchRawDelta(-available.y)
                    )
                }
            }
        }
    }

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
        /*LazyColumn(
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            item {
                ProfileHeader()
            }

            stickyHeader {
                ProfileTab(
                    menus = profileTabMenus,
                    selectedTabIndex = pagerState.currentPage,
                    onTabSelected = { index ->
                        coroutineScope.launch {
                            pagerState.scrollToPage(index)
                        }
                    }
                )
            }

            item {
                HorizontalPager(
                    count = profileTabMenus.size,
                    state = pagerState
                ) { index ->
                    when (index) {
                        0 -> PlantingScreen(navController)
                        1 -> PlantedScreen(navController)
                        2 -> {}
                    }
                }
            }
        }*/
        BoxWithConstraints {
            val screenHeight = maxHeight

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                ProfileHeader()
                Column(modifier = Modifier.height(screenHeight)) {
                    ProfileTab(
                        menus = profileTabMenus,
                        selectedTabIndex = pagerState.currentPage,
                        onTabSelected = { index ->
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        }
                    )
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxHeight()
                            .nestedScroll(nestedScrollConnection),
                        count = profileTabMenus.size,
                        state = pagerState
                    ) { page ->
                        when (page) {
                            0 -> PlantingScreen(navController)
                            1 -> PlantedScreen(navController)
                            2 -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileHeader() {
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(20.dp)
    ) {
        Image(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape),
            painter = /*rememberImagePainter("")*/painterResource(id = R.drawable.img_default_ava),
            contentDescription = "User avatar"
        )
        Spacer(modifier = Modifier.width(25.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "George Zayvich",
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
                CountingText(count = 10, text = stringResource(R.string.planting))
                Spacer(modifier = Modifier.width(20.dp))
                CountingText(count = 20, text = stringResource(R.string.planted))
            }
        }
    }
}

@Composable
fun ProfileTab(
    menus: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = MaterialTheme.colors.background,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                height = 3.dp,
                color = MaterialTheme.colors.primary
            )
        }
    ) {
        menus.forEachIndexed { index, menu ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = menu,
                        color = if (selectedTabIndex == index) {
                            MaterialTheme.colors.primary
                        } else {
                            Grey
                        },
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            )
        }
    }
}