package com.kebunby.kebunby.ui.feature.plant_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.Plant
import com.kebunby.kebunby.data.model.request.PlantActRequest
import com.kebunby.kebunby.domain.use_case.plant.AddPlantActivityUseCase
import com.kebunby.kebunby.domain.use_case.plant.DeletePlantActivityUseCase
import com.kebunby.kebunby.domain.use_case.plant.GetPlantDetailUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.kebunby.ui.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getUserCredentialUseCase: GetUserCredentialUseCase,
    private val getPlantDetailUseCase: GetPlantDetailUseCase,
    private val addPlantActivityUseCase: AddPlantActivityUseCase,
    private val deletePlantActivityUseCase: DeletePlantActivityUseCase
) : ViewModel() {
    var plantDetailState by mutableStateOf<UIState<Plant>>(UIState.Idle)
    var addFavPlantState by mutableStateOf<UIState<Nothing>>(UIState.Idle)
    var deleteFavPlantState by mutableStateOf<UIState<Nothing>>(UIState.Idle)
    var addPlantingPlantState by mutableStateOf<UIState<Nothing>>(UIState.Idle)
    var addPlantedPlantState by mutableStateOf<UIState<Nothing>>(UIState.Idle)

    var swipeRefreshing by mutableStateOf(false)
    var isFavorited by mutableStateOf<Boolean?>(null)
    var plantNowDialogVis by mutableStateOf(false)
    var finishPlantingDlgVis by mutableStateOf(false)

    init {
        onEvent(PlantDetailEvent.LoadPlantDetail)
    }

    fun onEvent(event: PlantDetailEvent) {
        when (event) {
            PlantDetailEvent.LoadPlantDetail -> getPlantDetail()

            PlantDetailEvent.AddFavoritePlant -> addFavoritePlant()

            PlantDetailEvent.DeleteFavoritePlant -> deleteFavoritePlant()

            PlantDetailEvent.AddPlantingPlant -> addPlantingPlant()

            PlantDetailEvent.AddPlantedPlant -> addPlantedPlant()
        }
    }

    fun onSwipeRefreshingChanged(isRefreshing: Boolean) {
        swipeRefreshing = isRefreshing
    }

    fun onFavoritePlant(_isFavorited: Boolean) {
        isFavorited = _isFavorited
    }

    fun onPlantNowDialogVisChanged(visibility: Boolean) {
        plantNowDialogVis = visibility
    }

    fun onFinishPlantingDlgVisChanged(visibility: Boolean) {
        finishPlantingDlgVis = visibility
    }

    private fun getPlantDetail() {
        plantDetailState = UIState.Loading

        viewModelScope.launch {
            val resource = getPlantDetailUseCase.invoke(savedStateHandle.get<Int>("plantId")!!)

            resource.catch {
                plantDetailState = UIState.Error(it.localizedMessage)
            }.collect {
                plantDetailState = when (it) {
                    is Resource.Success -> {
                        UIState.Success(it.data)
                    }

                    is Resource.Error -> {
                        UIState.Fail(it.message)
                    }
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
                plantActRequest = PlantActRequest(savedStateHandle.get<Int>("plantId")!!)
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

    private fun deleteFavoritePlant() {
        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            val resource = deletePlantActivityUseCase.invoke(
                username = userCredential.username!!,
                plantId = savedStateHandle.get<Int>("plantId")!!,
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

    private fun addPlantingPlant() {
        addPlantingPlantState = UIState.Loading

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            val resource = addPlantActivityUseCase.invoke(
                username = userCredential.username!!,
                isPlanting = true,
                plantActRequest = PlantActRequest(savedStateHandle.get<Int>("plantId")!!)
            )

            resource.catch {
                addPlantingPlantState = UIState.Error(it.localizedMessage)
            }.collect {
                when (it) {
                    is Resource.Success -> {
                        addPlantingPlantState = UIState.Success(null)
                    }

                    is Resource.Error -> {}
                }
            }
        }
    }

    private fun addPlantedPlant() {
        addPlantedPlantState = UIState.Loading

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            val resource = addPlantActivityUseCase.invoke(
                username = userCredential.username!!,
                isPlanted = true,
                plantActRequest = PlantActRequest(savedStateHandle.get<Int>("plantId")!!)
            )

            resource.catch {
                addPlantedPlantState = UIState.Error(it.localizedMessage)
            }.collect {
                when (it) {
                    is Resource.Success -> {
                        addPlantedPlantState = UIState.Success(null)
                    }

                    is Resource.Error -> {}
                }
            }
        }
    }
}