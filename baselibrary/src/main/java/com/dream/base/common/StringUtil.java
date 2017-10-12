package com.dream.base.common;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: TODO
 * author: Wang
 * date: 3/9/17 16:46
 * email:life_artist@163.com
 * Copyright©2017 by wang. All rights reserved.
 */
public class StringUtil {

    /**
     * 拼装网络请求地址
     *
     * @param url       网络请求地址
     * @param paramsMap 待拼接的参数集合
     * @return 拼接后的请求地址
     */
    public static String organizeUrl(String url, Map paramsMap) {
        if (url != null) {
            if (paramsMap != null) {
                String args = "";
                if (!"".equals(paramsMap) && paramsMap.size() > 0) {
                    Set<String> key = paramsMap.keySet();
                    for (Iterator it = key.iterator(); it.hasNext(); ) {
                        String s = (String) it.next();
                        args = args + s + "=" + paramsMap.get(s) + "&";
                    }
                }

                if (args != null && args.length() > 0) {
                    args = args.substring(0, args.length() - 1);
                    url = url + "?" + args;
                }
            }

            return url;
        }

        return null;
    }

    /**
     * get 请求组合 "?"+参数
     *
     * @param paramsMap
     * @return
     */
    public static String organizeParams(Map paramsMap) {
        String args = "";
        if (paramsMap != null) {
            if (!"".equals(paramsMap) && paramsMap.size() > 0) {
                Set<String> key = paramsMap.keySet();
                for (Iterator it = key.iterator(); it.hasNext(); ) {
                    String s = (String) it.next();
                    args = args + s + "=" + paramsMap.get(s) + "&";
                }
            }

            if (args != null && args.length() > 0) {
                args = args.substring(0, args.length() - 1);
                args = "?" + args;
            }
        }

        return args;
    }

    /**
     * map 转String
     *
     * @param map
     * @return
     */
    public static String transMapToString(Map map) {
        if (map == null) {
            return null;
        }
        java.util.Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (java.util.Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("=").append(null == entry.getValue() ? "" :
                    entry.getValue().toString()).append(iterator.hasNext() ? ";" : "");
        }
        return sb.toString();
    }

    /**
     * 是否为中文
     *
     * @param input
     * @return
     */
    public static boolean isChinese(String input) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(input);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }

    /**
     * 过滤特殊字符
     *
     * @param str
     * @return
     */
    public static String filterSpecialCharacter(String str) {
        if (str != null) {
            return str.replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "");
        }

        return null;
    }

    /**
     * 过滤所有空格，制表符等
     *
     * @param str
     * @return
     */
    public static String fiterEmptyCharacter(String str) {
        if (str != null) {
            return str.replaceAll("[\\s*|\t|\r|\n]", "");
        }

        return null;
    }

    /**
     * 是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 数组转逗号分隔的字符串
     *
     * @param ig
     * @return
     */
    public static String converToString(String[] ig) {
        String str = "";
        if (ig != null && ig.length > 0) {
            for (int i = 0; i < ig.length; i++) {
                str += ig[i] + ",";
            }
        }
        if (!TextUtils.isEmpty(str)) {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    /**
     * ArrayList转逗号分隔的字符串
     *
     * @param
     * @return
     */
    public static String transListToString(List list) {
        String str = "";
        try {
            int size = list.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    str += list.get(i) + ",";
                }
            }
            if (!TextUtils.isEmpty(str)) {
                str = str.substring(0, str.length() - 1);
            }
        } catch (Exception e) {
//            LogUtil.i("", "converToString " + e.toString());
        }

        return str;
    }

    /**
     * 是否为身份证号
     *
     * @param string
     * @return
     */
    public static boolean isIDCard(String string) {
        String telRegex = "/^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$/";
        if (TextUtils.isEmpty(string)) {
            return false;
        } else {
            return string.matches(telRegex);
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：139、138、137、136、135、134、
		    159、158、157、150、151、152、
		    147(数据卡)、
		    188、187、182、183、184、
		    178
		联通：130、131、132、156、155、186、185、145(数据卡)、176
		电信：133、153、180、189、（1349卫通）、181、177、173(待放)
		总结起来就是第一位必定为1，第二位必定为3、4或5或7、8，其他位置的可以为0-9
		*/
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }
}
