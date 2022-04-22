package com.kebunby.kebunby.domain.use_case

import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.data.repository.UserCredentialRepository
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.kebunby.util.TestCoroutineRule
import com.kebunby.kebunby.util.generateUserCredential
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
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
class GetUserCredentialUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    // Dependency
    @Mock
    private lateinit var userCredentialRepository: UserCredentialRepository

    // SUT
    private lateinit var getUserCredentialUseCase: GetUserCredentialUseCase

    @Before
    fun setUp() {
        getUserCredentialUseCase = GetUserCredentialUseCase(userCredentialRepository)
    }

    @Test
    fun getUserCredential_ShouldReturnUserCredentialIsNotEmpty() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val userCredential = flow {
                emit(generateUserCredential())
            }

            doReturn(userCredential).`when`(userCredentialRepository).getUserCredential()

            // Act
            val actUserCredential = getUserCredentialUseCase.invoke().first()

            // Assert
            assertEquals("Username should be 'george'", "george", actUserCredential.username)
            assertEquals("Access token should be 'abc'", "abc", actUserCredential.accessToken)

            // Verify
            verify(userCredentialRepository).getUserCredential()
        }
    }

    @Test
    fun getUserCredential_ShouldReturnUserCredentialIsEmpty() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val userCredential = flow {
                emit(
                    UserCredential(
                        username = "",
                        accessToken = ""
                    )
                )
            }

            doReturn(userCredential).`when`(userCredentialRepository).getUserCredential()

            // Act
            val actUserCredential = getUserCredentialUseCase.invoke().first()

            // Assert
            assertEquals("Username should be empty", "", actUserCredential.username)
            assertEquals("Access token should be empty", "", actUserCredential.accessToken)

            // Verify
            verify(userCredentialRepository).getUserCredential()
        }
    }
}