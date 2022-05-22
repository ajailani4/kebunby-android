package com.kebunby.kebunby.ui.feature.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.User
import com.kebunby.kebunby.domain.use_case.user.GetUserProfileUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.kebunby.ui.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserCredentialUseCase: GetUserCredentialUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {
    var userProfileState by mutableStateOf<UIState<User>>(UIState.Idle)

    init {
        onEvent(ProfileEvent.LoadUserProfile)
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.Idle -> idle()

            ProfileEvent.LoadUserProfile -> getUserProfile()
        }
    }

    private fun idle() {
        userProfileState = UIState.Idle
    }

    private fun getUserProfile() {
        userProfileState = UIState.Loading

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()
            val resource = getUserProfileUseCase.invoke(userCredential.username!!)

            resource.catch {
                userProfileState = UIState.Error(it.localizedMessage)
            }.collect {
                userProfileState = when (it) {
                    is Resource.Success -> UIState.Success(it.data)

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }
}