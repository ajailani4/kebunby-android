package com.kebunby.kebunby.domain.use_case

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.data.repository.impl.UserRepositoryImpl
import com.kebunby.kebunby.domain.use_case.user.LoginUserUseCase
import com.kebunby.kebunby.util.TestCoroutineRule
import com.kebunby.kebunby.util.generateLoginRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
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

    // Dependency
    @Mock
    private lateinit var userRepositoryImpl: UserRepositoryImpl

    // SUT
    private lateinit var loginUserUseCase: LoginUserUseCase

    @Before
    fun setUp() {
        loginUserUseCase = LoginUserUseCase(userRepositoryImpl)
    }

    @Test
    fun login_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Success<UserCredential>())
            }

            doReturn(resource).`when`(userRepositoryImpl).login(any())

            // Act
            val actResource = loginUserUseCase.invoke(generateLoginRequest()).first()

            // Assert
            assertEquals("Resource should be success", Resource.Success<UserCredential>(), actResource)

            // Verify
            verify(userRepositoryImpl).login(any())
        }
    }

    @Test
    fun login_ShouldReturnError() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Error<UserCredential>())
            }

            doReturn(resource).`when`(userRepositoryImpl).login(any())

            // Act
            val actResource = loginUserUseCase.invoke(generateLoginRequest()).first()

            // Assert
            assertEquals("Resource should be error", Resource.Error<UserCredential>(), actResource)

            // Verify
            verify(userRepositoryImpl).login(any())
        }
    }
}