package com.kebunby.kebunby.ui.feature.register

sealed class RegisterEvent {
    object Idle : RegisterEvent()
    object Submit : RegisterEvent()
}