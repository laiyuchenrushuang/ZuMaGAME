package com.example.vendor.stgame

import com.example.vendor.stgame.Constants.Companion.LIMIT86399
import com.example.vendor.stgame.Constants.Companion.LIMIT86400
import java.util.*

/**
24小时机制
 */
class TimeUtils {
    companion object {
        /**
         * hh:mm:ss   10:42:17
         */
        fun getCurrentTime(): String {
            val calendar = Calendar.getInstance()
            val h = changeTo01(calendar.get(Calendar.HOUR_OF_DAY))
            val m = changeTo01(calendar.get(Calendar.MINUTE))
            val s = changeTo01(calendar.get(Calendar.SECOND))
            return "$h:$m:$s"
        }

        /**
         * String2Int  38537  MAX 24:00:00  86400
         */
        fun String2Int(second: String): Int {
            val h = changeTo1(second.split(":")[0].toInt())
            val m = changeTo1(second.split(":")[1].toInt())
            val s = changeTo1(second.split(":")[2].toInt())
            return h * 60 * 60 + m * 60 + s
        }

        /**
         * Int2String  10:42:17
         */
        fun Int2String(second: Int): String {
            var s1 = second
            if (s1 > LIMIT86399) { //当零点的时候，周期计算
                s1 %= LIMIT86400
            }
            val h = changeTo01((s1 / 60) / 60)
            val m = changeTo01((s1 - h.toInt() * 60 * 60) / 60)
            val s = changeTo01(s1 - h.toInt() * 60 * 60 - m.toInt() * 60)
            return "$h:$m:$s"
        }

        /**
         * 把1->01
         */
        fun changeTo01(s: Int): String {
            return String.format("%02d", s)
        }

        /**
         * 把01->1
         */
        fun changeTo1(s: Int): Int {
            return Integer.parseInt(s.toString())
        }

        /**
         * 获取Day  当月
         */
        fun getDayOfMonth():Int{
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.DAY_OF_MONTH)
        }
    }
}