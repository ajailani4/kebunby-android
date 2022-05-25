package com.kebunby.kebunby.ui.feature.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kebunby.kebunby.domain.use_case.plant.GetPagingPlantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val getPagingPlantsUseCase: GetPagingPlantsUseCase
) : ViewModel() {
    var searchQuery by mutableStateOf("")

    fun onSearchQueryChanged(query: String) {
        searchQuery = query
    }
}