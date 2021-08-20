package com.inmersoft.trinidadpatrimonial

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.inmersoft.trinidadpatrimonial.preferences.UserPreferencesRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    companion object {
        private const val USER_PREFERENCES_NAME = "settings"
    }


    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    val userPrefsRepo by lazy { UserPreferencesRepository(dataStore) }

}

