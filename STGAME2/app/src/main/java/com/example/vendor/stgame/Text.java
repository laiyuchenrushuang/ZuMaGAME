package com.example.vendor.stgame;

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
        showLog(time);
    }

    public static void showLog(String msg){
        System.out.println(msg);
    }
}
