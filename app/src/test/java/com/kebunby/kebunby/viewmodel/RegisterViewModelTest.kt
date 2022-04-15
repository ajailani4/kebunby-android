package com.kebunby.kebunby.viewmodel

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.domain.use_case.user.RegisterUserUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.SaveUserCredentialUseCase
import com.kebunby.kebunby.ui.feature.register.RegisterEvent
import com.kebunby.kebunby.ui.feature.register.RegisterState
import com.kebunby.kebunby.ui.feature.register.RegisterViewModel
import com.kebunby.kebunby.util.TestCoroutineRule
import com.kebunby.kebunby.util.generateUserCredential
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

    // Dependency
    @Mock
    private lateinit var registerUserUseCase: RegisterUserUseCase

    @Mock
    private lateinit var saveUserCredentialUseCase: SaveUserCredentialUseCase

    // SUT
    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(registerUserUseCase, saveUserCredentialUseCase)
    }

    @Test
    fun register_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Success(generateUserCredential()))
            }

            doReturn(resource).`when`(registerUserUseCase).invoke(any())

            // Act
            registerViewModel.onEvent(RegisterEvent.Submit)

            val registerState = registerViewModel.registerState
            var isSuccess = false

            when (registerState) {
                is RegisterState.Success -> isSuccess = true

                is RegisterState.Fail -> isSuccess = false

                is RegisterState.Error -> isSuccess = false

                else -> {}
            }

            // Assert
            assertEquals("Should return success", true, isSuccess)

            // Verify
            verify(registerUserUseCase).invoke(any())
        }
    }

    @Test
    fun register_ShouldReturnFail() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Error<UserCredential>())
            }

            doReturn(resource).`when`(registerUserUseCase).invoke(any())

            // Act
            registerViewModel.onEvent(RegisterEvent.Submit)

            val registerState = registerViewModel.registerState
            var isSuccess = false

            when (registerState) {
                is RegisterState.Success -> isSuccess = true

                is RegisterState.Fail -> isSuccess = false

                is RegisterState.Error -> isSuccess = false

                else -> {}
            }

            // Assert
            assertEquals("Should return fail", false, isSuccess)

            // Verify
            verify(registerUserUseCase).invoke(any())
        }
    }
}