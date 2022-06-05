package com.kebunby.app.ui.feature.login

sealed class LoginEvent {
    object Idle : LoginEvent()
    object Submit : LoginEvent()
}
