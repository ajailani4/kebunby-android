package com.kebunby.app.ui.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kebunby.app.R
import com.kebunby.app.data.model.PlantCategory
import com.kebunby.app.data.model.PlantItem
import com.kebunby.app.data.model.User
import com.kebunby.app.ui.Screen
import com.kebunby.app.ui.common.SharedViewModel
import com.kebunby.app.ui.common.UIState
import com.kebunby.app.ui.feature.home.component.HomeHeaderShimmer
import com.kebunby.app.ui.feature.home.component.PlantCategoryCard
import com.kebunby.app.ui.feature.home.component.PlantMiniCard
import com.kebunby.app.ui.feature.home.component.TitleSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoilApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel
) {
    val onEvent = homeViewModel::onEvent
    val userProfileState = homeViewModel.userProfileState.value
    val trendingPlantsState = homeViewModel.trendingPlantsState.value
    val forBeginnerPlantsState = homeViewModel.forBeginnerPlantsState.value
    val plantCategoriesState = homeViewModel.plantCategoriesState.value
    val addFavPlantState = homeViewModel.addFavPlantState.value
    val deleteFavPlantState = homeViewModel.deleteFavPlantState.value

    val swipeRefreshing = homeViewModel.swipeRefreshing.value
    val onSwipeRefreshingChanged = homeViewModel::onSwipeRefreshingChanged
    val onSelectedPlantChanged = homeViewModel::onSelectedPlantChanged
    val trendingPlants = homeViewModel.trendingPlants
    val setTrendingPlants = homeViewModel::setTrendingPlants
    val updateTrendingPlants = homeViewModel::updateTrendingPlants
    val clearTrendingPlants = homeViewModel::clearTrendingPlants
    val forBeginnerPlants = homeViewModel.forBeginnerPlants
    val setForBeginnerPlants = homeViewModel::setForBeginnerPlants
    val updateForBeginnerPlants = homeViewModel::updateForBeginnerPlants
    val clearForBeginnerPlants = homeViewModel::clearForBeginnerPlants

    val isReloaded = sharedViewModel.isReloaded.value
    val onReload = sharedViewModel::onReload

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = swipeRefreshing),
            onRefresh = {
                onSwipeRefreshingChanged(true)

                // Clear local plant list
                clearTrendingPlants()
                clearForBeginnerPlants()

                onEvent(HomeEvent.LoadUserProfile)
                onEvent(HomeEvent.LoadTrendingPlants)
                onEvent(HomeEvent.LoadForBeginnerPlants)
                onEvent(HomeEvent.LoadPlantCategories)
            },
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    contentColor = MaterialTheme.colors.primary
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
                    HomeHeader(
                        onEvent = onEvent,
                        userProfileState = userProfileState,
                        onSwipeRefreshingChanged = onSwipeRefreshingChanged,
                        onReload = onReload,
                        coroutineScope = coroutineScope,
                        scaffoldState = scaffoldState
                    )
                    HomeContent(
                        navController = navController,
                        onEvent = onEvent,
                        trendingPlantsState = trendingPlantsState,
                        forBeginnerPlantsState = forBeginnerPlantsState,
                        plantCategoriesState = plantCategoriesState,
                        onSelectedPlantChanged = onSelectedPlantChanged,
                        trendingPlants = trendingPlants,
                        setTrendingPlants = setTrendingPlants,
                        updateTrendingPlants = updateTrendingPlants,
                        forBeginnerPlants = forBeginnerPlants,
                        setForBeginnerPlants = setForBeginnerPlants,
                        updateForBeginnerPlants = updateForBeginnerPlants,
                        coroutineScope = coroutineScope,
                        scaffoldState = scaffoldState
                    )
                }
            }
        }

        // Observe add favorite plant state
        when (addFavPlantState) {
            is UIState.Error -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        addFavPlantState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }

                onEvent(HomeEvent.Idle)
            }

            else -> {}
        }

        // Observe delete favorite plant state
        when (deleteFavPlantState) {
            is UIState.Error -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        deleteFavPlantState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }

                onEvent(HomeEvent.Idle)
            }

            else -> {}
        }

        // Observe is reloaded state
        if (isReloaded) {
            // Clear local plant list
            clearTrendingPlants()
            clearForBeginnerPlants()

            onEvent(HomeEvent.LoadUserProfile)
            onEvent(HomeEvent.LoadTrendingPlants)
            onEvent(HomeEvent.LoadForBeginnerPlants)
            onEvent(HomeEvent.LoadPlantCategories)
        }
    }
}

@Composable
fun HomeHeader(
    onEvent: (HomeEvent) -> Unit,
    userProfileState: UIState<User>,
    onSwipeRefreshingChanged: (Boolean) -> Unit,
    onReload: (Boolean) -> Unit,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Observe user profile state
        when (userProfileState) {
            is UIState.Loading -> {
                HomeHeaderShimmer()
            }

            is UIState.Success -> {
                onSwipeRefreshingChanged(false)
                onReload(false)

                val user = userProfileState.data

                Column {
                    if (user != null) {
                        Text(
                            text = "${stringResource(id = R.string.hello)}, ${user.name.split(" ")[0]}",
                            color = MaterialTheme.colors.onPrimary,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.h2
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.have_a_nice_day),
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h4
                    )
                }
                Image(
                    modifier = Modifier
                        .size(60.dp)
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

                onEvent(HomeEvent.Idle)
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

                onEvent(HomeEvent.Idle)
            }

            else -> {}
        }
    }
}

@ExperimentalCoilApi
@Composable
fun HomeContent(
    navController: NavController,
    onEvent: (HomeEvent) -> Unit,
    trendingPlantsState: UIState<List<PlantItem>>,
    forBeginnerPlantsState: UIState<List<PlantItem>>,
    plantCategoriesState: UIState<List<PlantCategory>>,
    onSelectedPlantChanged: (Int) -> Unit,
    trendingPlants: List<PlantItem>,
    setTrendingPlants: (List<PlantItem>) -> Unit,
    updateTrendingPlants: (Int, PlantItem) -> Unit,
    forBeginnerPlants: List<PlantItem>,
    setForBeginnerPlants: (List<PlantItem>) -> Unit,
    updateForBeginnerPlants: (Int, PlantItem) -> Unit,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(topEnd = 30.dp),
        color = MaterialTheme.colors.background
    ) {
        Column {
            TrendingSection(
                navController = navController,
                onEvent = onEvent,
                trendingPlantsState = trendingPlantsState,
                onSelectedPlantChanged = onSelectedPlantChanged,
                trendingPlants = trendingPlants,
                setTrendingPlants = setTrendingPlants,
                updateTrendingPlants = updateTrendingPlants,
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState
            )
            Spacer(modifier = Modifier.height(25.dp))
            ForBeginnerSection(
                navController = navController,
                onEvent = onEvent,
                forBeginnerPlantsState = forBeginnerPlantsState,
                onSelectedPlantChanged = onSelectedPlantChanged,
                forBeginnerPlants = forBeginnerPlants,
                setForBeginnerPlants = setForBeginnerPlants,
                updateForBeginnerPlants = updateForBeginnerPlants,
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState
            )
            Spacer(modifier = Modifier.height(25.dp))
            PlantCategorySection(
                navController = navController,
                onEvent = onEvent,
                plantCategoriesState = plantCategoriesState,
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun TrendingSection(
    navController: NavController,
    onEvent: (HomeEvent) -> Unit,
    trendingPlantsState: UIState<List<PlantItem>>,
    onSelectedPlantChanged: (Int) -> Unit,
    trendingPlants: List<PlantItem>,
    setTrendingPlants: (List<PlantItem>) -> Unit,
    updateTrendingPlants: (Int, PlantItem) -> Unit,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    TitleSection(
        modifier = Modifier
            .padding(top = 20.dp)
            .padding(horizontal = 20.dp),
        title = stringResource(id = R.string.trending),
        isViewAllEnabled = true,
        onViewAllClicked = {
            navController.navigate(Screen.PlantListScreen.route + "?isTrending=true")
        }
    )
    Spacer(modifier = Modifier.height(10.dp))

    // Observe trending plants
    when (trendingPlantsState) {
        is UIState.Loading -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UIState.Success -> {
            if (trendingPlantsState.data != null) {
                if (trendingPlants.isEmpty()) {
                    setTrendingPlants(trendingPlantsState.data)
                }

                LazyRow(contentPadding = PaddingValues(horizontal = 20.dp)) {
                    itemsIndexed(trendingPlants) { i, plantItem ->
                        PlantMiniCard(
                            plantItem = plantItem,
                            onFavorited = {
                                updateTrendingPlants(
                                    i,
                                    plantItem.copy(isFavorited = !plantItem.isFavorited)
                                )
                                onSelectedPlantChanged(plantItem.id)

                                if (!plantItem.isFavorited) {
                                    onEvent(HomeEvent.AddFavoritePlant)
                                } else {
                                    onEvent(HomeEvent.DeleteFavoritePlant)
                                }
                            },
                            onClick = {
                                navController.navigate(
                                    Screen.PlantDetailScreen.route + "?plantId=${plantItem.id}"
                                )
                            }
                        )

                        if (plantItem != trendingPlants.last()) {
                            Spacer(modifier = Modifier.width(18.dp))
                        }
                    }
                }
            }
        }

        is UIState.Fail -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    trendingPlantsState.message?.let { message ->
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }
            }

            onEvent(HomeEvent.Idle)
        }

        is UIState.Error -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    trendingPlantsState.message?.let { message ->
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }
            }

            onEvent(HomeEvent.Idle)
        }

        else -> {}
    }
}

@ExperimentalCoilApi
@Composable
fun ForBeginnerSection(
    navController: NavController,
    onEvent: (HomeEvent) -> Unit,
    forBeginnerPlantsState: UIState<List<PlantItem>>,
    onSelectedPlantChanged: (Int) -> Unit,
    forBeginnerPlants: List<PlantItem>,
    setForBeginnerPlants: (List<PlantItem>) -> Unit,
    updateForBeginnerPlants: (Int, PlantItem) -> Unit,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    TitleSection(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        title = stringResource(id = R.string.for_beginner),
        isViewAllEnabled = true,
        onViewAllClicked = {
            navController.navigate(Screen.PlantListScreen.route + "?forBeginner=true")
        }
    )
    Spacer(modifier = Modifier.height(10.dp))

    // Observe for beginner plants
    when (forBeginnerPlantsState) {
        is UIState.Loading -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UIState.Success -> {
            if (forBeginnerPlantsState.data != null) {
                if (forBeginnerPlants.isEmpty()) {
                    setForBeginnerPlants(forBeginnerPlantsState.data)
                }

                LazyRow(contentPadding = PaddingValues(horizontal = 20.dp)) {
                    itemsIndexed(forBeginnerPlants) { i, plantItem ->
                        PlantMiniCard(
                            plantItem = plantItem,
                            onFavorited = {
                                updateForBeginnerPlants(
                                    i,
                                    plantItem.copy(isFavorited = !plantItem.isFavorited)
                                )
                                onSelectedPlantChanged(plantItem.id)

                                if (!plantItem.isFavorited) {
                                    onEvent(HomeEvent.AddFavoritePlant)
                                } else {
                                    onEvent(HomeEvent.DeleteFavoritePlant)
                                }
                            },
                            onClick = {
                                navController.navigate(
                                    Screen.PlantDetailScreen.route + "?plantId=${plantItem.id}"
                                )
                            }
                        )

                        if (plantItem != forBeginnerPlants.last()) {
                            Spacer(modifier = Modifier.width(18.dp))
                        }
                    }
                }
            }
        }

        is UIState.Fail -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    forBeginnerPlantsState.message?.let { message ->
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }
            }

            onEvent(HomeEvent.Idle)
        }

        is UIState.Error -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    forBeginnerPlantsState.message?.let { message ->
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }
            }

            onEvent(HomeEvent.Idle)
        }

        else -> {}
    }
}

@Composable
fun PlantCategorySection(
    navController: NavController,
    onEvent: (HomeEvent) -> Unit,
    plantCategoriesState: UIState<List<PlantCategory>>,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    TitleSection(
        modifier = Modifier.padding(horizontal = 20.dp),
        title = stringResource(id = R.string.plant_categories)
    )
    Spacer(modifier = Modifier.height(10.dp))

    // Observe plant categories state
    when (plantCategoriesState) {
        is UIState.Loading -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UIState.Success -> {
            val plantCategories = plantCategoriesState.data

            plantCategories?.forEach { plantCategory ->
                PlantCategoryCard(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    plantCategory = plantCategory,
                    onClick = {
                        navController.navigate(
                            Screen.PlantListScreen.route + "?categoryId=${plantCategory.id}&name=${plantCategory.name}"
                        )
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        is UIState.Fail -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    plantCategoriesState.message?.let { message ->
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }
            }

            onEvent(HomeEvent.Idle)
        }

        is UIState.Error -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    plantCategoriesState.message?.let { message ->
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }
            }

            onEvent(HomeEvent.Idle)
        }

        else -> {}
    }
}