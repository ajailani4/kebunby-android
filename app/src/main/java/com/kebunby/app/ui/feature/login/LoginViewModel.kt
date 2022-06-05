package com.kebunby.app.ui.feature.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.app.data.Resource
import com.kebunby.app.data.model.request.LoginRequest
import com.kebunby.app.domain.use_case.user.LoginUserUseCase
import com.kebunby.app.domain.use_case.user_credential.SaveUserCredentialUseCase
import com.kebunby.app.ui.common.UIState
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
    private var _loginState = mutableStateOf<UIState<Nothing>>(UIState.Idle)
    val loginState: State<UIState<Nothing>> = _loginState

    private var _username = mutableStateOf("")
    val username: State<String> = _username

    private var _password = mutableStateOf("")
    val password: State<String> = _password

    private var _passwordVisibility = mutableStateOf(false)
    val passwordVisibility: State<Boolean> = _passwordVisibility

    fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.Idle -> idle()

            LoginEvent.Submit -> login()
        }
    }

    fun onUsernameChanged(text: String) {
        _username.value = text
    }

    fun onPasswordChanged(text: String) {
        _password.value = text
    }

    fun onPasswordVisibilityChanged() {
        _passwordVisibility.value = !_passwordVisibility.value
    }

    private fun idle() {
        _loginState.value = UIState.Idle
    }

    private fun login() {
        _loginState.value = UIState.Loading

        viewModelScope.launch {
            val resource = loginUserUseCase(
                LoginRequest(
                    username = _username.value,
                    password = _password.value
                )
            )

            resource.catch {
                _loginState.value = UIState.Error(it.localizedMessage)
            }.collect {
                _loginState.value = when (it) {
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