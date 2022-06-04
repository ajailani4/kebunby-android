package com.kebunby.kebunby.ui.feature.upload_edit_plant

sealed class UploadEditPlantEvent {
    object Idle : UploadEditPlantEvent()
    object LoadPlantDetail : UploadEditPlantEvent()
    object LoadPlantCategories : UploadEditPlantEvent()
    object UploadPlant : UploadEditPlantEvent()
    object EditPlant : UploadEditPlantEvent()
}
