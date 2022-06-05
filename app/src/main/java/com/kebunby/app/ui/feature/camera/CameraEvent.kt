package com.kebunby.app.ui.feature.camera

sealed class CameraEvent {
    object SwitchLens : CameraEvent()
    object Capture : CameraEvent()
    object ViewGallery : CameraEvent()
}
