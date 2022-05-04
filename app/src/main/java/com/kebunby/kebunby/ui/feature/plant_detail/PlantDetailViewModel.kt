package com.kebunby.kebunby.ui.feature.plant_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.domain.use_case.plant.GetPlantDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPlantDetailUseCase: GetPlantDetailUseCase
) : ViewModel() {
    var plantDetailState by mutableStateOf<PlantDetailState>(PlantDetailState.Idle)

    init {
        onEvent(PlantDetailEvent.LoadPlantDetail)
    }

    fun onEvent(event: PlantDetailEvent) {
        when (event) {
            PlantDetailEvent.LoadPlantDetail -> getPlantDetail()
        }
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
}