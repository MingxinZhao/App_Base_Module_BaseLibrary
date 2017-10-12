package com.dream.base.common;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * @ClassName: JsonParse
 * @Description: 把Json转换成对应的实体类
 * @author: wang
 * @date: 2016-3-9 下午4:32:26
 */
public class JsonParse {

    private static String TAG = "JsonParse";

    /**
     * 采用的解析方式为Gson
     */
    public final static int GSON = 1;

    private Gson gson;

    private Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }

        return gson;
    }

    /**
     * 默认为Gson解析
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <E> E parse(String json, Class<? extends E> clazz) {

        return parse(GSON, json, clazz);

    }

    public static <E> E parse(int type, String json, Class<? extends E> clazz) {
        E e = null;

        try {
            switch (type) {
                case GSON:
                    e = (E) new Gson().fromJson(json, clazz);
                    break;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return e;
    }

    /**
     * 把json解析成带有泛型的ResponseResult
     *
     * @param json
     * @param type
     * @return
     */
    public static ResponseResult parseWithHeader(String json, Type type) {

        ResponseResult entity = null;

        try {

            entity = new Gson().fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();

        }

        return entity;
    }

    public static String cast(Object obj) {

        Gson gson = new Gson();
        return gson.toJson(obj);

    }

//    /**
//     * 请求是否成功
//     *
//     * @return
//     */
//    public static boolean isSuccess(String result) {
//        try {
//            JSONObject json = new JSONObject(result);
//            String status = json.getString("status");
//
//            if (status.equals("success")) {
//                return true;
//            } else {
//                return false;
//            }
//
//        } catch (Exception e) {
//            return false;
//        }
//    }

}
