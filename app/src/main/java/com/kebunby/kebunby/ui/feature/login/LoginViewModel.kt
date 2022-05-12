package com.kebunby.kebunby.ui.feature.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.request.LoginRequest
import com.kebunby.kebunby.domain.use_case.user.LoginUserUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.SaveUserCredentialUseCase
import com.kebunby.kebunby.ui.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val saveUserCredentialUseCase: SaveUserCredentialUseCase
) : ViewModel() {
    var loginState by mutableStateOf<UIState<Nothing>>(UIState.Idle)
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVisibility by mutableStateOf(false)

    fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.Idle -> idle()

            LoginEvent.Submit -> login()
        }
    }

    fun onUsernameChanged(text: String) {
        username = text
    }

    fun onPasswordChanged(text: String) {
        password = text
    }

    fun onPasswordVisibilityChanged() {
        passwordVisibility = !passwordVisibility
    }

    private fun idle() {
        loginState = UIState.Idle
    }

    private fun login() {
        loginState = UIState.Loading

        viewModelScope.launch {
            val resource = loginUserUseCase.invoke(
                LoginRequest(
                    username = username,
                    password = password
                )
            )

            resource.catch {
                loginState = UIState.Error(it.localizedMessage)
            }.collect {
                loginState = when (it) {
                    is Resource.Success -> {
                        saveUserCredentialUseCase(it.data!!)
                        UIState.Success(null)
                    }

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }
}