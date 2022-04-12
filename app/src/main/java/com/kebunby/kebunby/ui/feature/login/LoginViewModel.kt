package com.kebunby.kebunby.ui.feature.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.request.LoginRequest
import com.kebunby.kebunby.domain.use_case.LoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {
    var loginState by mutableStateOf<LoginState>(LoginState.Idle)
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
        loginState = LoginState.Idle
    }

    private fun login() {
        loginState = LoginState.LoggingIn

        viewModelScope.launch {
            val resource = loginUserUseCase.invoke(
                LoginRequest(
                    username = username,
                    password = password
                )
            )

            resource.catch {
                loginState = LoginState.Error(it.localizedMessage)
            }.collect {
                loginState = when (it) {
                    is Resource.Success -> {
                        Log.d("AccesssToken", it.data?.accessToken!!)
                        LoginState.Success
                    }

                    is Resource.Error -> {
                        LoginState.Fail(it.message)
                    }
                }
            }
        }
    }
}