package com.kebunby.app.viewmodel

import com.kebunby.app.data.Resource
import com.kebunby.app.data.model.UserCredential
import com.kebunby.app.domain.use_case.user.RegisterUserUseCase
import com.kebunby.app.domain.use_case.user_credential.SaveUserCredentialUseCase
import com.kebunby.app.ui.common.UIState
import com.kebunby.app.ui.feature.register.RegisterEvent
import com.kebunby.app.ui.feature.register.RegisterViewModel
import com.kebunby.app.util.TestCoroutineRule
import com.kebunby.app.util.generateUserCredential
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var registerUserUseCase: RegisterUserUseCase

    @Mock
    private lateinit var saveUserCredentialUseCase: SaveUserCredentialUseCase

    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(registerUserUseCase, saveUserCredentialUseCase)
    }

    @Test
    fun `Register should return success`() {
        testCoroutineRule.runTest {
            val resource = flow {
                emit(Resource.Success(generateUserCredential()))
            }

            doReturn(resource).`when`(registerUserUseCase)(any())

            registerViewModel.onEvent(RegisterEvent.Submit)

            val isSuccess = when (registerViewModel.registerState.value) {
                is UIState.Success -> true

                else -> false
            }

            assertEquals("Should return success", true, isSuccess)

            verify(registerUserUseCase)(any())
        }
    }

    @Test
    fun `Register should return fail`() {
        testCoroutineRule.runTest {
            val resource = flow {
                emit(Resource.Error<UserCredential>())
            }

            doReturn(resource).`when`(registerUserUseCase)(any())

            registerViewModel.onEvent(RegisterEvent.Submit)

            val isSuccess = when (registerViewModel.registerState.value) {
                is UIState.Success -> true

                else -> false
            }

            assertEquals("Should return fail", false, isSuccess)

            verify(registerUserUseCase)(any())
        }
    }
}