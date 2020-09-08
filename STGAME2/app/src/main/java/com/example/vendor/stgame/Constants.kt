package com.example.vendor.stgame

import android.app.Application

/**
 * Created by ly on 2019/12/11 13:08
 *
 * Copyright is owned by chengdu haicheng technology
 * co., LTD. The code is only for learning and sharing.
 * It is forbidden to make profits by spreading the code.
 */
class Constants {
    companion object {
        var BOSS_SPACE = "boss_space"
        var BOSS_TIME = "boss_time"
        var BOSS_PERIOD = "boss_period"
        var STATE = "state"  // 0 默认 1 更新的 2(权重最高)
        var OVER_NUM = "over_num" // 0是没超过12点 1 是超过12点
        var ENABLE = "LYC"
        var TIME = "TIME"

        var YAO_SHAN = "妖山一层"
        var SEN_LIN = "森林"
        var DI_XIA = "地下"
        var HAI_DI = "海底"
        var BS = "BOSS"

        var LIMIT86399 = 86399
        var LIMIT86400 = 86400
    }
}