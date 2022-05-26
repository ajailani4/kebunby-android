package com.kebunby.kebunby.domain.use_case

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.data.repository.UserRepository
import com.kebunby.kebunby.domain.use_case.user.LoginUserUseCase
import com.kebunby.kebunby.util.TestCoroutineRule
import com.kebunby.kebunby.util.generateLoginRequest
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

            val actResource = loginUserUseCase.invoke(generateLoginRequest()).first()
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

            val actResource = loginUserUseCase.invoke(generateLoginRequest()).first()

            assertEquals("Resource should be error", Resource.Error<UserCredential>(), actResource)

            verify(userRepository).login(any())
        }
    }
}