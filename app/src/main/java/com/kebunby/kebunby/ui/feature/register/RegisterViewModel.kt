package com.kebunby.kebunby.ui.feature.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.request.RegisterRequest
import com.kebunby.kebunby.domain.use_case.user.RegisterUserUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.SaveUserCredentialUseCase
import com.kebunby.kebunby.ui.common.UIState
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
    var registerState by mutableStateOf<UIState<Nothing>>(UIState.Idle)
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var name by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVisibility by mutableStateOf(false)

    fun onEvent(event: RegisterEvent) {
        when (event) {
            RegisterEvent.Idle -> idle()

            RegisterEvent.Submit -> register()
        }
    }

    fun onUsernameChanged(text: String) {
        username = text
    }

    fun onEmailChanged(text: String) {
        email = text
    }

    fun onNameChanged(text: String) {
        name = text
    }

    fun onPasswordChanged(text: String) {
        password = text
    }

    fun onPasswordVisibilityChanged() {
        passwordVisibility = !passwordVisibility
    }

    private fun idle() {
        registerState = UIState.Idle
    }

    private fun register() {
        registerState = UIState.Loading

        viewModelScope.launch {
            val resource = registerUserUseCase.invoke(
                RegisterRequest(
                    username = username,
                    email = email,
                    password = password,
                    name = name
                )
            )

            resource.catch {
                registerState = UIState.Error(it.localizedMessage)
            }.collect {
                registerState = when (it) {
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