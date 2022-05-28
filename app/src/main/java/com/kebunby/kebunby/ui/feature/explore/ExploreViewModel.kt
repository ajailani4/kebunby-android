package com.kebunby.kebunby.ui.feature.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.domain.use_case.plant.GetPagingPlantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val getPagingPlantsUseCase: GetPagingPlantsUseCase
) : ViewModel() {
    var pagingPlants = MutableStateFlow<PagingData<PlantItem>>(PagingData.empty())
    var swipeRefreshing by mutableStateOf(false)
    var searchQuery by mutableStateOf("")
    var isSearched by mutableStateOf(false)

    init {
        onEvent(ExploreEvent.LoadPlants)
    }

    fun onEvent(event: ExploreEvent) {
        when (event) {
            ExploreEvent.LoadPlants -> getPlants()
        }
    }

    fun onSwipeRefreshingChanged(isRefreshing: Boolean) {
        swipeRefreshing = isRefreshing
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery = query
    }

    fun onSearchPlant(_isSearched: Boolean) {
        isSearched = _isSearched
    }

    private fun getPlants() {
        viewModelScope.launch {
            getPagingPlantsUseCase.invoke(
                isTrending = null,
                forBeginner = null,
                searchQuery = searchQuery
            ).cachedIn(viewModelScope).collect {
                pagingPlants.value = it
            }
        }
    }
}