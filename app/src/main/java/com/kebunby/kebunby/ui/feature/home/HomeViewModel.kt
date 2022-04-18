package com.kebunby.kebunby.ui.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.kebunby.data.Resource
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
    private val getUserCredentialUseCase: GetUserCredentialUseCase
) : ViewModel() {
    var userProfileState by mutableStateOf<HomeState>(HomeState.Idle)

    init {
        onEvent(HomeEvent.LoadUserProfile)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadUserProfile -> getUserProfile()
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
                    is Resource.Success -> {
                        HomeState.UserProfile(it.data)
                    }

                    is Resource.Error -> {
                        HomeState.FailUserProfile(it.message)
                    }
                }
            }
        }
    }
}