package com.kebunby.app.ui.feature.home

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.app.data.Resource
import com.kebunby.app.data.model.PlantCategory
import com.kebunby.app.data.model.PlantItem
import com.kebunby.app.data.model.User
import com.kebunby.app.data.model.request.PlantActRequest
import com.kebunby.app.domain.use_case.plant.AddPlantActivityUseCase
import com.kebunby.app.domain.use_case.plant.DeletePlantActivityUseCase
import com.kebunby.app.domain.use_case.plant.GetPlantCategoriesUseCase
import com.kebunby.app.domain.use_case.plant.GetPlantsUseCase
import com.kebunby.app.domain.use_case.user.GetUserProfileUseCase
import com.kebunby.app.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.app.ui.common.UIState
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
    private var _userProfileState = mutableStateOf<UIState<User>>(UIState.Idle)
    val userProfileState: State<UIState<User>> = _userProfileState

    private var _trendingPlantsState = mutableStateOf<UIState<List<PlantItem>>>(UIState.Idle)
    val trendingPlantsState: State<UIState<List<PlantItem>>> = _trendingPlantsState

    private var _forBeginnerPlantsState = mutableStateOf<UIState<List<PlantItem>>>(UIState.Idle)
    val forBeginnerPlantsState: State<UIState<List<PlantItem>>> = _forBeginnerPlantsState

    private var _plantCategoriesState = mutableStateOf<UIState<List<PlantCategory>>>(UIState.Idle)
    val plantCategoriesState: State<UIState<List<PlantCategory>>> = _plantCategoriesState

    private var _addFavPlantState = mutableStateOf<UIState<Nothing>>(UIState.Idle)
    val addFavPlantState: State<UIState<Nothing>> = _addFavPlantState

    private var _deleteFavPlantState = mutableStateOf<UIState<Nothing>>(UIState.Idle)
    val deleteFavPlantState: State<UIState<Nothing>> = _deleteFavPlantState

    private var _swipeRefreshing = mutableStateOf(false)
    val swipeRefreshing: State<Boolean> = _swipeRefreshing

    private var selectedPlant by mutableStateOf(0)

    private var _trendingPlants = mutableStateListOf<PlantItem>()
    val trendingPlants: SnapshotStateList<PlantItem> = _trendingPlants

    private var _forBeginnerPlants = mutableStateListOf<PlantItem>()
    val forBeginnerPlants: SnapshotStateList<PlantItem> = _forBeginnerPlants

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
        _swipeRefreshing.value = isRefreshing
    }

    fun onSelectedPlantChanged(plantId: Int) {
        selectedPlant = plantId
    }

    fun setTrendingPlants(plants: List<PlantItem>) {
        _trendingPlants.addAll(plants)
    }

    fun updateTrendingPlants(index: Int, plant: PlantItem) {
        _trendingPlants[index] = plant
    }

    fun clearTrendingPlants() {
        _trendingPlants.clear()
    }


    fun setForBeginnerPlants(plants: List<PlantItem>) {
        _forBeginnerPlants.addAll(plants)
    }

    fun updateForBeginnerPlants(index: Int, plant: PlantItem) {
        _forBeginnerPlants[index] = plant
    }

    fun clearForBeginnerPlants() {
        _forBeginnerPlants.clear()
    }

    private fun idle() {
        _userProfileState.value = UIState.Idle
        _trendingPlantsState.value = UIState.Idle
        _forBeginnerPlantsState.value = UIState.Idle
        _plantCategoriesState.value = UIState.Idle
        _addFavPlantState.value = UIState.Idle
        _deleteFavPlantState.value = UIState.Idle
    }

    private fun getUserProfile() {
        _userProfileState.value = UIState.Loading

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase().first()

            val resource = getUserProfileUseCase(userCredential.username!!)

            resource.catch {
                _userProfileState.value = UIState.Error(it.localizedMessage)
            }.collect {
                _userProfileState.value = when (it) {
                    is Resource.Success -> UIState.Success(it.data)

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }

    private fun getTrendingPlants() {
        _trendingPlantsState.value = UIState.Loading

        viewModelScope.launch {
            val resource = getPlantsUseCase(
                page = 1,
                size = 5,
                isTrending = true
            )

            resource.catch {
                _trendingPlantsState.value = UIState.Error(it.localizedMessage)
            }.collect {
                _trendingPlantsState.value = when (it) {
                    is Resource.Success -> UIState.Success(it.data)

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }

    private fun getForBeginnerPlants() {
        _forBeginnerPlantsState.value = UIState.Loading

        viewModelScope.launch {
            val resource = getPlantsUseCase(
                page = 1,
                size = 5,
                forBeginner = true
            )

            resource.catch {
                _forBeginnerPlantsState.value = UIState.Error(it.localizedMessage)
            }.collect {
                _forBeginnerPlantsState.value = when (it) {
                    is Resource.Success -> UIState.Success(it.data)

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }

    private fun getPlantCategories() {
        _plantCategoriesState.value = UIState.Loading

        viewModelScope.launch {
            val resource = getPlantCategoriesUseCase()

            resource.catch {
                _plantCategoriesState.value = UIState.Error(it.localizedMessage)
            }.collect {
                _plantCategoriesState.value = when (it) {
                    is Resource.Success -> UIState.Success(it.data)

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }

    private fun addFavoritePlant() {
        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase().first()

            val resource = addPlantActivityUseCase(
                username = userCredential.username!!,
                isFavorited = true,
                plantActRequest = PlantActRequest(selectedPlant)
            )

            resource.catch {
                _addFavPlantState.value = UIState.Error(it.localizedMessage)
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
            val userCredential = getUserCredentialUseCase().first()

            val resource = deletePlantActivityUseCase(
                username = userCredential.username!!,
                plantId = selectedPlant,
                isFavorited = true
            )

            resource.catch {
                _deleteFavPlantState.value = UIState.Error(it.localizedMessage)
            }.collect {
                when (it) {
                    is Resource.Success -> {}
                    is Resource.Error -> {}
                }
            }
        }
    }
}