package com.kebunby.kebunby.ui.feature.profile

sealed class ProfileEvent {
    object Idle : ProfileEvent()
    object LoadUserProfile : ProfileEvent()
    object Logout : ProfileEvent()
}
