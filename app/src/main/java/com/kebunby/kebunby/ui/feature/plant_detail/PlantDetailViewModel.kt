package com.kebunby.kebunby.ui.feature.plant_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
    private var _plantDetailState = mutableStateOf<UIState<Plant>>(UIState.Idle)
    val plantDetailState: State<UIState<Plant>> = _plantDetailState

    private var _addFavPlantState = mutableStateOf<UIState<Nothing>>(UIState.Idle)
    val addFavPlantState: State<UIState<Nothing>> = _addFavPlantState

    private var _deleteFavPlantState = mutableStateOf<UIState<Nothing>>(UIState.Idle)
    val deleteFavPlantState: State<UIState<Nothing>> = _deleteFavPlantState

    private var _addPlantingPlantState = mutableStateOf<UIState<Nothing>>(UIState.Idle)
    val addPlantingPlantState: State<UIState<Nothing>> = _addPlantingPlantState

    private var _addPlantedPlantState = mutableStateOf<UIState<Nothing>>(UIState.Idle)
    val addPlantedPlantState: State<UIState<Nothing>> = _addPlantedPlantState

    private var _swipeRefreshing = mutableStateOf(false)
    val swipeRefreshing: State<Boolean> = _swipeRefreshing

    private var _isFavorited = mutableStateOf<Boolean?>(null)
    val isFavorited: State<Boolean?> = _isFavorited

    private var _plantNowDialogVis = mutableStateOf(false)
    val plantNowDialogVis: State<Boolean> = _plantNowDialogVis

    private var _finishPlantingDlgVis = mutableStateOf(false)
    val finishPlantingDlgVis: State<Boolean> = _finishPlantingDlgVis

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
        _swipeRefreshing.value = isRefreshing
    }

    fun onFavoritePlant(isFavorited: Boolean) {
        _isFavorited.value = isFavorited
    }

    fun onPlantNowDialogVisChanged(visibility: Boolean) {
        _plantNowDialogVis.value = visibility
    }

    fun onFinishPlantingDlgVisChanged(visibility: Boolean) {
        _finishPlantingDlgVis.value = visibility
    }

    private fun getPlantDetail() {
        _plantDetailState.value = UIState.Loading

        viewModelScope.launch {
            val resource = getPlantDetailUseCase.invoke(savedStateHandle.get<Int>("plantId")!!)

            resource.catch {
                _plantDetailState.value = UIState.Error(it.localizedMessage)
            }.collect {
                _plantDetailState.value = when (it) {
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
                _addFavPlantState.value = UIState.Error(it.localizedMessage)
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
                _deleteFavPlantState.value = UIState.Error(it.localizedMessage)
            }.collect {
                when (it) {
                    is Resource.Success -> {}
                    is Resource.Error -> {}
                }
            }
        }
    }

    private fun addPlantingPlant() {
        _addPlantingPlantState.value = UIState.Loading

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            val resource = addPlantActivityUseCase.invoke(
                username = userCredential.username!!,
                isPlanting = true,
                plantActRequest = PlantActRequest(savedStateHandle.get<Int>("plantId")!!)
            )

            resource.catch {
                _addPlantingPlantState.value = UIState.Error(it.localizedMessage)
            }.collect {
                when (it) {
                    is Resource.Success -> {
                        _addPlantingPlantState.value = UIState.Success(null)
                    }

                    is Resource.Error -> {}
                }
            }
        }
    }

    private fun addPlantedPlant() {
        _addPlantedPlantState.value = UIState.Loading

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            val resource = addPlantActivityUseCase.invoke(
                username = userCredential.username!!,
                isPlanted = true,
                plantActRequest = PlantActRequest(savedStateHandle.get<Int>("plantId")!!)
            )

            resource.catch {
                _addPlantedPlantState.value = UIState.Error(it.localizedMessage)
            }.collect {
                when (it) {
                    is Resource.Success -> {
                        _addPlantedPlantState.value = UIState.Success(null)
                    }

                    is Resource.Error -> {}
                }
            }
        }
    }
}