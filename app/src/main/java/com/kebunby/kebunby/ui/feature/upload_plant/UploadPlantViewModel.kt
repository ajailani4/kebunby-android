package com.kebunby.kebunby.ui.feature.upload_plant

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadPlantViewModel @Inject constructor(
    private val getUserCredentialUseCase: GetUserCredentialUseCase
) : ViewModel() {
    private var _photo = mutableStateOf<File?>(null)
    val photo: State<File?> = _photo

    private var _cameraScreenVis = mutableStateOf(true)
    val cameraScreenVis: State<Boolean> = _cameraScreenVis

    fun onPhotoChanged(file: File?) {
        _photo.value = file
    }

    fun onCameraScreenVisChanged(visibility: Boolean) {
        _cameraScreenVis.value = visibility
    }
}