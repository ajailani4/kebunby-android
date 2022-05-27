package com.kebunby.kebunby.ui.feature.profile.planting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.Screen
import com.kebunby.kebunby.ui.common.component.PlantCard
import com.kebunby.kebunby.ui.theme.Grey

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PlantingScreen(
    navController: NavController,
    plantingViewModel: PlantingViewModel = hiltViewModel()
) {
    val pagingPlants = plantingViewModel.pagingPlants.collectAsLazyPagingItems()

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
                                .padding(top = 150.dp),
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
                                .padding(top = 50.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier.size(190.dp),
                                painter = painterResource(id = R.drawable.img_empty_plant_profile),
                                contentDescription = "Empty plant illustration"
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = stringResource(id = R.string.no_planting_plants_yet),
                                color = Grey,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.h3
                            )
                            Spacer(modifier = Modifier.height(25.dp))
                            Button(
                                shape = MaterialTheme.shapes.small,
                                onClick = {
                                    navController.navigate(Screen.ExploreScreen.route) {
                                        popUpTo(Screen.ProfileScreen.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            ) {
                                Text(
                                    text = stringResource(id = R.string.search_plant),
                                    color = MaterialTheme.colors.onPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.subtitle1
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
            }
        }
    }
}