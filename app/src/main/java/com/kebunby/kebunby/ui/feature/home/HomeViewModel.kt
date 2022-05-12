package com.kebunby.kebunby.ui.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.kebunby.kebunby.ui.common.BaseUIState
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
    var userProfileState by mutableStateOf<BaseUIState<User>>(BaseUIState.Idle)
    var trendingPlantsState by mutableStateOf<BaseUIState<List<PlantItem>>>(BaseUIState.Idle)
    var forBeginnerPlantsState by mutableStateOf<BaseUIState<List<PlantItem>>>(BaseUIState.Idle)
    var plantCategoriesState by mutableStateOf<BaseUIState<List<PlantCategory>>>(BaseUIState.Idle)
    var addFavPlantState by mutableStateOf<BaseUIState<Nothing>>(BaseUIState.Idle)
    var deleteFavPlantState by mutableStateOf<BaseUIState<Nothing>>(BaseUIState.Idle)
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
        userProfileState = BaseUIState.Idle
        trendingPlantsState = BaseUIState.Idle
        forBeginnerPlantsState = BaseUIState.Idle
        plantCategoriesState = BaseUIState.Idle
        addFavPlantState = BaseUIState.Idle
        deleteFavPlantState = BaseUIState.Idle
    }

    private fun getUserProfile() {
        userProfileState = BaseUIState.Loading

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            val resource = getUserProfileUseCase.invoke(userCredential.username!!)

            resource.catch {
                userProfileState = BaseUIState.Error(it.localizedMessage)
            }.collect {
                userProfileState = when (it) {
                    is Resource.Success -> BaseUIState.Success(it.data)

                    is Resource.Error -> BaseUIState.Fail(it.message)
                }
            }
        }
    }

    private fun getTrendingPlants() {
        trendingPlantsState = BaseUIState.Loading

        viewModelScope.launch {
            val resource = getPlantsUseCase.invoke(
                page = 1,
                size = 5,
                isTrending = true
            )

            resource.catch {
                trendingPlantsState = BaseUIState.Error(it.localizedMessage)
            }.collect {
                trendingPlantsState = when (it) {
                    is Resource.Success -> BaseUIState.Success(it.data)

                    is Resource.Error -> BaseUIState.Fail(it.message)
                }
            }
        }
    }

    private fun getForBeginnerPlants() {
        forBeginnerPlantsState = BaseUIState.Loading

        viewModelScope.launch {
            val resource = getPlantsUseCase.invoke(
                page = 1,
                size = 5,
                forBeginner = true
            )

            resource.catch {
                forBeginnerPlantsState = BaseUIState.Error(it.localizedMessage)
            }.collect {
                forBeginnerPlantsState = when (it) {
                    is Resource.Success -> BaseUIState.Success(it.data)

                    is Resource.Error -> BaseUIState.Fail(it.message)
                }
            }
        }
    }

    private fun getPlantCategories() {
        plantCategoriesState = BaseUIState.Loading

        viewModelScope.launch {
            val resource = getPlantCategoriesUseCase.invoke()

            resource.catch {
                plantCategoriesState = BaseUIState.Error(it.localizedMessage)
            }.collect {
                plantCategoriesState = when (it) {
                    is Resource.Success -> BaseUIState.Success(it.data)

                    is Resource.Error -> BaseUIState.Fail(it.message)
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
                addFavPlantState = BaseUIState.Error(it.localizedMessage)
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
                deleteFavPlantState = BaseUIState.Error(it.localizedMessage)
            }.collect {
                when (it) {
                    is Resource.Success -> {}
                    is Resource.Error -> {}
                }
            }
        }
    }
}