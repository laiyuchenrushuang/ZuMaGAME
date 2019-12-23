package com.example.vendor.stgame

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Process
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.widget.Toast
import com.example.vendor.stgame.Constants.Companion.LIMIT86399
import com.example.vendor.stgame.Constants.Companion.LIMIT86400


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

        fun getYaoOneBsTime(): String {
            val time: String?
            val currentTime = TimeUtils.getCurrentTime()
            val ch = currentTime.split(":")[1]  // 分钟
            if (ch.toInt() in 47..59) {
                if (TimeUtils.String2Int(currentTime) + 1 * 60 * 60 > LIMIT86399) {  //晚上12点后怎么跳时间
                    time = TimeUtils.Int2String(TimeUtils.String2Int(currentTime) + 1 * 60 * 60 - LIMIT86400)
                } else {
                    time = TimeUtils.Int2String(TimeUtils.String2Int(currentTime) + 1 * 60 * 60)
                }

            } else {
                time = currentTime
            }

            val h = time.split(":")[0]  //
            var newTime = "$h:47:00"
            return newTime
        } //妖1 每47 min

        fun getBsTime(currentTime :String,period: Float): String {
            val time: String?
            if (TimeUtils.String2Int(currentTime) + period * 60 * 60 > LIMIT86399) {  //晚上12点后怎么跳时间
                time = TimeUtils.Int2String(TimeUtils.String2Int(currentTime) + (period * 60 * 60 - LIMIT86400).toInt())
            } else {
                time = TimeUtils.Int2String(TimeUtils.String2Int(currentTime) + (period * 60 * 60).toInt())
            }
            return time
        }
    }
}
