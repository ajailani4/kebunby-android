package com.kebunby.kebunby.ui.feature.plant_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.request.PlantActRequest
import com.kebunby.kebunby.domain.use_case.plant.AddPlantActivityUseCase
import com.kebunby.kebunby.domain.use_case.plant.DeletePlantActivityUseCase
import com.kebunby.kebunby.domain.use_case.plant.GetPlantDetailUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
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
    var plantDetailState by mutableStateOf<PlantDetailState>(PlantDetailState.Idle)
    var addFavPlantState by mutableStateOf<PlantDetailState>(PlantDetailState.Idle)
    var deleteFavPlantState by mutableStateOf<PlantDetailState>(PlantDetailState.Idle)
    var isFavorited by mutableStateOf<Boolean?>(null)

    init {
        onEvent(PlantDetailEvent.LoadPlantDetail)
    }

    fun onEvent(event: PlantDetailEvent) {
        when (event) {
            PlantDetailEvent.LoadPlantDetail -> getPlantDetail()

            PlantDetailEvent.AddFavoritePlant -> addFavoritePlant()

            PlantDetailEvent.DeleteFavoritePlant -> deleteFavoritePlant()
        }
    }

    fun onFavoritePlant(_isFavorited: Boolean) {
        isFavorited = _isFavorited
    }

    private fun getPlantDetail() {
        plantDetailState = PlantDetailState.LoadingPlantDetail

        viewModelScope.launch {
            val resource = getPlantDetailUseCase.invoke(savedStateHandle.get<Int>("plantId")!!)

            resource.catch {
                plantDetailState = PlantDetailState.ErrorPlantDetail(it.localizedMessage)
            }.collect {
                plantDetailState = when (it) {
                    is Resource.Success -> {
                        PlantDetailState.PlantDetail(it.data)
                    }

                    is Resource.Error -> {
                        PlantDetailState.FailPlantDetail(it.message)
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
                addFavPlantState = PlantDetailState.ErrorAddFavoritePlant(it.localizedMessage)
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
                deleteFavPlantState = PlantDetailState.ErrorDeleteFavoritePlant(it.localizedMessage)
            }.collect {
                when (it) {
                    is Resource.Success -> {}
                    is Resource.Error -> {}
                }
            }
        }
    }
}