package com.shuham.ganga.data.local

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class TokenManager(private val settings: Settings) {

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_IS_FIRST_RUN = "is_first_run"
    }

    fun saveAuthData(accessToken: String, refreshToken: String, userName: String) {
        settings[KEY_ACCESS_TOKEN] = accessToken
        settings[KEY_REFRESH_TOKEN] = refreshToken
        settings[KEY_USER_NAME] = userName
    }

    fun getAccessToken(): String? = settings.getStringOrNull(KEY_ACCESS_TOKEN)
    fun getRefreshToken(): String? = settings.getStringOrNull(KEY_REFRESH_TOKEN)
    fun getUserName(): String? = settings.getStringOrNull(KEY_USER_NAME)

    fun clearAuth() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
        settings.remove(KEY_USER_NAME)
    }

    fun isFirstRun(): Boolean {
        // Defaults to true if key doesn't exist
        return settings.getBoolean(KEY_IS_FIRST_RUN, true)
    }

    fun setFirstRunCompleted() {
        settings[KEY_IS_FIRST_RUN] = false
    }
}