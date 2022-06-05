package com.kebunby.app.ui.feature.explore

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kebunby.app.data.model.PlantItem
import com.kebunby.app.domain.use_case.plant.GetPagingPlantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val getPagingPlantsUseCase: GetPagingPlantsUseCase
) : ViewModel() {
    private var _pagingPlants = MutableStateFlow<PagingData<PlantItem>>(PagingData.empty())
    val pagingPlants: StateFlow<PagingData<PlantItem>> = _pagingPlants

    private var _swipeRefreshing = mutableStateOf(false)
    val swipeRefreshing: State<Boolean> = _swipeRefreshing

    private var _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private var _isSearched = mutableStateOf(false)
    val isSearched: State<Boolean> = _isSearched

    init {
        onEvent(ExploreEvent.LoadPlants)
    }

    fun onEvent(event: ExploreEvent) {
        when (event) {
            ExploreEvent.LoadPlants -> getPlants()
        }
    }

    fun onSwipeRefreshingChanged(isRefreshing: Boolean) {
        _swipeRefreshing.value = isRefreshing
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onSearchPlant(isSearched: Boolean) {
        _isSearched.value = isSearched
    }

    private fun getPlants() {
        viewModelScope.launch {
            getPagingPlantsUseCase(
                isTrending = null,
                forBeginner = null,
                searchQuery = _searchQuery.value
            ).cachedIn(viewModelScope).collect {
                _pagingPlants.value = it
            }
        }
    }
}