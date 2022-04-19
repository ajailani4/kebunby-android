package com.kebunby.kebunby.ui.feature.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.kebunby.kebunby.R
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.ui.feature.home.component.HomeUserProfileShimmer
import com.kebunby.kebunby.ui.feature.home.component.PlantCategoryCard
import com.kebunby.kebunby.ui.feature.home.component.PlantMiniCard
import com.kebunby.kebunby.ui.feature.home.component.TitleSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoilApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val onEvent = homeViewModel::onEvent
    val userProfileState = homeViewModel.userProfileState
    val trendingPlantsState = homeViewModel.trendingPlantsState
    val forBeginnerPlantsState = homeViewModel.forBeginnerPlantsState
    val plantCategoriesState = homeViewModel.plantCategoriesState

    val trendingPlants = homeViewModel.trendingPlants
    val setTrendingPlants = homeViewModel::setTrendingPlants
    val updateTrendingPlants = homeViewModel::updateTrendingPlants

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
                HomeHeader(
                    userProfileState = userProfileState,
                    coroutineScope = coroutineScope,
                    scaffoldState = scaffoldState
                )
                HomeContent(
                    trendingPlantsState = trendingPlantsState,
                    forBeginnerPlantsState = forBeginnerPlantsState,
                    plantCategoriesState = plantCategoriesState,
                    trendingPlants = trendingPlants,
                    setTrendingPlants = setTrendingPlants,
                    updateTrendingPlants = updateTrendingPlants,
                    coroutineScope = coroutineScope,
                    scaffoldState = scaffoldState
                )
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun HomeHeader(
    userProfileState: HomeState,
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
            is HomeState.LoadingUserProfile -> {
                HomeUserProfileShimmer()
            }

            is HomeState.UserProfile -> {
                val user = userProfileState.user

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
                        style = MaterialTheme.typography.h3
                    )
                }
                Image(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    painter = if (user?.avatar != null) {
                        rememberImagePainter(user.avatar)
                    } else {
                        painterResource(id = R.drawable.img_default_ava)
                    },
                    contentScale = ContentScale.Crop,
                    contentDescription = "User avatar"
                )
            }

            is HomeState.FailUserProfile -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        userProfileState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }
            }

            is HomeState.ErrorUserProfile -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        userProfileState.message?.let { message ->
                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@ExperimentalCoilApi
@Composable
fun HomeContent(
    trendingPlantsState: HomeState,
    forBeginnerPlantsState: HomeState,
    plantCategoriesState: HomeState,
    trendingPlants: List<PlantItem>,
    setTrendingPlants: (List<PlantItem>) -> Unit,
    updateTrendingPlants: (Int, PlantItem) -> Unit,
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
                trendingPlantsState = trendingPlantsState,
                trendingPlants = trendingPlants,
                setTrendingPlants = setTrendingPlants,
                updateTrendingPlants = updateTrendingPlants,
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState
            )
            Spacer(modifier = Modifier.height(20.dp))
            ForBeginnerSection(
                forBeginnerPlantsState = forBeginnerPlantsState,
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState
            )
            Spacer(modifier = Modifier.height(20.dp))
            PlantCategorySection(
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
    trendingPlantsState: HomeState,
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
        isViewAllEnabled = true
    )
    Spacer(modifier = Modifier.height(10.dp))

    // Observe trending plants
    when (trendingPlantsState) {
        is HomeState.LoadingTrendingPlants -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is HomeState.TrendingPlants -> {
            if (trendingPlantsState.plants != null) {
                setTrendingPlants(trendingPlantsState.plants)

                LazyRow(contentPadding = PaddingValues(horizontal = 20.dp)) {
                    itemsIndexed(trendingPlants) { i, plantItem ->
                        PlantMiniCard(plantItem) {
                            updateTrendingPlants(i, plantItem.copy(isFavorited = !plantItem.isFavorited))
                        }

                        if (plantItem != trendingPlants.last()) {
                            Spacer(modifier = Modifier.width(18.dp))
                        }
                    }
                }
            }
        }

        is HomeState.FailTrendingPlants -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    trendingPlantsState.message?.let { message ->
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }
            }
        }

        is HomeState.ErrorTrendingPlants -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    trendingPlantsState.message?.let { message ->
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }
            }
        }

        else -> {}
    }
}

@ExperimentalCoilApi
@Composable
fun ForBeginnerSection(
    forBeginnerPlantsState: HomeState,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    TitleSection(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        title = stringResource(id = R.string.for_beginner),
        isViewAllEnabled = true
    )
    Spacer(modifier = Modifier.height(10.dp))

    // Observe for beginner plants
    when (forBeginnerPlantsState) {
        is HomeState.LoadingForBeginnerPlants -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is HomeState.ForBeginnerPlants -> {
            val forBeginnerPlants = forBeginnerPlantsState.plants

            if (forBeginnerPlants != null) {
                LazyRow(contentPadding = PaddingValues(horizontal = 20.dp)) {
                    items(forBeginnerPlants) { plantItem ->
                        PlantMiniCard(plantItem) {}

                        if (plantItem != forBeginnerPlants.last()) {
                            Spacer(modifier = Modifier.width(18.dp))
                        }
                    }
                }
            }
        }

        is HomeState.FailForBeginnerPlants -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    forBeginnerPlantsState.message?.let { message ->
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }
            }
        }

        is HomeState.ErrorForBeginnerPlants -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    forBeginnerPlantsState.message?.let { message ->
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }
            }
        }

        else -> {}
    }
}

@Composable
fun PlantCategorySection(
    plantCategoriesState: HomeState,
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
        is HomeState.LoadingPlantCategories -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is HomeState.PlantCategories -> {
            val plantCategories = plantCategoriesState.plantCategories

            plantCategories?.forEach { plantCategory ->
                PlantCategoryCard(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    plantCategory = plantCategory
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        is HomeState.FailPlantCategories -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    plantCategoriesState.message?.let { message ->
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }
            }
        }

        is HomeState.ErrorPlantCategories -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    plantCategoriesState.message?.let { message ->
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }
            }
        }

        else -> {}
    }
}