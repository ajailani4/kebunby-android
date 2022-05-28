package com.kebunby.kebunby.ui.feature.profile.uploaded

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.domain.use_case.plant.GetPlantActivitiesUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadedViewModel @Inject constructor(
    private val getUserCredentialUseCase: GetUserCredentialUseCase,
    private val getPlantActivitiesUseCase: GetPlantActivitiesUseCase
) : ViewModel() {
    private var _pagingPlants = MutableStateFlow<PagingData<PlantItem>>(PagingData.empty())
    val pagingPlants: StateFlow<PagingData<PlantItem>> = _pagingPlants

    init {
        onEvent(UploadedEvent.LoadPlants)
    }

    fun onEvent(event: UploadedEvent) {
        when (event) {
            UploadedEvent.LoadPlants -> getPlants()
        }
    }

    private fun getPlants() {
        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            getPlantActivitiesUseCase.invoke(
                username = userCredential.username!!,
                isPlanting = null,
                isPlanted = null
            ).cachedIn(viewModelScope).collect {
                _pagingPlants.value = it
            }
        }
    }
}