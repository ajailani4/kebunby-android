package com.kebunby.kebunby.viewmodel

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.User
import com.kebunby.kebunby.domain.use_case.user.GetUserProfileUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.kebunby.ui.common.UIState
import com.kebunby.kebunby.ui.feature.profile.ProfileViewModel
import com.kebunby.kebunby.util.TestCoroutineRule
import com.kebunby.kebunby.util.generateUser
import com.kebunby.kebunby.util.generateUserCredential
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*

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
class ProfileViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    // Dependency
    @Mock
    private lateinit var getUserCredentialUseCase: GetUserCredentialUseCase

    @Mock
    private lateinit var getUserProfileUseCase: GetUserProfileUseCase

    // SUT
    private lateinit var profileViewModel: ProfileViewModel

    @Test
    fun getProfile_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Success(generateUser()))
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(resource).`when`(getUserProfileUseCase).invoke(anyString())

            // Act
            profileViewModel = ProfileViewModel(
                getUserCredentialUseCase,
                getUserProfileUseCase
            )

            val userProfile = when (val userProfileState = profileViewModel.userProfileState) {
                is UIState.Success -> userProfileState.data
                else -> null
            }

            // Assert
            assertNotNull(userProfile)
            assertEquals("Username should be 'george'", "george", userProfile?.username)
            assertEquals("Email should be 'george@email.com'", "george@email.com", userProfile?.email)

            // Verify
            verify(getUserCredentialUseCase).invoke()
            verify(getUserProfileUseCase).invoke(anyString())
        }
    }

    @Test
    fun getProfile_ShouldReturnFail() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Error<User>())
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(resource).`when`(getUserProfileUseCase).invoke(anyString())

            // Act
            profileViewModel = ProfileViewModel(
                getUserCredentialUseCase,
                getUserProfileUseCase
            )

            val isSuccess = when (val userProfileState = profileViewModel.userProfileState) {
                is UIState.Success -> true

                is UIState.Fail -> false

                is UIState.Error -> false

                else -> null
            }

            // Assert
            assertEquals("Should be fail", false, isSuccess)

            // Verify
            verify(getUserCredentialUseCase).invoke()
            verify(getUserProfileUseCase).invoke(anyString())
        }
    }
}