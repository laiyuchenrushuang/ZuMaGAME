package com.example.vendor.stgame;

import android.os.SystemClock;

/**
 * Created by ly on 2020/9/8 11:33
 */
public class Javatest {
    public  static void main(String[] args){

       print(Utils.Companion.longToStringData(System.currentTimeMillis()));
       print(Utils.Companion.longToStringData(SystemClock.currentThreadTimeMillis()));
       print(Utils.Companion.longToStringData(SystemClock.uptimeMillis()));
       print(Utils.Companion.longToStringData(SystemClock.elapsedRealtime()));
       print(Utils.Companion.longToStringData(SystemClock.elapsedRealtimeNanos()));
    }

    public static void print(Object obj){
        System.out.println(obj);
    }
}
