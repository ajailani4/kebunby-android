package com.kebunby.kebunby.ui.feature.profile.planted

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.kebunby.kebunby.ui.Screen
import com.kebunby.kebunby.ui.common.component.PlantCard

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PlantedScreen(
    navController: NavController,
    plantedViewModel: PlantedViewModel = hiltViewModel()
) {
    val pagingPlants = plantedViewModel.pagingPlants.collectAsLazyPagingItems()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    LazyColumn(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .height(screenHeight - 160.dp),
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
                                .padding(150.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
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