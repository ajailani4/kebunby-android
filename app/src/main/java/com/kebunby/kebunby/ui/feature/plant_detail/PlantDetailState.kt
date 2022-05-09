package com.kebunby.kebunby.ui.feature.plant_detail

import com.kebunby.kebunby.data.model.Plant

sealed class PlantDetailState {
    object Idle : PlantDetailState()

    object LoadingPlantDetail : PlantDetailState()

    data class PlantDetail(val plant: Plant?) : PlantDetailState()

    data class FailPlantDetail(val message: String?) : PlantDetailState()

    data class ErrorPlantDetail(val message: String?) : PlantDetailState()
    data class ErrorAddFavoritePlant(val message: String?) : PlantDetailState()
    data class ErrorDeleteFavoritePlant(val message: String?) : PlantDetailState()
}
