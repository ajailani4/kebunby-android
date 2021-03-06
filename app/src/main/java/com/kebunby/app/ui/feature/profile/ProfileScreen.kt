package com.kebunby.app.ui.feature.profile

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
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kebunby.app.R
import com.kebunby.app.data.model.User
import com.kebunby.app.ui.Screen
import com.kebunby.app.ui.common.SharedViewModel
import com.kebunby.app.ui.common.UIState
import com.kebunby.app.ui.common.component.CustomAlertDialog
import com.kebunby.app.ui.common.component.CustomToolbar
import com.kebunby.app.ui.common.component.FullSizeProgressBar
import com.kebunby.app.ui.feature.profile.component.CountingText
import com.kebunby.app.ui.feature.profile.component.ProfileHeaderShimmer
import com.kebunby.app.ui.feature.profile.planted.PlantedScreen
import com.kebunby.app.ui.feature.profile.planting.PlantingScreen
import com.kebunby.app.ui.feature.profile.uploaded.UploadedScreen
import com.kebunby.app.ui.theme.Grey
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.LogOut
import compose.icons.evaicons.outline.Plus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel
) {
    val onEvent = profileViewModel::onEvent
    val userProfileState = profileViewModel.userProfileState.value
    val logoutState = profileViewModel.logoutState.value
    val swipeRefreshing = profileViewModel.swipeRefreshing.value
    val onSwipeRefreshingChanged = profileViewModel::onSwipeRefreshingChanged
    val logoutConfirmDlgVis = profileViewModel.logoutConfirmDlgVis.value
    val onLogoutConfirmDlgVisChanged = profileViewModel::onLogoutConfirmDlgVisChanged

    val isReloaded = sharedViewModel.isReloaded.value
    val onReload = sharedViewModel::onReload

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

    val cameraPermissionState =
        rememberPermissionState(android.Manifest.permission.CAMERA)
    val readExStoragePermissionState =
        rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomToolbar(
                title = stringResource(id = R.string.profile),
                hasMenuIcon = true,
                menuIcons = listOf(
                    Pair(
                        {
                            Icon(
                                imageVector = EvaIcons.Outline.Plus,
                                tint = MaterialTheme.colors.onPrimary,
                                contentDescription = "Upload icon"
                            )
                        },
                        {
                            when {
                                cameraPermissionState.hasPermission && readExStoragePermissionState.hasPermission -> {
                                    navController.navigate(Screen.UploadEditPlantScreen.route)
                                }

                                cameraPermissionState.shouldShowRationale ||
                                    !cameraPermissionState.permissionRequested -> {
                                    cameraPermissionState.launchPermissionRequest()
                                }

                                readExStoragePermissionState.shouldShowRationale ||
                                    !readExStoragePermissionState.permissionRequested -> {
                                    readExStoragePermissionState.launchPermissionRequest()
                                }
                            }
                        }
                    ),
                    Pair(
                        {
                            Icon(
                                imageVector = EvaIcons.Outline.LogOut,
                                tint = MaterialTheme.colors.onPrimary,
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
                        onReload = onReload,
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
                                        isRefreshing = swipeRefreshing || isReloaded
                                    )
                                }

                                1 -> {
                                    PlantedScreen(
                                        navController = navController,
                                        isRefreshing = swipeRefreshing || isReloaded
                                    )
                                }

                                2 -> {
                                    UploadedScreen(
                                        navController = navController,
                                        isRefreshing = swipeRefreshing || isReloaded
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

        // Observe is reloaded state
        if (isReloaded) onEvent(ProfileEvent.LoadUserProfile)
    }
}

@Composable
fun ProfileHeader(
    onEvent: (ProfileEvent) -> Unit,
    userProfileState: UIState<User>,
    onSwipeRefreshingChanged: (Boolean) -> Unit,
    onReload: (Boolean) -> Unit,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope
) {
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        when (userProfileState) {
            is UIState.Loading -> {
                ProfileHeaderShimmer()
            }

            is UIState.Success -> {
                onSwipeRefreshingChanged(false)
                onReload(false)

                val user = userProfileState.data

                Image(
                    modifier = Modifier
                        .size(85.dp)
                        .clip(CircleShape),
                    painter = if (user?.avatar != null) {
                        rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user.avatar)
                                .placeholder(R.drawable.img_default_ava)
                                .build()
                        )
                    } else {
                        painterResource(id = R.drawable.img_default_ava)
                    },
                    contentScale = ContentScale.Crop,
                    contentDescription = "User avatar"
                )
                Spacer(modifier = Modifier.width(25.dp))
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    if (user != null) {
                        Column {
                            Text(
                                text = user.name,
                                color = MaterialTheme.colors.onBackground,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.h4
                            )
                            Text(
                                text = user.username,
                                color = Grey,
                                style = MaterialTheme.typography.body1
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CountingText(
                                count = user.planting,
                                text = stringResource(R.string.planting)
                            )
                            CountingText(
                                count = user.planted,
                                text = stringResource(R.string.planted)
                            )
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