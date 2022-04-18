package com.kebunby.kebunby.ui.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.domain.use_case.plant.GetPlantsUseCase
import com.kebunby.kebunby.domain.use_case.user.GetUserProfileUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserCredentialUseCase: GetUserCredentialUseCase,
    private val getPlantsUseCase: GetPlantsUseCase
) : ViewModel() {
    var userProfileState by mutableStateOf<HomeState>(HomeState.Idle)
    var trendingPlantsState by mutableStateOf<HomeState>(HomeState.Idle)
    var forBeginnerPlantsState by mutableStateOf<HomeState>(HomeState.Idle)

    init {
        onEvent(HomeEvent.LoadUserProfile)
        onEvent(HomeEvent.LoadTrendingPlants)
        onEvent(HomeEvent.LoadForBeginnerPlants)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadUserProfile -> getUserProfile()

            HomeEvent.LoadTrendingPlants -> getTrendingPlants()

            HomeEvent.LoadForBeginnerPlants -> getForBeginnerPlants()
        }
    }

    private fun getUserProfile() {
        userProfileState = HomeState.LoadingUserProfile

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()

            val resource = getUserProfileUseCase.invoke(userCredential.username!!)

            resource.catch {
                userProfileState = HomeState.ErrorUserProfile(it.localizedMessage)
            }.collect {
                userProfileState = when (it) {
                    is Resource.Success -> HomeState.UserProfile(it.data)

                    is Resource.Error -> HomeState.FailUserProfile(it.message)
                }
            }
        }
    }

    private fun getTrendingPlants() {
        trendingPlantsState = HomeState.LoadingTrendingPlants

        viewModelScope.launch {
            val resource = getPlantsUseCase.invoke(
                page = 1,
                size = 5,
                isTrending = true
            )

            resource.catch {
                trendingPlantsState = HomeState.ErrorTrendingPlants(it.localizedMessage)
            }.collect {
                trendingPlantsState = when (it) {
                    is Resource.Success -> HomeState.TrendingPlants(it.data)

                    is Resource.Error -> HomeState.FailTrendingPlants(it.message)
                }
            }
        }
    }

    private fun getForBeginnerPlants() {
        forBeginnerPlantsState = HomeState.LoadingForBeginnerPlants

        viewModelScope.launch {
            val resource = getPlantsUseCase.invoke(
                page = 1,
                size = 5,
                forBeginner = true
            )

            resource.catch {
                forBeginnerPlantsState = HomeState.ErrorForBeginnerPlants(it.localizedMessage)
            }.collect {
                forBeginnerPlantsState = when (it) {
                    is Resource.Success -> HomeState.ForBeginnerPlants(it.data)

                    is Resource.Error -> HomeState.FailForBeginnerPlants(it.message)
                }
            }
        }
    }
}