package com.kebunby.kebunby.viewmodel

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.domain.use_case.user.RegisterUserUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.SaveUserCredentialUseCase
import com.kebunby.kebunby.ui.common.UIState
import com.kebunby.kebunby.ui.feature.register.RegisterEvent
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
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(generateUserCredential()))
            }

            doReturn(resource).`when`(registerUserUseCase).invoke(any())

            registerViewModel.onEvent(RegisterEvent.Submit)

            val isSuccess = when (registerViewModel.registerState) {
                is UIState.Success -> true

                is UIState.Fail -> false

                is UIState.Error -> false

                else -> false
            }

            assertEquals("Should return success", true, isSuccess)

            verify(registerUserUseCase).invoke(any())
        }
    }

    @Test
    fun `Register should return fail`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<UserCredential>())
            }

            doReturn(resource).`when`(registerUserUseCase).invoke(any())

            registerViewModel.onEvent(RegisterEvent.Submit)

            val isSuccess = when (registerViewModel.registerState) {
                is UIState.Success -> true

                is UIState.Fail -> false

                is UIState.Error -> false

                else -> false
            }

            assertEquals("Should return fail", false, isSuccess)

            verify(registerUserUseCase).invoke(any())
        }
    }
}