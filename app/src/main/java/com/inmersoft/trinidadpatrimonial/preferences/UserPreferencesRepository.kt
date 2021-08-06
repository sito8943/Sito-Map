package com.inmersoft.trinidadpatrimonial.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val USER_SEE_ONBOARDING = booleanPreferencesKey("user_see_onboarding")
    }


    fun getPrefs(): LiveData<UserPreferences> {
        return dataStore.data.map { preferences ->
            val userSeeOnboarding = preferences[PreferencesKeys.USER_SEE_ONBOARDING] ?: false
            UserPreferences(
                userSeeOnboarding
            )
        }.asLiveData()
    }

    suspend fun setUserPreferences(userPreferences: UserPreferences) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_SEE_ONBOARDING] = userPreferences.userSeeOnboarding
        }
    }

}