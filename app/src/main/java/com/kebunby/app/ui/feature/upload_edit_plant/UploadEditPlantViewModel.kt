package com.kebunby.app.ui.feature.upload_edit_plant

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kebunby.app.data.Resource
import com.kebunby.app.data.model.Plant
import com.kebunby.app.data.model.PlantCategory
import com.kebunby.app.domain.use_case.plant.EditPlantUseCase
import com.kebunby.app.domain.use_case.plant.GetPlantCategoriesUseCase
import com.kebunby.app.domain.use_case.plant.GetPlantDetailUseCase
import com.kebunby.app.domain.use_case.plant.UploadPlantUseCase
import com.kebunby.app.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.app.ui.common.UIState
import com.kebunby.app.util.ListAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadEditPlantViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPlantDetailUseCase: GetPlantDetailUseCase,
    private val getUserCredentialUseCase: GetUserCredentialUseCase,
    private val getPlantCategoriesUseCase: GetPlantCategoriesUseCase,
    private val uploadPlantUseCase: UploadPlantUseCase,
    private val editPlantUseCase: EditPlantUseCase
) : ViewModel() {
    val plantId = savedStateHandle.get<Int>("plantId")

    private var _plantDetailState = mutableStateOf<UIState<Plant>>(UIState.Idle)
    val plantDetailState: State<UIState<Plant>> = _plantDetailState

    private var _plantCategoriesState = mutableStateOf<UIState<List<PlantCategory>>>(UIState.Idle)
    val plantCategoriesState: State<UIState<List<PlantCategory>>> = _plantCategoriesState

    private var _uploadPlantState = mutableStateOf<UIState<Any>>(UIState.Idle)
    val uploadPlantState: State<UIState<Any>> = _uploadPlantState

    private var _editPlantState = mutableStateOf<UIState<Any>>(UIState.Idle)
    val editPlantState: State<UIState<Any>> = _editPlantState

    private var _cameraScreenVis = mutableStateOf(true)
    val cameraScreenVis: State<Boolean> = _cameraScreenVis

    private var _photo = mutableStateOf<Any?>(null)
    val photo: State<Any?> = _photo

    private var _plantName = mutableStateOf("")
    val plantName: State<String> = _plantName

    private var _selectedCategory = mutableStateOf<PlantCategory?>(null)
    val selectedCategory: State<PlantCategory?> = _selectedCategory

    private var _categorySpinnerVis = mutableStateOf(false)
    val categorySpinnerVis: State<Boolean> = _categorySpinnerVis

    private var _growthEst = mutableStateOf("")
    val growthEst: State<String> = _growthEst

    private var _wateringFreq = mutableStateOf("")
    val wateringFreq: State<String> = _wateringFreq

    private var _desc = mutableStateOf("")
    val desc: State<String> = _desc

    private var _tools = mutableStateListOf("")
    val tools: SnapshotStateList<String> = _tools

    private var _materials = mutableStateListOf("")
    val materials: SnapshotStateList<String> = _materials

    private var _steps = mutableStateListOf("")
    val steps: SnapshotStateList<String> = _steps

    private var popularity = 0
    private var publishedOn = ""

    init {
        if (plantId!! > 0) {
            _cameraScreenVis.value = false
            onEvent(UploadEditPlantEvent.LoadPlantDetail)
        }

        onEvent(UploadEditPlantEvent.LoadPlantCategories)
    }

    fun onEvent(event: UploadEditPlantEvent) {
        when (event) {
            UploadEditPlantEvent.Idle -> idle()

            UploadEditPlantEvent.LoadPlantDetail -> getPlantDetail()

            UploadEditPlantEvent.LoadPlantCategories -> getPlantCategories()

            UploadEditPlantEvent.UploadPlant -> uploadPlant()

            UploadEditPlantEvent.EditPlant -> editPlant()
        }
    }

    fun onCameraScreenVisChanged(visibility: Boolean) {
        _cameraScreenVis.value = visibility
    }

    fun onPhotoChanged(photo: Any?) {
        _photo.value = photo
    }

    fun onSelectedCategoryChanged(category: PlantCategory) {
        _selectedCategory.value = category
    }

    fun onCategorySpinnerVisChanged(visibility: Boolean) {
        _categorySpinnerVis.value = visibility
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

    fun onDescChanged(text: String) {
        _desc.value = text
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

    fun setTools(items: List<String>) {
        _tools.clear()
        _tools.addAll(items)
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

    fun setMaterials(items: List<String>) {
        _materials.clear()
        _materials.addAll(items)
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

    fun setSteps(items: List<String>) {
        _steps.clear()
        _steps.addAll(items)
    }

    fun setPopularity(_popularity: Int) {
        popularity = _popularity
    }

    fun setPublishedOn(_publishedOn: String) {
        publishedOn = _publishedOn
    }

    private fun idle() {
        _plantDetailState.value = UIState.Idle
        _uploadPlantState.value = UIState.Idle
    }

    private fun getPlantDetail() {
        _plantDetailState.value = UIState.Loading

        viewModelScope.launch {
            val resource = getPlantDetailUseCase(plantId!!)

            resource.catch {
                _plantDetailState.value = UIState.Error(it.localizedMessage)
            }.collect {
                _plantDetailState.value = when (it) {
                    is Resource.Success -> {
                        UIState.Success(it.data)
                    }

                    is Resource.Error -> {
                        UIState.Fail(it.message)
                    }
                }
            }
        }
    }

    private fun getPlantCategories() {
        viewModelScope.launch {
            val resource = getPlantCategoriesUseCase()

            resource.catch {
                _plantCategoriesState.value = UIState.Error(it.localizedMessage)
            }.collect {
                _plantCategoriesState.value = when (it) {
                    is Resource.Success -> UIState.Success(it.data)

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }

    private fun uploadPlant() {
        _uploadPlantState.value = UIState.Loading

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase().first()

            val resource = uploadPlantUseCase(
                name = _plantName.value,
                image = _photo.value as File,
                category = _selectedCategory.value!!.id.toString(),
                wateringFreq = _wateringFreq.value,
                growthEst = _growthEst.value,
                desc = _desc.value,
                tools = _tools,
                materials = _materials,
                steps = _steps,
                author = userCredential.username!!
            )

            resource.catch {
                _uploadPlantState.value = UIState.Error(it.localizedMessage)
            }.collect {
                _uploadPlantState.value = when (it) {
                    is Resource.Success -> UIState.Success(it.data)

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }

    private fun editPlant() {
        _editPlantState.value = UIState.Loading

        viewModelScope.launch {
            val userCredential = getUserCredentialUseCase().first()

            val resource = editPlantUseCase(
                id = plantId!!,
                name = _plantName.value,
                image = if (_photo.value is File) _photo.value as File else null,
                category = _selectedCategory.value!!.id.toString(),
                wateringFreq = _wateringFreq.value,
                growthEst = _growthEst.value,
                desc = _desc.value,
                tools = _tools,
                materials = _materials,
                steps = _steps,
                author = userCredential.username!!,
                popularity = popularity.toString(),
                publishedOn = publishedOn
            )

            resource.catch {
                _editPlantState.value = UIState.Error(it.localizedMessage)
            }.collect {
                _editPlantState.value = when (it) {
                    is Resource.Success -> UIState.Success(it.data)

                    is Resource.Error -> UIState.Fail(it.message)
                }
            }
        }
    }
}