package com.kebunby.app.ui.feature.register

sealed class RegisterEvent {
    object Idle : RegisterEvent()
    object Submit : RegisterEvent()
}