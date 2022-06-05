package com.kebunby.app.domain.use_case

import com.kebunby.app.data.Resource
import com.kebunby.app.data.model.User
import com.kebunby.app.data.repository.UserRepository
import com.kebunby.app.domain.use_case.user.GetUserProfileUseCase
import com.kebunby.app.util.TestCoroutineRule
import com.kebunby.app.util.generateUser
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

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var getUserProfileUseCase: GetUserProfileUseCase

    @Before
    fun setUp() {
        getUserProfileUseCase = GetUserProfileUseCase(userRepository)
    }

    @Test
    fun `Get user profile should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(generateUser()))
            }

            doReturn(resource).`when`(userRepository).getUserProfile(anyString())

            val actualResource = getUserProfileUseCase(anyString()).first()

            assertEquals(
                "Resource should be success", Resource.Success(generateUser()), actualResource
            )

            verify(userRepository).getUserProfile(anyString())
        }
    }

    @Test
    fun `Get user profile should returns success error`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<User>())
            }

            doReturn(resource).`when`(userRepository).getUserProfile(anyString())

            val actualResource = getUserProfileUseCase(anyString()).first()

            assertEquals(
                "Resource should be error", Resource.Error<User>(), actualResource
            )

            verify(userRepository).getUserProfile(anyString())
        }
    }
}