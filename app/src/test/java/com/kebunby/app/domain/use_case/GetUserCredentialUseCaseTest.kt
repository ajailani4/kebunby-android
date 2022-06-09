package com.kebunby.app.domain.use_case

import com.kebunby.app.data.model.UserCredential
import com.kebunby.app.data.repository.UserCredentialRepository
import com.kebunby.app.domain.use_case.user_credential.GetUserCredentialUseCase
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
class GetUserCredentialUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var userCredentialRepository: UserCredentialRepository

    private lateinit var getUserCredentialUseCase: GetUserCredentialUseCase

    @Before
    fun setUp() {
        getUserCredentialUseCase = GetUserCredentialUseCase(userCredentialRepository)
    }

    @Test
    fun `Get user credential should return user credential is not empty`() {
        testCoroutineRule.runTest {
            val userCredential = flow {
                emit(generateUserCredential())
            }

            doReturn(userCredential).`when`(userCredentialRepository).getUserCredential()

            val actUserCredential = getUserCredentialUseCase().first()

            assertEquals("Username should be 'george'", "george", actUserCredential.username)
            assertEquals("Access token should be 'abc'", "abc", actUserCredential.accessToken)

            verify(userCredentialRepository).getUserCredential()
        }
    }

    @Test
    fun `Get user credential should return user credential is empty`() {
        testCoroutineRule.runTest {
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
            val actUserCredential = getUserCredentialUseCase().first()

            // Assert
            assertEquals("Username should be empty", "", actUserCredential.username)
            assertEquals("Access token should be empty", "", actUserCredential.accessToken)

            // Verify
            verify(userCredentialRepository).getUserCredential()
        }
    }
}