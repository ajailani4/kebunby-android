package com.kebunby.kebunby.ui.feature.splash

import androidx.lifecycle.ViewModel
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getUserCredentialUseCase: GetUserCredentialUseCase
) : ViewModel() {
    fun getUserCredential() = getUserCredentialUseCase.invoke()
}