package com.dy.memorygod.manager

import android.content.Context

object PreferenceManager {

    private const val PREFERENCE_NAME = "MemoryGod"
    const val PREFERENCE_DEFAULT_VALUE = ""

    fun set(context: Context, key: String, value: String) {
        val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val edit = preference.edit()

        edit?.putString(key, value)?.apply()
    }

    fun get(context: Context, key: String): String {
        val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        return preference.getString(key, PREFERENCE_DEFAULT_VALUE)!!
    }

}