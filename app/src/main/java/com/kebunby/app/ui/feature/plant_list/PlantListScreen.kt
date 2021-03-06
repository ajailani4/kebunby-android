package com.kebunby.app.ui.feature.plant_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kebunby.app.R
import com.kebunby.app.ui.Screen
import com.kebunby.app.ui.common.SharedViewModel
import com.kebunby.app.ui.common.component.CustomToolbar
import com.kebunby.app.ui.common.component.PlantCard
import com.kebunby.app.ui.theme.Grey
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PlantListScreen(
    navController: NavController,
    plantListViewModel: PlantListViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel
) {
    val isTrending = plantListViewModel.isTrending
    val forBeginner = plantListViewModel.forBeginner
    val categoryId = plantListViewModel.categoryId
    val category = plantListViewModel.category

    val onEvent = plantListViewModel::onEvent
    val pagingPlants = plantListViewModel.pagingPlants.collectAsLazyPagingItems()
    val swipeRefreshing = plantListViewModel.swipeRefreshing.value
    val onSwipeRefreshingChanged = plantListViewModel::onSwipeRefreshingChanged

    val isReloaded = sharedViewModel.isReloaded.value
    val onReload = sharedViewModel::onReload

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomToolbar(
                title = when {
                    isTrending == true -> {
                        stringResource(id = R.string.trending)
                    }

                    forBeginner == true -> {
                        stringResource(id = R.string.for_beginner)
                    }

                    category != null -> {
                        category
                    }

                    else -> "Plants"
                },
                hasBackButton = true,
                onBackButtonClicked = { navController.navigateUp() }
            )
        }
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = swipeRefreshing),
            onRefresh = {
                onSwipeRefreshingChanged(true)

                if (categoryId!! > 0) {
                    onEvent(PlantListEvent.LoadPlantsByCategory)
                } else {
                    onEvent(PlantListEvent.LoadPlants)
                }
            },
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    contentColor = MaterialTheme.colors.primary
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(color = MaterialTheme.colors.background)
                    .fillMaxSize(),
                contentPadding = PaddingValues(20.dp)
            ) {
                items(pagingPlants) { plant ->
                    PlantCard(
                        plantItem = plant,
                        onClick = {
                            navController.navigate(
                                Screen.PlantDetailScreen.route + "?plantId=${plant?.id}"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Handle paging plants state
                pagingPlants.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(top = 170.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        loadState.source.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached -> {
                            onSwipeRefreshingChanged(false)
                            onReload(false)

                            if (itemCount < 1) {
                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(top = 150.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Image(
                                            modifier = Modifier.size(200.dp),
                                            painter = painterResource(id = R.drawable.img_empty_plant),
                                            contentDescription = "Empty plant illustration"
                                        )
                                        Text(
                                            text = stringResource(id = R.string.no_plants_yet),
                                            color = Grey,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.h3
                                        )
                                    }
                                }
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                        }

                        loadState.append is LoadState.Error -> {
                            onSwipeRefreshingChanged(false)

                            item {
                                coroutineScope.launch {
                                    (loadState.append as LoadState.Error).error.localizedMessage?.let { message ->
                                        scaffoldState.snackbarHostState.showSnackbar(message)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Observe is reloaded state
        if (isReloaded) onEvent(PlantListEvent.LoadPlants)
    }
}