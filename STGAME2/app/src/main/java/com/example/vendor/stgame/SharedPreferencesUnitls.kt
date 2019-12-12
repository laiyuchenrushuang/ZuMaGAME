package com.example.vendor.stgame

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.R.id.edit
import android.content.SharedPreferences


class SharedPreferencesUnitls {

    companion object {
        private val SHARE_TABLE = "LAIYU"

        fun setParam(context: Context, key: String, value: String) {
            val editor = context.getSharedPreferences(SHARE_TABLE, MODE_PRIVATE).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun getParam(context: Context, key: String): String {
            val enity = context.getSharedPreferences(SHARE_TABLE, MODE_PRIVATE)
            return enity.getString(key, "2019-12-12 18:00:00")
        }
    }
}
