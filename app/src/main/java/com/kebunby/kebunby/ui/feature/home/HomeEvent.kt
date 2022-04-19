package com.kebunby.kebunby.ui.feature.home

sealed class HomeEvent {
    object LoadUserProfile : HomeEvent()
    object LoadTrendingPlants : HomeEvent()
    object LoadForBeginnerPlants : HomeEvent()
    object LoadPlantCategories : HomeEvent()
    object AddFavoritePlant : HomeEvent()
    object DeleteFavoritePlant : HomeEvent()
}