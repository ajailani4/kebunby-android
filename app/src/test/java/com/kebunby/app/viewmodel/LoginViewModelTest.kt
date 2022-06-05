package com.kebunby.app.viewmodel

import com.kebunby.app.data.Resource
import com.kebunby.app.data.model.UserCredential
import com.kebunby.app.domain.use_case.user.LoginUserUseCase
import com.kebunby.app.domain.use_case.user_credential.SaveUserCredentialUseCase
import com.kebunby.app.ui.common.UIState
import com.kebunby.app.ui.feature.login.LoginEvent
import com.kebunby.app.ui.feature.login.LoginViewModel
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
class LoginViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var loginUserUseCase: LoginUserUseCase

    @Mock
    private lateinit var saveUserCredentialUseCase: SaveUserCredentialUseCase

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(loginUserUseCase, saveUserCredentialUseCase)
    }

    @Test
    fun `Login should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(generateUserCredential()))
            }

            doReturn(resource).`when`(loginUserUseCase)(any())

            loginViewModel.onEvent(LoginEvent.Submit)

            val isSuccess = when (loginViewModel.loginState.value) {
                is UIState.Success -> true

                else -> false
            }

            assertEquals("Should be success", true, isSuccess)

            verify(loginUserUseCase)(any())
        }
    }

    @Test
    fun `Login should return fail`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<UserCredential>())
            }

            doReturn(resource).`when`(loginUserUseCase)(any())

            loginViewModel.onEvent(LoginEvent.Submit)

            val isSuccess = when (loginViewModel.loginState.value) {
                is UIState.Success -> true

                else -> false
            }

            assertEquals("Should be fail", false, isSuccess)

            verify(loginUserUseCase)(any())
        }
    }
}