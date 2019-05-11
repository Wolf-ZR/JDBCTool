package com.zhang.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 封装了反射常用的操作
 */
public class ReflectUtil {
    /**
     * 调用obj对象对应属性fieldName的get方法
     * @param fieldName
     * @param obj
     * @return
     */
    public static Object invokeGet(String fieldName,Object obj){
        try {
            Class c=obj.getClass();
            Method m=c.getMethod("get"+ StringUtil.firstCharToUpperCase(fieldName),null);
            return m.invoke(obj,null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void invokeSet(Object obj,String columnName,Object columnValue){
        try {
            if(columnName!=null){
                Method m=obj.getClass().getDeclaredMethod("set"+StringUtil.
                        firstCharToUpperCase(columnName),columnValue.getClass());
                m.invoke(obj,columnValue);
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
