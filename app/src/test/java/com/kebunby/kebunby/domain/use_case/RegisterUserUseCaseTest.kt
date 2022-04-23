package com.kebunby.kebunby.domain.use_case

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.data.repository.UserRepository
import com.kebunby.kebunby.domain.use_case.user.RegisterUserUseCase
import com.kebunby.kebunby.util.TestCoroutineRule
import com.kebunby.kebunby.util.generateRegisterRequest
import com.kebunby.kebunby.util.generateUserCredential
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

    // Dependency
    @Mock
    private lateinit var userRepository: UserRepository

    // SUT
    private lateinit var registerUserUseCase: RegisterUserUseCase

    @Before
    fun setUp() {
        registerUserUseCase = RegisterUserUseCase(userRepository)
    }

    @Test
    fun login_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(
                    Resource.Success<UserCredential>(
                        generateUserCredential()
                    )
                )
            }

            doReturn(resource).`when`(userRepository).register(any())

            // Act
            val actResource = registerUserUseCase.invoke(generateRegisterRequest()).first()
            var userCredential = UserCredential()

            when (actResource) {
                is Resource.Success -> {
                    userCredential = actResource.data!!
                }

                is Resource.Error -> {}
            }

            // Assert
            Assert.assertEquals("Username should be 'george'", "george", userCredential.username)
            Assert.assertEquals("Access token should be 'abc'", "abc", userCredential.accessToken)

            // Verify
            verify(userRepository).register(any())
        }
    }

    @Test
    fun login_ShouldReturnError() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Error<UserCredential>())
            }

            doReturn(resource).`when`(userRepository).register(any())

            // Act
            val actResource = registerUserUseCase.invoke(generateRegisterRequest()).first()

            // Assert
            Assert.assertEquals(
                "Resource should be error",
                Resource.Error<UserCredential>(),
                actResource
            )

            // Verify
            verify(userRepository).register(any())
        }
    }
}