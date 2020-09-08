package com.example.vendor.stgame

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.R.id.edit
import android.content.SharedPreferences


class SharedPreferencesUnitls {

    companion object {
        private val SHARE_TABLE = "LAIYU"
        private val TIME = "TIME"

        fun setParam(context: Context, key: String, value: String) {
            val editor = context.getSharedPreferences(SHARE_TABLE, MODE_PRIVATE).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun getParam(context: Context, key: String): String {
            val enity = context.getSharedPreferences(SHARE_TABLE, MODE_PRIVATE)
            return enity.getString(key, "2020-10-08 11:26:30")
        }

        fun setTime(context: Context, key: String, value: String) {
            val editor = context.getSharedPreferences(SHARE_TABLE, MODE_PRIVATE).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun getTime(context: Context, key: String): String {
            val enity = context.getSharedPreferences(SHARE_TABLE, MODE_PRIVATE)
            return enity.getString(key, "2020-09-08 11:26:30")
        }
    }
}
