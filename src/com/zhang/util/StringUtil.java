package com.zhang.util;

/**
 * 封装了字符串常用的操作
 */
public class StringUtil {
    /**
     * 将目标字符串首字母变为大写
     * @param src
     * @return
     */
    public static String firstCharToUpperCase(String src){
       return src.toUpperCase().substring(0,1)+src.substring(1);
    }
}
