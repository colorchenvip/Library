package com.colorchen.tool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Gson 工具类
 * Author ChenQ on 2017-11-15
 * email：wxchenq@yutong.com
 */
public class GsonUtil {
    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyyMMddHHmmss")
            .create();

    public static Gson getGson() {
        return gson;
    }

    public static String obj2Str(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T str2Obj(String json, Class<T> tClass) {
        return gson.fromJson(json, tClass);
    }

    public static <T> T str2Obj(String json, Type type){
        return gson.fromJson(json, type);
    }

}
