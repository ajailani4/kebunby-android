package com.kebunby.app.ui.common

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private var _isReloaded = mutableStateOf(false)
    val isReloaded: State<Boolean> = _isReloaded

    fun onReload(isReloaded: Boolean) {
        _isReloaded.value = isReloaded
    }
}