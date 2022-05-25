package com.kebunby.kebunby.viewmodel

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.domain.use_case.user.LoginUserUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.SaveUserCredentialUseCase
import com.kebunby.kebunby.ui.common.UIState
import com.kebunby.kebunby.ui.feature.login.LoginEvent
import com.kebunby.kebunby.ui.feature.login.LoginViewModel
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
    fun login_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(generateUserCredential()))
            }

            doReturn(resource).`when`(loginUserUseCase).invoke(any())

            loginViewModel.onEvent(LoginEvent.Submit)

            val isSuccess = when (loginViewModel.loginState) {
                is UIState.Success -> true

                is UIState.Fail -> false

                is UIState.Error -> false

                else -> false
            }

            assertEquals("Should be success", true, isSuccess)

            verify(loginUserUseCase).invoke(any())
        }
    }

    @Test
    fun login_ShouldReturnFail() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<UserCredential>())
            }

            doReturn(resource).`when`(loginUserUseCase).invoke(any())

            loginViewModel.onEvent(LoginEvent.Submit)

            val isSuccess = when (loginViewModel.loginState) {
                is UIState.Success -> true

                is UIState.Fail -> false

                is UIState.Error -> false

                else -> false
            }

            assertEquals("Should be fail", false, isSuccess)

            verify(loginUserUseCase).invoke(any())
        }
    }
}