package com.example.theweatherapp.ui.helper

import android.content.Context
import android.content.SharedPreferences

object SharedPrefsManager {
    private const val PREFS_NAME = "MyAppPrefs"
    private const val SWITCH_STATE_KEY = "switchState"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getSwitchState(): Boolean {
        return sharedPreferences.getBoolean(SWITCH_STATE_KEY,false)
    }

    fun setSwitchState(state: Boolean) {
        sharedPreferences.edit().putBoolean(SWITCH_STATE_KEY, state).apply()
    }
}
