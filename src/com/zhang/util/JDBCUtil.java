package com.zhang.util;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 封装了JDBC查询常用的操作
 */
public class JDBCUtil {
    /**
     * 给sql设参数
     * @param statement 预编译sql语句对象
     * @param params 参数
     */
    public static void handleParams(PreparedStatement statement, Object[] params){
        if(params!=null){
            for(int i=0;i<params.length;i++){
                try {
                    statement.setObject(1+i,params[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
