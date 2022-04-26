package com.kebunby.kebunby.ui.feature.plant_list

import com.kebunby.kebunby.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.ui.common.component.CustomToolbar
import com.kebunby.kebunby.ui.feature.plant_list.component.PlantCard

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PlantListScreen(
    navController: NavController,
    plantListViewModel: PlantListViewModel = hiltViewModel()
) {
    val plantList = plantListViewModel.getPlantsByPaging().collectAsLazyPagingItems()
    val isTrending = plantListViewModel.isTrending
    val forBeginner = plantListViewModel.forBeginner
    val searchQuery = plantListViewModel.searchQuery

    Scaffold(
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

                    else -> searchQuery ?: "Plants"
                },
                hasBackButton = true
            )
        }
    ) {
        LazyColumn(contentPadding = PaddingValues(20.dp)) {
            if (plantList.loadState.refresh == LoadState.Loading) {
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

            items(plantList) { plant ->
                PlantCard(plantItem = plant)
                Spacer(modifier = Modifier.height(20.dp))
            }

            if (plantList.loadState.append == LoadState.Loading) {
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