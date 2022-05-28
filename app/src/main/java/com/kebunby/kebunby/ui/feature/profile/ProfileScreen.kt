package com.kebunby.kebunby.ui.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kebunby.kebunby.R
import com.kebunby.kebunby.data.model.User
import com.kebunby.kebunby.ui.Screen
import com.kebunby.kebunby.ui.common.UIState
import com.kebunby.kebunby.ui.common.component.CustomAlertDialog
import com.kebunby.kebunby.ui.common.component.CustomToolbar
import com.kebunby.kebunby.ui.common.component.FullSizeProgressBar
import com.kebunby.kebunby.ui.feature.profile.component.CountingText
import com.kebunby.kebunby.ui.feature.profile.component.ProfileHeaderShimmer
import com.kebunby.kebunby.ui.feature.profile.planted.PlantedScreen
import com.kebunby.kebunby.ui.feature.profile.planting.PlantingScreen
import com.kebunby.kebunby.ui.feature.profile.uploaded.UploadedScreen
import com.kebunby.kebunby.ui.theme.Grey
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.LogOut
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val onEvent = profileViewModel::onEvent
    val userProfileState = profileViewModel.userProfileState
    val logoutState = profileViewModel.logoutState
    val swipeRefreshing = profileViewModel.swipeRefreshing
    val onSwipeRefreshingChanged = profileViewModel::onSwipeRefreshingChanged
    val logoutConfirmDlgVis = profileViewModel.logoutConfirmDlgVis
    val onLogoutConfirmDlgVisChanged = profileViewModel::onLogoutConfirmDlgVisChanged

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
                        { onLogoutConfirmDlgVisChanged(true) }
                    )
                )
            )
        }
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = swipeRefreshing),
            onRefresh = {
                onSwipeRefreshingChanged(true)
                onEvent(ProfileEvent.LoadUserProfile)
            },
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    contentColor = MaterialTheme.colors.primary
                )
            }
        ) {
            BoxWithConstraints {
                val screenHeight = maxHeight

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    ProfileHeader(
                        onEvent = onEvent,
                        userProfileState = userProfileState,
                        onSwipeRefreshingChanged = onSwipeRefreshingChanged,
                        scaffoldState = scaffoldState,
                        coroutineScope = coroutineScope
                    )
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
                                0 -> {
                                    PlantingScreen(
                                        navController = navController,
                                        isRefreshing = swipeRefreshing
                                    )
                                }

                                1 -> {
                                    PlantedScreen(
                                        navController = navController,
                                        isRefreshing = swipeRefreshing
                                    )
                                }

                                2 -> {
                                    UploadedScreen(
                                        navController = navController,
                                        isRefreshing = swipeRefreshing
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Logout confirmation dialog
        if (logoutConfirmDlgVis) {
            CustomAlertDialog(
                onVisibilityChanged = onLogoutConfirmDlgVisChanged,
                title = stringResource(R.string.logout),
                message = stringResource(R.string.logout_confirm_msg),
                onConfirmed = {
                    onLogoutConfirmDlgVisChanged(true)
                    onEvent(ProfileEvent.Logout)
                },
                onDismissed = { onLogoutConfirmDlgVisChanged(false) }
            )
        }

        // Observe logout state
        when (logoutState) {
            is UIState.Loading -> {
                FullSizeProgressBar()
            }

            is UIState.Success -> {
                navController.navigate(Screen.OnboardingScreen.route) {
                    launchSingleTop = true

                    popUpTo(Screen.HomeScreen.route) {
                        inclusive = true
                    }
                }
            }

            else -> {}
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileHeader(
    onEvent: (ProfileEvent) -> Unit,
    userProfileState: UIState<User>,
    onSwipeRefreshingChanged: (Boolean) -> Unit,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope
) {
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(20.dp)
    ) {
        when (userProfileState) {
            is UIState.Loading -> {
                ProfileHeaderShimmer()
            }

            is UIState.Success -> {
                onSwipeRefreshingChanged(false)

                val user = userProfileState.data

                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    painter = if (user?.avatar != null) {
                        rememberImagePainter(user.avatar)
                    } else {
                        painterResource(id = R.drawable.img_default_ava)
                    },
                    contentScale = ContentScale.Crop,
                    contentDescription = "User avatar"
                )
                Spacer(modifier = Modifier.width(25.dp))
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    if (user != null) {
                        Column {
                            Text(
                                text = user.name,
                                color = MaterialTheme.colors.onBackground,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.h3
                            )
                            Text(
                                text = user.username,
                                color = Grey,
                                style = MaterialTheme.typography.body1
                            )
                        }
                        Row {
                            CountingText(
                                count = user.planting,
                                text = stringResource(R.string.planting)
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            CountingText(
                                count = user.planted,
                                text = stringResource(R.string.planted)
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            CountingText(
                                count = user.uploaded,
                                text = stringResource(R.string.uploaded)
                            )
                        }
                    }
                }
            }

            is UIState.Fail -> {
                onSwipeRefreshingChanged(false)

                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        userProfileState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }

                onEvent(ProfileEvent.Idle)
            }

            is UIState.Error -> {
                onSwipeRefreshingChanged(false)

                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        userProfileState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }

                onEvent(ProfileEvent.Idle)
            }

            else -> {}
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