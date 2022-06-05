package com.kebunby.app.domain.use_case

import com.kebunby.app.data.Resource
import com.kebunby.app.data.model.UserCredential
import com.kebunby.app.data.repository.UserRepository
import com.kebunby.app.domain.use_case.user.RegisterUserUseCase
import com.kebunby.app.util.TestCoroutineRule
import com.kebunby.app.util.generateRegisterRequest
import com.kebunby.app.util.generateUserCredential
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.junit.Assert
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
class RegisterUserUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var registerUserUseCase: RegisterUserUseCase

    @Before
    fun setUp() {
        registerUserUseCase = RegisterUserUseCase(userRepository)
    }

    @Test
    fun `Register should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(
                    Resource.Success<UserCredential>(
                        generateUserCredential()
                    )
                )
            }

            doReturn(resource).`when`(userRepository).register(any())

            val actResource = registerUserUseCase(generateRegisterRequest()).first()
            var userCredential = UserCredential()

            when (actResource) {
                is Resource.Success -> {
                    userCredential = actResource.data!!
                }

                is Resource.Error -> {}
            }

            Assert.assertEquals("Username should be 'george'", "george", userCredential.username)
            Assert.assertEquals("Access token should be 'abc'", "abc", userCredential.accessToken)

            verify(userRepository).register(any())
        }
    }

    @Test
    fun `Register should return error`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<UserCredential>())
            }

            doReturn(resource).`when`(userRepository).register(any())

            val actResource = registerUserUseCase(generateRegisterRequest()).first()

            Assert.assertEquals(
                "Resource should be error",
                Resource.Error<UserCredential>(),
                actResource
            )

            verify(userRepository).register(any())
        }
    }
}