package com.kebunby.kebunby.ui.feature.plant_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import com.kebunby.kebunby.R
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
import com.kebunby.kebunby.ui.Screen
import com.kebunby.kebunby.ui.common.component.CustomToolbar
import com.kebunby.kebunby.ui.common.component.PlantCard
import com.kebunby.kebunby.ui.theme.Grey
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PlantListScreen(
    navController: NavController,
    plantListViewModel: PlantListViewModel = hiltViewModel()
) {
    val isTrending = plantListViewModel.isTrending
    val forBeginner = plantListViewModel.forBeginner
    val category = plantListViewModel.category

    val pagingPlants = plantListViewModel.pagingPlants.collectAsLazyPagingItems()

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomToolbar(
                navController = navController,
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
                hasBackButton = true
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
                        loadState.append.endOfPaginationReached && itemCount < 1 -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 150.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    modifier = Modifier.size(230.dp),
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
}