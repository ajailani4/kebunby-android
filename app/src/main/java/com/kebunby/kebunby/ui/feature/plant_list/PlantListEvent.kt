package com.kebunby.kebunby.ui.feature.plant_list

sealed class PlantListEvent {
    object LoadPlants : PlantListEvent()
    object LoadPlantsByCategory : PlantListEvent()
}
