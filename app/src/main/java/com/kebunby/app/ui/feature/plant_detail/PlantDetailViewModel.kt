package com.kebunby.app.ui.feature.plant_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.app.data.Resource
import com.kebunby.app.data.model.Plant
import com.kebunby.app.data.model.request.PlantActRequest
import com.kebunby.app.domain.use_case.plant.AddPlantActivityUseCase
import com.kebunby.app.domain.use_case.plant.DeletePlantActivityUseCase
import com.kebunby.app.domain.use_case.plant.DeletePlantUseCase
import com.kebunby.app.domain.use_case.plant.GetPlantDetailUseCase
import com.kebunby.app.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.app.ui.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getUserCredentialUseCase: GetUserCredentialUseCase,
    private val getPlantDetailUseCase: GetPlantDetailUseCase,
    private val addPlantActivityUseCase: AddPlantActivityUseCase,
    private val deletePlantActivityUseCase: DeletePlantActivityUseCase,
    private val deletePlantUseCase: DeletePlantUseCase
) : ViewModel() {
    val plantId = savedStateHandle.get<Int>("plantId")

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

    private var _deletePlantState = mutableStateOf<UIState<Any>>(UIState.Idle)
    val deletePlantState: State<UIState<Any>> = _deletePlantState

    private var _swipeRefreshing = mutableStateOf(false)
    val swipeRefreshing: State<Boolean> = _swipeRefreshing

    private var _fullSizeImgVis = mutableStateOf(false)
    val fullSizeImgVis: State<Boolean> = _fullSizeImgVis

    private var _isFavorited = mutableStateOf<Boolean?>(null)
    val isFavorited: State<Boolean?> = _isFavorited

    private var _moreMenuBtnVis = mutableStateOf(false)
    val moreMenuBtnVis: State<Boolean> = _moreMenuBtnVis

    private var _plantNowDialogVis = mutableStateOf(false)
    val plantNowDialogVis: State<Boolean> = _plantNowDialogVis

    private var _finishPlantingDlgVis = mutableStateOf(false)
    val finishPlantingDlgVis: State<Boolean> = _finishPlantingDlgVis

    private var _deletePlantDlgVis = mutableStateOf(false)
    val deletePlantDlgVis: State<Boolean> = _deletePlantDlgVis

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

            PlantDetailEvent.DeletePlant -> deletePlant()
        }
    }

    fun onSwipeRefreshingChanged(isRefreshing: Boolean) {
        _swipeRefreshing.value = isRefreshing
    }

    fun onFullSizeImgVisChanged(visibility: Boolean) {
        _fullSizeImgVis.value = visibility
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

    fun onDeletePlantDlgVisChanged(visibility: Boolean) {
        _deletePlantDlgVis.value = visibility
    }

    private fun getPlantDetail() {
        _plantDetailState.value = UIState.Loading

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase().first()
            val resource = getPlantDetailUseCase(savedStateHandle.get<Int>("plantId")!!)

            resource.catch {
                _plantDetailState.value = UIState.Error(it.localizedMessage)
            }.collect {
                _plantDetailState.value = when (it) {
                    is Resource.Success -> {
                        if (userCredential.username == it.data?.author) {
                            _moreMenuBtnVis.value = true
                        }

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
            val userCredential = getUserCredentialUseCase().first()

            val resource = addPlantActivityUseCase(
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
            val userCredential = getUserCredentialUseCase().first()

            val resource = deletePlantActivityUseCase(
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
            val userCredential = getUserCredentialUseCase().first()

            val resource = addPlantActivityUseCase(
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
            val userCredential = getUserCredentialUseCase().first()

            val resource = addPlantActivityUseCase(
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

    private fun deletePlant() {
        _deletePlantState.value = UIState.Loading

        viewModelScope.launch {
            val resource = deletePlantUseCase(plantId!!)

            resource.catch {
                _deletePlantState.value = UIState.Error(it.localizedMessage)
            }.collect {
                _deletePlantState.value = when (it) {
                    is Resource.Success -> UIState.Success(it.data)

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }
}