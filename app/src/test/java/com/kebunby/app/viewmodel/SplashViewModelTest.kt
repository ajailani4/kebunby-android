package com.kebunby.app.viewmodel

import com.kebunby.app.data.model.UserCredential
import com.kebunby.app.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.app.ui.feature.splash.SplashViewModel
import com.kebunby.app.util.TestCoroutineRule
import com.kebunby.app.util.generateUserCredential
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
        testCoroutineRule.runTest {
            val userCredential = flow {
                emit(generateUserCredential())
            }

            doReturn(userCredential).`when`(getUserCredentialUseCase)()

            val actUserCredential = splashViewModel.getUserCredential().first()

            assertEquals("Username should be 'george'", "george", actUserCredential.username)
            assertEquals("Access token should be 'abc'", "abc", actUserCredential.accessToken)

            verify(getUserCredentialUseCase)()
        }
    }

    @Test
    fun `Get user credential should return user credential is empty`() {
        testCoroutineRule.runTest {
            val userCredential = flow {
                emit(
                    UserCredential(
                        username = "",
                        accessToken = ""
                    )
                )
            }

            doReturn(userCredential).`when`(getUserCredentialUseCase)()

            val actUserCredential = splashViewModel.getUserCredential().first()

            assertEquals("Username should be empty", "", actUserCredential.username)
            assertEquals("Access token should be empty", "", actUserCredential.accessToken)

            verify(getUserCredentialUseCase)()
        }
    }
}