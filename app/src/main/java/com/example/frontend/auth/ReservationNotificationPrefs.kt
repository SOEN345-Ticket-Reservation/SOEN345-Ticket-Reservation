package com.example.frontend.auth

import android.content.Context
import android.content.SharedPreferences

object ReservationNotificationPrefs {

    private const val PREFS_NAME = "reservation_notify"
    private const val KEY_PREFIX = "notify_"

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    fun getNotify(reservationId: Long): Boolean {
        return prefs?.getBoolean(KEY_PREFIX + reservationId, false) ?: false
    }

    fun setNotify(reservationId: Long, enabled: Boolean) {
        prefs?.edit()?.putBoolean(KEY_PREFIX + reservationId, enabled)?.apply()
    }
}
