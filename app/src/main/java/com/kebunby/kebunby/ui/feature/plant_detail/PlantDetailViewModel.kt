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
import com.kebunby.kebunby.ui.common.BaseUIState
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
    var plantDetailState by mutableStateOf<BaseUIState<Plant>>(BaseUIState.Idle)
    var addFavPlantState by mutableStateOf<BaseUIState<Nothing>>(BaseUIState.Idle)
    var deleteFavPlantState by mutableStateOf<BaseUIState<Nothing>>(BaseUIState.Idle)
    var addPlantingPlantState by mutableStateOf<BaseUIState<Nothing>>(BaseUIState.Idle)
    var addPlantedPlantState by mutableStateOf<BaseUIState<Nothing>>(BaseUIState.Idle)
    var isFavorited by mutableStateOf<Boolean?>(null)

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

    fun onFavoritePlant(_isFavorited: Boolean) {
        isFavorited = _isFavorited
    }

    private fun getPlantDetail() {
        plantDetailState = BaseUIState.Loading

        viewModelScope.launch {
            val resource = getPlantDetailUseCase.invoke(savedStateHandle.get<Int>("plantId")!!)

            resource.catch {
                plantDetailState = BaseUIState.Error(it.localizedMessage)
            }.collect {
                plantDetailState = when (it) {
                    is Resource.Success -> {
                        BaseUIState.Success(it.data)
                    }

                    is Resource.Error -> {
                       BaseUIState.Fail(it.message)
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
                addFavPlantState = BaseUIState.Error(it.localizedMessage)
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
                deleteFavPlantState = BaseUIState.Error(it.localizedMessage)
            }.collect {
                when (it) {
                    is Resource.Success -> {}
                    is Resource.Error -> {}
                }
            }
        }
    }

    private fun addPlantingPlant() {
        addPlantingPlantState = BaseUIState.Loading

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            val resource = addPlantActivityUseCase.invoke(
                username = userCredential.username!!,
                isPlanting = true,
                plantActRequest = PlantActRequest(savedStateHandle.get<Int>("plantId")!!)
            )

            resource.catch {
                addPlantingPlantState = BaseUIState.Error(it.localizedMessage)
            }.collect {
                when (it) {
                    is Resource.Success -> {
                        addPlantingPlantState = BaseUIState.Success(null)
                    }

                    is Resource.Error -> {}
                }
            }
        }
    }

    private fun addPlantedPlant() {
        addPlantedPlantState = BaseUIState.Loading

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            val resource = addPlantActivityUseCase.invoke(
                username = userCredential.username!!,
                isPlanted = true,
                plantActRequest = PlantActRequest(savedStateHandle.get<Int>("plantId")!!)
            )

            resource.catch {
                addPlantedPlantState = BaseUIState.Error(it.localizedMessage)
            }.collect {
                when (it) {
                    is Resource.Success -> {
                        addPlantedPlantState = BaseUIState.Success(null)
                    }

                    is Resource.Error -> {}
                }
            }
        }
    }
}