package com.kebunby.kebunby.ui.feature.plant_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kebunby.kebunby.domain.use_case.plant.GetPagingPlantsByCategoryUseCase
import com.kebunby.kebunby.domain.use_case.plant.GetPagingPlantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun getPagingPlants() = getPagingPlantsUseCase.invoke(
        isTrending = isTrending,
        forBeginner = forBeginner,
        searchQuery = searchQuery
    ).cachedIn(viewModelScope)

    fun getPagingPlantsByCat() =
        getPagingPlantsByCategoryUseCase.invoke(categoryId!!).cachedIn(viewModelScope)
}