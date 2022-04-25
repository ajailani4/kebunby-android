package com.kebunby.kebunby.ui.feature.plant_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.ui.common.component.CustomToolbar
import com.kebunby.kebunby.ui.feature.plant_list.component.PlantCard

@Composable
fun PlantListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CustomToolbar(
                navController = navController,
                title = "Tanaman",
                hasBackButton = true
            )
        }
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            PlantCard(
                plantItem = PlantItem(
                    id = 1,
                    name = "Tanaman",
                    image = "test",
                    category = "Tanaman Hias",
                    growthEst = "2-3 Tahun",
                    wateringFreq = "3x Sehari",
                    popularity = 10,
                    isFavorited = true
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            PlantCard(
                plantItem = PlantItem(
                    id = 1,
                    name = "Tanaman",
                    image = "test",
                    category = "Tanaman Hias",
                    growthEst = "2-3 Tahun",
                    wateringFreq = "3x Sehari",
                    popularity = 10,
                    isFavorited = true
                )
            )
        }
    }
}