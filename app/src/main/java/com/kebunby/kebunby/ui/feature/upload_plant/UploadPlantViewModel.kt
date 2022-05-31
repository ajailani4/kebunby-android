package com.kebunby.kebunby.ui.feature.upload_plant

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.kebunby.util.ListAction
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadPlantViewModel @Inject constructor(
    private val getUserCredentialUseCase: GetUserCredentialUseCase
) : ViewModel() {
    private var _cameraScreenVis = mutableStateOf(true)
    val cameraScreenVis: State<Boolean> = _cameraScreenVis

    private var _photo = mutableStateOf<File?>(null)
    val photo: State<File?> = _photo

    private var _plantName = mutableStateOf("")
    val plantName: State<String> = _plantName

    private var _growthEst = mutableStateOf("")
    val growthEst: State<String> = _growthEst

    private var _wateringFreq = mutableStateOf("")
    val wateringFreq: State<String> = _wateringFreq

    private var _tools = mutableStateListOf("Tool 1")
    val tools: SnapshotStateList<String> = _tools

    private var _materials = mutableStateListOf("Material 1")
    val materials: SnapshotStateList<String> = _materials

    private var _steps = mutableStateListOf("Step 1")
    val steps: SnapshotStateList<String> = _steps

    fun onCameraScreenVisChanged(visibility: Boolean) {
        _cameraScreenVis.value = visibility
    }

    fun onPhotoChanged(file: File?) {
        _photo.value = file
    }

    fun onPlantNameChanged(text: String) {
        _plantName.value = text
    }

    fun onGrowthEstChanged(text: String) {
        _growthEst.value = text
    }

    fun onWateringFreqChanged(text: String) {
        _wateringFreq.value = text
    }

    fun onToolsChanged(index: Int?, tool: String?, action: ListAction) {
        when (action) {
            ListAction.ADD_ITEM -> {
                if (index == null && tool != null) _tools.add(tool)
            }

            ListAction.UPDATE_ITEM -> {
                if (index != null && tool != null) _tools[index] = tool
            }

            ListAction.DELETE_ITEM -> {
                if (index != null) _tools.removeAt(index)
            }
        }
    }

    fun onMaterialsChanged(index: Int?, material: String?, action: ListAction) {
        when (action) {
            ListAction.ADD_ITEM -> {
                if (index == null && material != null) _materials.add(material)
            }

            ListAction.UPDATE_ITEM -> {
                if (index != null && material != null) _materials[index] = material
            }

            ListAction.DELETE_ITEM -> {
                if (index != null) _materials.removeAt(index)
            }
        }
    }

    fun onStepsChanged(index: Int?, step: String?, action: ListAction) {
        when (action) {
            ListAction.ADD_ITEM -> {
                if (index == null && step != null) _steps.add(step)
            }

            ListAction.UPDATE_ITEM -> {
                if (index != null && step != null) _steps[index] = step
            }

            ListAction.DELETE_ITEM -> {
                if (index != null) _steps.removeAt(index)
            }
        }
    }
}