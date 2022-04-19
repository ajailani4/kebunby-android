package com.kebunby.kebunby.ui.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.data.model.request.UserActPlantRequest
import com.kebunby.kebunby.domain.use_case.plant.AddUserPlantActUseCase
import com.kebunby.kebunby.domain.use_case.plant.GetPlantCategoriesUseCase
import com.kebunby.kebunby.domain.use_case.plant.GetPlantsUseCase
import com.kebunby.kebunby.domain.use_case.user.GetUserProfileUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserCredentialUseCase: GetUserCredentialUseCase,
    private val getPlantsUseCase: GetPlantsUseCase,
    private val getPlantCategoriesUseCase: GetPlantCategoriesUseCase,
    private val addUserPlantActUseCase: AddUserPlantActUseCase
) : ViewModel() {
    var userProfileState by mutableStateOf<HomeState>(HomeState.Idle)
    var trendingPlantsState by mutableStateOf<HomeState>(HomeState.Idle)
    var forBeginnerPlantsState by mutableStateOf<HomeState>(HomeState.Idle)
    var plantCategoriesState by mutableStateOf<HomeState>(HomeState.Idle)
    var addUserFavPlantState by mutableStateOf<HomeState>(HomeState.Idle)
    private var selectedPlant by mutableStateOf(0)
    var trendingPlants = mutableStateListOf<PlantItem>()

    init {
        onEvent(HomeEvent.LoadUserProfile)
        onEvent(HomeEvent.LoadTrendingPlants)
        onEvent(HomeEvent.LoadForBeginnerPlants)
        onEvent(HomeEvent.LoadPlantCategories)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadUserProfile -> getUserProfile()

            HomeEvent.LoadTrendingPlants -> getTrendingPlants()

            HomeEvent.LoadForBeginnerPlants -> getForBeginnerPlants()

            HomeEvent.LoadPlantCategories -> getPlantCategories()

            HomeEvent.AddFavoritePlant -> addUserFavPlant()
        }
    }

    fun onSelectedPlantChanged(plantId: Int) {
        selectedPlant = plantId
    }

    fun setTrendingPlants(plants: List<PlantItem>) {
        trendingPlants.addAll(plants)
    }

    fun updateTrendingPlants(index: Int, plant: PlantItem) {
        trendingPlants[index] = plant
    }

    private fun getUserProfile() {
        userProfileState = HomeState.LoadingUserProfile

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            val resource = getUserProfileUseCase.invoke(userCredential.username!!)

            resource.catch {
                userProfileState = HomeState.ErrorUserProfile(it.localizedMessage)
            }.collect {
                userProfileState = when (it) {
                    is Resource.Success -> HomeState.UserProfile(it.data)

                    is Resource.Error -> HomeState.FailUserProfile(it.message)
                }
            }
        }
    }

    private fun getTrendingPlants() {
        trendingPlantsState = HomeState.LoadingTrendingPlants

        viewModelScope.launch {
            val resource = getPlantsUseCase.invoke(
                page = 1,
                size = 5,
                isTrending = true
            )

            resource.catch {
                trendingPlantsState = HomeState.ErrorTrendingPlants(it.localizedMessage)
            }.collect {
                trendingPlantsState = when (it) {
                    is Resource.Success -> HomeState.TrendingPlants(it.data)

                    is Resource.Error -> HomeState.FailTrendingPlants(it.message)
                }
            }
        }
    }

    private fun getForBeginnerPlants() {
        forBeginnerPlantsState = HomeState.LoadingForBeginnerPlants

        viewModelScope.launch {
            val resource = getPlantsUseCase.invoke(
                page = 1,
                size = 5,
                forBeginner = true
            )

            resource.catch {
                forBeginnerPlantsState = HomeState.ErrorForBeginnerPlants(it.localizedMessage)
            }.collect {
                forBeginnerPlantsState = when (it) {
                    is Resource.Success -> HomeState.ForBeginnerPlants(it.data)

                    is Resource.Error -> HomeState.FailForBeginnerPlants(it.message)
                }
            }
        }
    }

    private fun getPlantCategories() {
        plantCategoriesState = HomeState.LoadingPlantCategories

        viewModelScope.launch {
            val resource = getPlantCategoriesUseCase.invoke()

            resource.catch {
                plantCategoriesState = HomeState.ErrorPlantCategories(it.localizedMessage)
            }.collect {
                plantCategoriesState = when (it) {
                    is Resource.Success -> HomeState.PlantCategories(it.data)

                    is Resource.Error -> HomeState.FailPlantCategories(it.message)
                }
            }
        }
    }

    private fun addUserFavPlant() {
        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            val resource = addUserPlantActUseCase.invoke(
                username = userCredential.username!!,
                isFavorited = true,
                userActPlantRequest = UserActPlantRequest(selectedPlant)
            )

            resource.catch {
                addUserFavPlantState = HomeState.ErrorAddFavoritePlant(it.localizedMessage)
            }.collect {
                when (it) {
                    is Resource.Success -> {}

                    is Resource.Error -> {
                        addUserFavPlantState = HomeState.ErrorAddFavoritePlant(it.message)
                    }
                }
            }
        }
    }
}