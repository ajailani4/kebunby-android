package com.kebunby.kebunby.ui.feature.register

sealed class RegisterState {
    object Idle : RegisterState()
    object Registering : RegisterState()
    object Success : RegisterState()
    data class Fail(val message: String?) : RegisterState()
    data class Error(val message: String?) : RegisterState()
}
