package com.kebunby.app.ui.feature.home

sealed class HomeEvent {
    object Idle : HomeEvent()
    object LoadUserProfile : HomeEvent()
    object LoadTrendingPlants : HomeEvent()
    object LoadForBeginnerPlants : HomeEvent()
    object LoadPlantCategories : HomeEvent()
    object AddFavoritePlant : HomeEvent()
    object DeleteFavoritePlant : HomeEvent()
}