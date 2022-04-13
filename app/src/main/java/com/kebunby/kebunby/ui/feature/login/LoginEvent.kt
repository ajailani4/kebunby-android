package com.kebunby.kebunby.ui.feature.login

sealed class LoginEvent {
    object Idle : LoginEvent()
    object Submit : LoginEvent()
}
