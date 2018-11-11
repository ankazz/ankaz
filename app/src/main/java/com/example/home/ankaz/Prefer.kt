package com.example.home.ankaz

import android.content.Context
import android.preference.PreferenceManager

class Prefer(context: Context){
    companion object {
        val DEVELOP_MODE = false
        private val LOGIN_TOKEN = "data.source.prefs.LOGIN_TOKEN"
        private val PASSWORD_TOKEN = "data.source.prefs.PASSWORD_TOKEN"
        private val REMEMBER_TOKEN = "data.source.prefs.REMEMBER_TOKEN"
    }
    private val preferences = PreferenceManager   .getDefaultSharedPreferences(context)
    // save device token
    var loginToken = preferences.getString(LOGIN_TOKEN, "")
        set(value) = preferences.edit().putString(LOGIN_TOKEN,     value).apply()

    var passwordToken = preferences.getString(PASSWORD_TOKEN, "")
        set(value) = preferences.edit().putString(PASSWORD_TOKEN,     value).apply()

    var rememberToken = preferences.getBoolean(REMEMBER_TOKEN, false)
        set(value) = preferences.edit().putBoolean(REMEMBER_TOKEN,     value).apply()
}