package com.kebunby.app.ui.feature.plant_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kebunby.app.data.model.PlantItem
import com.kebunby.app.domain.use_case.plant.GetPagingPlantsByCategoryUseCase
import com.kebunby.app.domain.use_case.plant.GetPagingPlantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val categoryId = savedStateHandle.get<Int>("categoryId")
    val category = savedStateHandle.get<String>("name")

    private var _pagingPlants = MutableStateFlow<PagingData<PlantItem>>(PagingData.empty())
    val pagingPlants: StateFlow<PagingData<PlantItem>> = _pagingPlants

    private var _swipeRefreshing = mutableStateOf(false)
    val swipeRefreshing: State<Boolean> = _swipeRefreshing

    init {
        if (categoryId!! > 0) {
            onEvent(PlantListEvent.LoadPlantsByCategory)
        } else {
            onEvent(PlantListEvent.LoadPlants)
        }
    }

    fun onEvent(event: PlantListEvent) {
        when (event) {
            PlantListEvent.LoadPlants -> getPlants()

            PlantListEvent.LoadPlantsByCategory -> getPlantsByCategory()
        }
    }

    fun onSwipeRefreshingChanged(isRefreshing: Boolean) {
        _swipeRefreshing.value = isRefreshing
    }

    private fun getPlants() {
        viewModelScope.launch {
            getPagingPlantsUseCase(
                isTrending = isTrending,
                forBeginner = forBeginner,
                searchQuery = null
            ).cachedIn(viewModelScope).collect {
                _pagingPlants.value = it
            }
        }
    }

    private fun getPlantsByCategory() {
        viewModelScope.launch {
            getPagingPlantsByCategoryUseCase(categoryId!!)
                .cachedIn(viewModelScope)
                .collect {
                    _pagingPlants.value = it
                }
        }
    }
}