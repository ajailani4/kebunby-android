package com.kebunby.app.ui.feature.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.app.data.Resource
import com.kebunby.app.data.model.request.RegisterRequest
import com.kebunby.app.domain.use_case.user.RegisterUserUseCase
import com.kebunby.app.domain.use_case.user_credential.SaveUserCredentialUseCase
import com.kebunby.app.ui.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val saveUserCredentialUseCase: SaveUserCredentialUseCase
) : ViewModel() {
    private var _registerState = mutableStateOf<UIState<Nothing>>(UIState.Idle)
    val registerState: State<UIState<Nothing>> = _registerState

    private var _username = mutableStateOf("")
    val username: State<String> = _username

    private var _email = mutableStateOf("")
    val email: State<String> = _email

    private var _name = mutableStateOf("")
    val name: State<String> = _name

    private var _password = mutableStateOf("")
    val password: State<String> = _password

    private var _passwordVisibility = mutableStateOf(false)
    val passwordVisibility: State<Boolean> = _passwordVisibility

    fun onEvent(event: RegisterEvent) {
        when (event) {
            RegisterEvent.Idle -> idle()

            RegisterEvent.Submit -> register()
        }
    }

    fun onUsernameChanged(text: String) {
        _username.value = text
    }

    fun onEmailChanged(text: String) {
        _email.value = text
    }

    fun onNameChanged(text: String) {
        _name.value = text
    }

    fun onPasswordChanged(text: String) {
        _password.value = text
    }

    fun onPasswordVisibilityChanged() {
        _passwordVisibility.value = !passwordVisibility.value
    }

    private fun idle() {
        _registerState.value = UIState.Idle
    }

    private fun register() {
        _registerState.value = UIState.Loading

        viewModelScope.launch {
            val resource = registerUserUseCase(
                RegisterRequest(
                    username = _username.value,
                    email = _email.value,
                    password = password.value,
                    name = name.value
                )
            )

            resource.catch {
                _registerState.value = UIState.Error(it.localizedMessage)
            }.collect {
                _registerState.value = when (it) {
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