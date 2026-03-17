package com.example.frontend.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object TokenStorage {

    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_USER_ID = "user_id"

    private var prefs: android.content.SharedPreferences? = null

    fun init(context: Context) {
        if (prefs != null) return
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        prefs = EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getToken(): String? = prefs?.getString(KEY_TOKEN, null)

    fun setToken(token: String) {
        prefs?.edit()?.putString(KEY_TOKEN, token)?.apply()
    }

    fun clearToken() {
        prefs?.edit()?.remove(KEY_TOKEN)?.remove(KEY_USER_ID)?.apply()
    }

    fun getUserId(): Long? {
        val raw = prefs?.getString(KEY_USER_ID, null) ?: return null
        return raw.toLongOrNull()
    }

    fun setUserId(id: Long) {
        prefs?.edit()?.putString(KEY_USER_ID, id.toString())?.apply()
    }
}
