package com.kebunby.kebunby.ui.feature.home

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.PlantCategory
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.data.model.User
import com.kebunby.kebunby.data.model.request.PlantActRequest
import com.kebunby.kebunby.domain.use_case.plant.AddPlantActivityUseCase
import com.kebunby.kebunby.domain.use_case.plant.DeletePlantActivityUseCase
import com.kebunby.kebunby.domain.use_case.plant.GetPlantCategoriesUseCase
import com.kebunby.kebunby.domain.use_case.plant.GetPlantsUseCase
import com.kebunby.kebunby.domain.use_case.user.GetUserProfileUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.kebunby.ui.common.UIState
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
    private val addPlantActivityUseCase: AddPlantActivityUseCase,
    private val deletePlantActivityUseCase: DeletePlantActivityUseCase
) : ViewModel() {
    var userProfileState by mutableStateOf<UIState<User>>(UIState.Idle)
    var trendingPlantsState by mutableStateOf<UIState<List<PlantItem>>>(UIState.Idle)
    var forBeginnerPlantsState by mutableStateOf<UIState<List<PlantItem>>>(UIState.Idle)
    var plantCategoriesState by mutableStateOf<UIState<List<PlantCategory>>>(UIState.Idle)
    var addFavPlantState by mutableStateOf<UIState<Nothing>>(UIState.Idle)
    var deleteFavPlantState by mutableStateOf<UIState<Nothing>>(UIState.Idle)

    var swipeRefreshing by mutableStateOf(false)
    private var selectedPlant by mutableStateOf(0)
    var trendingPlants = mutableStateListOf<PlantItem>()
    var forBeginnerPlants = mutableStateListOf<PlantItem>()

    init {
        onEvent(HomeEvent.LoadUserProfile)
        onEvent(HomeEvent.LoadTrendingPlants)
        onEvent(HomeEvent.LoadForBeginnerPlants)
        onEvent(HomeEvent.LoadPlantCategories)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.Idle -> idle()

            HomeEvent.LoadUserProfile -> getUserProfile()

            HomeEvent.LoadTrendingPlants -> getTrendingPlants()

            HomeEvent.LoadForBeginnerPlants -> getForBeginnerPlants()

            HomeEvent.LoadPlantCategories -> getPlantCategories()

            HomeEvent.AddFavoritePlant -> addFavoritePlant()

            HomeEvent.DeleteFavoritePlant -> deleteFavPlant()
        }
    }

    fun onSwipeRefreshingChanged(isRefreshing: Boolean) {
        swipeRefreshing = isRefreshing
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

    fun setForBeginnerPlants(plants: List<PlantItem>) {
        forBeginnerPlants.addAll(plants)
    }

    fun updateForBeginnerPlants(index: Int, plant: PlantItem) {
        forBeginnerPlants[index] = plant
    }

    private fun idle() {
        userProfileState = UIState.Idle
        trendingPlantsState = UIState.Idle
        forBeginnerPlantsState = UIState.Idle
        plantCategoriesState = UIState.Idle
        addFavPlantState = UIState.Idle
        deleteFavPlantState = UIState.Idle
    }

    private fun getUserProfile() {
        userProfileState = UIState.Loading

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            val resource = getUserProfileUseCase.invoke(userCredential.username!!)

            resource.catch {
                userProfileState = UIState.Error(it.localizedMessage)
            }.collect {
                userProfileState = when (it) {
                    is Resource.Success -> UIState.Success(it.data)

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }

    private fun getTrendingPlants() {
        trendingPlantsState = UIState.Loading

        viewModelScope.launch {
            val resource = getPlantsUseCase.invoke(
                page = 1,
                size = 5,
                isTrending = true
            )

            resource.catch {
                trendingPlantsState = UIState.Error(it.localizedMessage)
            }.collect {
                trendingPlantsState = when (it) {
                    is Resource.Success -> UIState.Success(it.data)

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }

    private fun getForBeginnerPlants() {
        forBeginnerPlantsState = UIState.Loading

        viewModelScope.launch {
            val resource = getPlantsUseCase.invoke(
                page = 1,
                size = 5,
                forBeginner = true
            )

            resource.catch {
                forBeginnerPlantsState = UIState.Error(it.localizedMessage)
            }.collect {
                forBeginnerPlantsState = when (it) {
                    is Resource.Success -> UIState.Success(it.data)

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }

    private fun getPlantCategories() {
        plantCategoriesState = UIState.Loading

        viewModelScope.launch {
            val resource = getPlantCategoriesUseCase.invoke()

            resource.catch {
                plantCategoriesState = UIState.Error(it.localizedMessage)
            }.collect {
                plantCategoriesState = when (it) {
                    is Resource.Success -> UIState.Success(it.data)

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }

    private fun addFavoritePlant() {
        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            val resource = addPlantActivityUseCase.invoke(
                username = userCredential.username!!,
                isFavorited = true,
                plantActRequest = PlantActRequest(selectedPlant)
            )

            resource.catch {
                addFavPlantState = UIState.Error(it.localizedMessage)
            }.collect {
                when (it) {
                    is Resource.Success -> {}
                    is Resource.Error -> {}
                }
            }
        }
    }

    private fun deleteFavPlant() {
        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            val resource = deletePlantActivityUseCase.invoke(
                username = userCredential.username!!,
                plantId = selectedPlant,
                isFavorited = true
            )

            resource.catch {
                deleteFavPlantState = UIState.Error(it.localizedMessage)
            }.collect {
                when (it) {
                    is Resource.Success -> {}
                    is Resource.Error -> {}
                }
            }
        }
    }
}