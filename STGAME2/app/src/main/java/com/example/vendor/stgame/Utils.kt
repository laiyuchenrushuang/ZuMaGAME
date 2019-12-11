package com.example.vendor.stgame

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by ly on 2019/12/11 13:42
 *
 *
 * Copyright is owned by chengdu haicheng technology
 * co., LTD. The code is only for learning and sharing.
 * It is forbidden to make profits by spreading the code.
 */
class Utils {
    companion object {

        /**
         * 获取屏幕宽
         * @param context
         * @return
         */
        fun getScreenWidth(context: Context): Int {
            val outMetrics = DisplayMetrics()
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getRealMetrics(outMetrics)
            return outMetrics.widthPixels
        }

        /**
         * 获取屏幕高
         * @param context
         * @return
         */
        fun getScreenHeight(context: Context): Int {
            val outMetrics = DisplayMetrics()
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getRealMetrics(outMetrics)
            return outMetrics.heightPixels
        }

        /**
         * 时间戳转换为字符串类型
         *
         * @return
         */
        fun longToStringData(date: Long): String? {
            if (0L == date) {
                return null
            }
            try {
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.CHINA) // "yyyy-MM-dd HH:mm:ss"
                return sdf.format(Date(date))
            } catch (e: Exception) {
                return null
            }

        }

        /**
         * 字符串类型转为时间戳转换
         *
         * @return
         */
        fun dateToStamp(s: String): Long {
            if ("" == s) {
                return 0
            }
            try {
                val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.CHINA)
                val date = simpleDateFormat.parse(s)
                return date.time
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return 0
        }
    }
}
