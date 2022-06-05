package com.kebunby.app.domain.use_case

import com.kebunby.app.data.Resource
import com.kebunby.app.data.model.UserCredential
import com.kebunby.app.data.repository.UserRepository
import com.kebunby.app.domain.use_case.user.LoginUserUseCase
import com.kebunby.app.util.TestCoroutineRule
import com.kebunby.app.util.generateLoginRequest
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
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginUserUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var loginUserUseCase: LoginUserUseCase

    @Before
    fun setUp() {
        loginUserUseCase = LoginUserUseCase(userRepository)
    }

    @Test
    fun `Login should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(
                    Resource.Success<UserCredential>(
                        generateUserCredential()
                    )
                )
            }

            doReturn(resource).`when`(userRepository).login(any())

            val actResource = loginUserUseCase(generateLoginRequest()).first()
            var userCredential = UserCredential()

            when (actResource) {
                is Resource.Success -> {
                    userCredential = actResource.data!!
                }

                is Resource.Error -> {}
            }

            assertEquals("Username should be 'george'", "george", userCredential.username)
            assertEquals("Access token should be 'abc'", "abc", userCredential.accessToken)

            // Verify
            verify(userRepository).login(any())
        }
    }

    @Test
    fun `Login should return error`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<UserCredential>())
            }

            doReturn(resource).`when`(userRepository).login(any())

            val actResource = loginUserUseCase(generateLoginRequest()).first()

            assertEquals("Resource should be error", Resource.Error<UserCredential>(), actResource)

            verify(userRepository).login(any())
        }
    }
}