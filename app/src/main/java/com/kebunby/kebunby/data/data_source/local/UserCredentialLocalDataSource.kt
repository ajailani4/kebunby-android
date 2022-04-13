package com.kebunby.kebunby.data.data_source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kebunby.kebunby.data.model.UserCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserCredentialLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userCredential")
        private val USERNAME = stringPreferencesKey("username")
        private val ACCESS_TOKEN = stringPreferencesKey("accessToken")
    }

    suspend fun saveUserCredential(userCredential: UserCredential) {
        context.dataStore.edit {
            it[USERNAME] = userCredential.username
            it[ACCESS_TOKEN] = userCredential.accessToken
        }
    }

    fun getUserCredential() =
        context.dataStore.data
            .map {
                UserCredential(
                    username = it[USERNAME] ?: "",
                    accessToken = it[ACCESS_TOKEN] ?: ""
                )
            }

    suspend fun deleteUserCredential() {
        context.dataStore.edit {
            it.remove(USERNAME)
            it.remove(ACCESS_TOKEN)
        }
    }
}