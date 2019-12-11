package com.example.vendor.stgame

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type
import java.util.ArrayList

/**
 * Created by seatrend on 2018/8/21.
 */

class GsonUtils {
    companion object {
        private val gson = Gson()

        @Throws(JsonSyntaxException::class)
        fun <T> gson(json: String, clas: Class<T>): T {
            return gson.fromJson(json, clas)
        }

        fun toJson(o: Any): String {
            return gson.toJson(o)
        }

        fun <T> jsonToArrayList(json: String, clazz: Class<T>): ArrayList<T> {
            val type = object : TypeToken<ArrayList<JsonObject>>() {

            }.type
            val jsonObjects = Gson().fromJson<ArrayList<JsonObject>>(json, type)

            val arrayList = ArrayList<T>()
            for (jsonObject in jsonObjects) {
                arrayList.add(Gson().fromJson(jsonObject, clazz))
            }
            return arrayList
        }
    }
}
