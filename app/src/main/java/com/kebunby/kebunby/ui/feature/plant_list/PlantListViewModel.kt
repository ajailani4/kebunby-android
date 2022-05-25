package com.kebunby.kebunby.ui.feature.plant_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.domain.use_case.plant.GetPagingPlantsByCategoryUseCase
import com.kebunby.kebunby.domain.use_case.plant.GetPagingPlantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPagingPlantsUseCase: GetPagingPlantsUseCase,
    private val getPagingPlantsByCategoryUseCase: GetPagingPlantsByCategoryUseCase
) : ViewModel() {
    val isTrending = savedStateHandle.get<Boolean>("isTrending")
    val forBeginner = savedStateHandle.get<Boolean>("forBeginner")
    val searchQuery = savedStateHandle.get<String>("searchQuery")
    val categoryId = savedStateHandle.get<Int>("categoryId")
    val category = savedStateHandle.get<String>("category")

    val pagingPlants = MutableStateFlow<PagingData<PlantItem>>(PagingData.empty())

    init {
        if (categoryId!! > 0) {
            onEvent(PlantListEvent.LoadPlantsByCategory)
        } else {
            onEvent(PlantListEvent.LoadPlants)
        }
    }

    fun onEvent(event: PlantListEvent) {
        when (event) {
            PlantListEvent.LoadPlants -> {
                getPlants()
            }

            PlantListEvent.LoadPlantsByCategory -> {
                getPlantsByCategory()
            }
        }
    }

    private fun getPlants() {
        viewModelScope.launch {
            getPagingPlantsUseCase.invoke(
                isTrending = isTrending,
                forBeginner = forBeginner,
                searchQuery = searchQuery
            ).cachedIn(viewModelScope).collect {
                pagingPlants.value = it
            }
        }
    }

    private fun getPlantsByCategory() {
        viewModelScope.launch {
            getPagingPlantsByCategoryUseCase
                .invoke(categoryId!!)
                .cachedIn(viewModelScope)
                .collect {
                    pagingPlants.value = it
                }
        }
    }
}