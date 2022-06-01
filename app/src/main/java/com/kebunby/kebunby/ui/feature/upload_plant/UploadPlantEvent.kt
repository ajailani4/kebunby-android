package com.kebunby.kebunby.ui.feature.upload_plant

sealed class UploadPlantEvent {
    object LoadPlantCategories : UploadPlantEvent()
    object UploadPlant : UploadPlantEvent()
}
