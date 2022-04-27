package com.kebunby.kebunby.ui.feature.plant_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.kebunby.domain.use_case.plant.GetPagingPlantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlantListViewModel @Inject constructor(
    private val getPagingPlantsUseCase: GetPagingPlantsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val isTrending = savedStateHandle.get<Boolean>("isTrending")
    val forBeginner = savedStateHandle.get<Boolean>("forBeginner")
    val searchQuery = savedStateHandle.get<String>("searchQuery")

    fun getPlantsByPaging() = getPagingPlantsUseCase.invoke(
        isTrending = isTrending,
        forBeginner = forBeginner,
        searchQuery = searchQuery
    ).cachedIn(viewModelScope)
}