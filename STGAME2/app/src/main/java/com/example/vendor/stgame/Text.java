package com.example.vendor.stgame;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by ly on 2019/12/12 13:29
 * <p>
 * Copyright is owned by chengdu haicheng technology
 * co., LTD. The code is only for learning and sharing.
 * It is forbidden to make profits by spreading the code.
 */
public class Text {
    public static void main(String args[]) {
        String time = Utils.Companion.longToStringData(56820000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        HashMap<String, String> map = new HashMap<>();
        showLog(getCurrentHourMinute());
    }

    public static void showLog(Object msg) {
        System.out.println(msg);
    }

    private static int getCurrentHourMinute() {
        Calendar cal = Calendar.getInstance();
        cal.get(Calendar.DAY_OF_MONTH);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

}
