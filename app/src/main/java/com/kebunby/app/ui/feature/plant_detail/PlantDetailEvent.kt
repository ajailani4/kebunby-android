package com.kebunby.app.ui.feature.plant_detail

sealed class PlantDetailEvent {
    object LoadPlantDetail : PlantDetailEvent()
    object AddFavoritePlant : PlantDetailEvent()
    object DeleteFavoritePlant : PlantDetailEvent()
    object AddPlantingPlant : PlantDetailEvent()
    object AddPlantedPlant : PlantDetailEvent()
    object DeletePlant : PlantDetailEvent()
}