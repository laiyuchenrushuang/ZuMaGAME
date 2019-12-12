package com.example.vendor.stgame

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Process
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.DialogInterface
import android.graphics.Color
import android.widget.TextView
import android.widget.Toast
import java.lang.reflect.AccessibleObject.setAccessible
import java.lang.reflect.AccessibleObject.setAccessible


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
         * 时间戳转换为字符串类型
         *
         * @return
         */
        fun long2StringData(date: Long): String? {
            if (0L == date) {
                return null
            }
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA) // "yyyy-MM-dd HH:mm:ss"
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

        /**
         * 字符串类型转为时间戳转换
         *
         * @return
         */
        fun date2Stamp(s: String): Long {
            if ("" == s) {
                return 0
            }
            try {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                val date = simpleDateFormat.parse(s)
                return date.time
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return 0
        }

        fun getCurrentTimeL(): Long {
            val sdf = SimpleDateFormat("HH:mm:ss")
            return dateToStamp((sdf.format(Date())))
        }

        //做一个时间限定
        fun checkEnableUse(activity: Activity) {
            var limitTime = SharedPreferencesUnitls.getParam(activity, Constants.ENABLE)
            var currentTime = System.currentTimeMillis()
            if (currentTime - date2Stamp(limitTime) > 0) {
                val builder = AlertDialog.Builder(activity)
                builder.setMessage("当前app使用期限已到\n\n截止时间为:$limitTime")
                builder.setTitle("提示")
                builder.setPositiveButton("确定") { dialog, _ ->
                    dialog.dismiss()
                    // 这里是确定是要做的操作

                    activity.finish()
                    Process.killProcess(Process.myPid())
                }
                var dialog = builder.create()
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()

            } else if (date2Stamp(limitTime) - currentTime < 3 * 24 * 60 * 60 * 1000L) {
                Toast.makeText(activity, "请注意截止时间为:$limitTime", Toast.LENGTH_SHORT).show()
            }
        }

        //妖1 每47 min

        fun getYaoOneBsTime(): Long {
            var time:String?
            var currentTime = longToStringData(System.currentTimeMillis())
            var ch = currentTime!!.split(":")[1]  // 分钟
            if (ch.toInt() in 47..59) {
                time = longToStringData(System.currentTimeMillis())
            }else{
                time = longToStringData(System.currentTimeMillis() - 1 * 60 * 60 * 1000L)
            }

            var h = time!!.split(":")[0]  //
            var newTime = "$h:47:00"
            return dateToStamp(newTime)
        }
    }
}
