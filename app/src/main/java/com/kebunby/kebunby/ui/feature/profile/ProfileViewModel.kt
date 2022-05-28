package com.kebunby.kebunby.ui.feature.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.User
import com.kebunby.kebunby.domain.use_case.user.GetUserProfileUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.DeleteUserCredentialUseCase
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
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val deleteUserCredentialUseCase: DeleteUserCredentialUseCase
) : ViewModel() {
    private var _userProfileState = mutableStateOf<UIState<User>>(UIState.Idle)
    val userProfileState: State<UIState<User>> = _userProfileState

    private var _logoutState = mutableStateOf<UIState<Nothing>>(UIState.Idle)
    val logoutState: State<UIState<Nothing>> = _logoutState

    private var _swipeRefreshing = mutableStateOf(false)
    val swipeRefreshing: State<Boolean> = _swipeRefreshing

    private var _logoutConfirmDlgVis = mutableStateOf(false)
    val logoutConfirmDlgVis: State<Boolean> = _logoutConfirmDlgVis

    init {
        onEvent(ProfileEvent.LoadUserProfile)
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.Idle -> idle()

            ProfileEvent.LoadUserProfile -> getUserProfile()

            ProfileEvent.Logout -> logout()
        }
    }

    fun onSwipeRefreshingChanged(isRefreshing: Boolean) {
        _swipeRefreshing.value = isRefreshing
    }

    fun onLogoutConfirmDlgVisChanged(visibility: Boolean) {
        _logoutConfirmDlgVis.value = visibility
    }

    private fun idle() {
        _userProfileState.value = UIState.Idle
    }

    private fun getUserProfile() {
        _userProfileState.value = UIState.Loading

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase.invoke().first()
            val resource = getUserProfileUseCase.invoke(userCredential.username!!)

            resource.catch {
                _userProfileState.value = UIState.Error(it.localizedMessage)
            }.collect {
                _userProfileState.value = when (it) {
                    is Resource.Success -> UIState.Success(it.data)

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }

    private fun logout() {
        _logoutState.value = UIState.Loading

        viewModelScope.launch {
            deleteUserCredentialUseCase.invoke()
            _logoutState.value = UIState.Success(null)
        }
    }
}