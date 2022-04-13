package com.kebunby.kebunby.ui.feature.login

sealed class LoginState {
    object Idle : LoginState()
    object LoggingIn : LoginState()
    object Success : LoginState()
    data class Fail(val message: String?) : LoginState()
    data class Error(val message: String?) : LoginState()
}
