package com.kebunby.kebunby.ui.feature.home

import com.kebunby.kebunby.data.model.PlantCategory
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.data.model.User

sealed class HomeState {
    object Idle : HomeState()

    object LoadingUserProfile : HomeState()
    object LoadingTrendingPlants : HomeState()
    object LoadingForBeginnerPlants : HomeState()
    object LoadingPlantCategories : HomeState()

    data class UserProfile(val user: User?) : HomeState()
    data class TrendingPlants(val plants: List<PlantItem>?) : HomeState()
    data class ForBeginnerPlants(val plants: List<PlantItem>?) : HomeState()
    data class PlantCategories(val plantCategories: List<PlantCategory>?) : HomeState()
    object SuccessFav : HomeState()

    data class FailUserProfile(val message: String?) : HomeState()
    data class FailTrendingPlants(val message: String?) : HomeState()
    data class FailForBeginnerPlants(val message: String?) : HomeState()
    data class FailPlantCategories(val message: String?) : HomeState()
    data class FailAddFavoritePlant(val message: String?) : HomeState()

    data class ErrorUserProfile(val message: String?) : HomeState()
    data class ErrorTrendingPlants(val message: String?) : HomeState()
    data class ErrorForBeginnerPlants(val message: String?) : HomeState()
    data class ErrorPlantCategories(val message: String?) : HomeState()
    data class ErrorAddFavoritePlant(val message: String?) : HomeState()
}
