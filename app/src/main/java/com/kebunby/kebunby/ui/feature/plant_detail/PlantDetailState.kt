package com.kebunby.kebunby.ui.feature.plant_detail

import com.kebunby.kebunby.data.model.Plant

sealed class PlantDetailState {
    object Idle : PlantDetailState()

    object LoadingPlantDetail : PlantDetailState()
    object LoadingAddPlantingPlant : PlantDetailState()
    object LoadingAddPlantedPlant : PlantDetailState()

    data class PlantDetail(val plant: Plant?) : PlantDetailState()
    object SuccessAddPlantingPlant : PlantDetailState()
    object SuccessAddPlantedPlant : PlantDetailState()

    data class FailPlantDetail(val message: String?) : PlantDetailState()

    data class ErrorPlantDetail(val message: String?) : PlantDetailState()
    data class ErrorAddFavoritePlant(val message: String?) : PlantDetailState()
    data class ErrorDeleteFavoritePlant(val message: String?) : PlantDetailState()
    data class ErrorAddPlantingPlant(val message: String?) : PlantDetailState()
    data class ErrorAddPlantedPlant(val message: String?) : PlantDetailState()
}
