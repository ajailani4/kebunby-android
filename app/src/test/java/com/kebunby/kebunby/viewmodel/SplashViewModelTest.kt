package com.kebunby.kebunby.viewmodel

import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.kebunby.ui.feature.splash.SplashViewModel
import com.kebunby.kebunby.util.TestCoroutineRule
import com.kebunby.kebunby.util.generateUserCredential
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var getUserCredentialUseCase: GetUserCredentialUseCase

    private lateinit var splashViewModel: SplashViewModel

    @Before
    fun setUp() {
        splashViewModel = SplashViewModel(getUserCredentialUseCase)
    }

    @Test
    fun `Get user credential should return user credential is not empty`() {
        testCoroutineRule.runBlockingTest {
            val userCredential = flow {
                emit(generateUserCredential())
            }

            doReturn(userCredential).`when`(getUserCredentialUseCase).invoke()

            val actUserCredential = splashViewModel.getUserCredential().first()

            assertEquals("Username should be 'george'", "george", actUserCredential.username)
            assertEquals("Access token should be 'abc'", "abc", actUserCredential.accessToken)

            verify(getUserCredentialUseCase).invoke()
        }
    }

    @Test
    fun `Get user credential should return user credential is empty`() {
        testCoroutineRule.runBlockingTest {
            val userCredential = flow {
                emit(
                    UserCredential(
                        username = "",
                        accessToken = ""
                    )
                )
            }

            doReturn(userCredential).`when`(getUserCredentialUseCase).invoke()

            val actUserCredential = splashViewModel.getUserCredential().first()

            assertEquals("Username should be empty", "", actUserCredential.username)
            assertEquals("Access token should be empty", "", actUserCredential.accessToken)

            verify(getUserCredentialUseCase).invoke()
        }
    }
}