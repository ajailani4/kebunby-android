package com.kebunby.kebunby.domain.use_case

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.User
import com.kebunby.kebunby.data.repository.UserRepository
import com.kebunby.kebunby.domain.use_case.user.GetUserProfileUseCase
import com.kebunby.kebunby.util.TestCoroutineRule
import com.kebunby.kebunby.util.generateUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetUserProfileUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    // Dependency
    @Mock
    private lateinit var userRepository: UserRepository

    // SUT
    private lateinit var getUserProfileUseCase: GetUserProfileUseCase

    @Before
    fun setUp() {
        getUserProfileUseCase = GetUserProfileUseCase(userRepository)
    }

    @Test
    fun getUserProfile_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Success(generateUser()))
            }

            doReturn(resource).`when`(userRepository).getUserProfile(anyString())

            // Act
            val actualResource = getUserProfileUseCase.invoke(anyString()).first()

            // Assert
            assertEquals(
                "Resource should be success", Resource.Success(generateUser()), actualResource
            )

            // Verify
            verify(userRepository).getUserProfile(anyString())
        }
    }

    @Test
    fun getUserProfile_ShouldReturnSuccessError() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Error<User>())
            }

            doReturn(resource).`when`(userRepository).getUserProfile(anyString())

            // Act
            val actualResource = getUserProfileUseCase.invoke(anyString()).first()

            // Assert
            assertEquals(
                "Resource should be error", Resource.Error<User>(), actualResource
            )

            // Verify
            verify(userRepository).getUserProfile(anyString())
        }
    }
}