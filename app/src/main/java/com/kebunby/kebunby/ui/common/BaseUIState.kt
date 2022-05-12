package com.kebunby.kebunby.ui.common

sealed class BaseUIState<out T> {
    object Idle : BaseUIState<Nothing>()
    object Loading : BaseUIState<Nothing>()
    data class Success<T>(val data: T?) : BaseUIState<T>()
    data class Fail(val message: String?) : BaseUIState<Nothing>()
    data class Error(val message: String?) : BaseUIState<Nothing>()
}
