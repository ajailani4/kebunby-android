package com.kebunby.app.viewmodel

import com.kebunby.app.data.Resource
import com.kebunby.app.data.model.User
import com.kebunby.app.domain.use_case.user.GetUserProfileUseCase
import com.kebunby.app.domain.use_case.user_credential.DeleteUserCredentialUseCase
import com.kebunby.app.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.app.ui.common.UIState
import com.kebunby.app.ui.feature.profile.ProfileViewModel
import com.kebunby.app.util.TestCoroutineRule
import com.kebunby.app.util.generateUser
import com.kebunby.app.util.generateUserCredential
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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

    @Mock
    private lateinit var getUserCredentialUseCase: GetUserCredentialUseCase

    @Mock
    private lateinit var getUserProfileUseCase: GetUserProfileUseCase

    @Mock
    private lateinit var deleteUserCredentialUseCase: DeleteUserCredentialUseCase

    private lateinit var profileViewModel: ProfileViewModel

    @Test
    fun `Get profile should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(generateUser()))
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(getUserProfileUseCase)(anyString())

            profileViewModel = ProfileViewModel(
                getUserCredentialUseCase,
                getUserProfileUseCase,
                deleteUserCredentialUseCase
            )

            val userProfile =
                when (val userProfileState = profileViewModel.userProfileState.value) {
                    is UIState.Success -> userProfileState.data
                    else -> null
                }

            assertNotNull(userProfile)
            assertEquals("Username should be 'george'", "george", userProfile?.username)
            assertEquals(
                "Email should be 'george@email.com'",
                "george@email.com",
                userProfile?.email
            )

            verify(getUserCredentialUseCase)()
            verify(getUserProfileUseCase)(anyString())
        }
    }

    @Test
    fun `Get profile should return fail`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<User>())
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(getUserProfileUseCase)(anyString())

            profileViewModel = ProfileViewModel(
                getUserCredentialUseCase,
                getUserProfileUseCase,
                deleteUserCredentialUseCase
            )

            val isSuccess = when (val userProfileState = profileViewModel.userProfileState.value) {
                is UIState.Success -> true

                is UIState.Fail -> false

                is UIState.Error -> false

                else -> null
            }

            assertEquals("Should be fail", false, isSuccess)

            verify(getUserCredentialUseCase)()
            verify(getUserProfileUseCase)(anyString())
        }
    }
}