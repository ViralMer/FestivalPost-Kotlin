package com.app.festivalpost.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(val context: Context) {
    private val PREFS_NAME = "com.app.festivalpost.session"
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


    fun setStringValue(KEY_NAME: String, value: String): String {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, value)
        editor.apply()
        return value
    }

    fun setBooleanValue(KEY_NAME: String, value: Boolean): Boolean {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, value)
        editor.apply()
        return value
    }

    fun setIntValue(KEY_NAME: String, value: Int): Int {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_NAME, value)
        editor.apply()
        return value
    }

    fun getValueString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, null)
    }

    fun getBooleanValue(KEY_NAME: String): Boolean? {
        return sharedPref.getBoolean(KEY_NAME, false)
    }

    fun getValueInt(KEY_NAME: String): Int? {
        return sharedPref.getInt(KEY_NAME, 0)
    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(KEY_NAME)
        editor.apply()
    }
}
