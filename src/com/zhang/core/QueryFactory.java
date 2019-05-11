package com.zhang.core;

/**
 * 创建Query类的工厂类
 */
public class QueryFactory {
    private QueryFactory(){}
    public static QueryFactory factory=new QueryFactory();
    private static  Query protoTypeObj=null;//原型对象
    //private static Class c=null;//单例模式
    static {
        try {
            Class c=Class.forName(DBManage.getConf().getQueryClass());//加载指定的Query类
            protoTypeObj=(Query)c.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Query createQuery(){
        try {
            return (Query)protoTypeObj.clone();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
