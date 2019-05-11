package com.zhang.core;

import com.zhang.bean.Configuration;
import com.zhang.pool.DBConnPool;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 根据配置信息，维持连接对象的管理（增加连接池功能）
 */
public class DBManage {
    /**
     * 配置信息
     */
    private static Configuration conf;
    /**
     * 连接池对象
     */
    private static DBConnPool pool;
    public static Configuration getConf() {
        return conf;
    }

    static {
        Properties properties=new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().
                    getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        conf=new Configuration();
        conf.setDriver(properties.getProperty("driver"));
        conf.setUrl(properties.getProperty("url"));
        conf.setUser(properties.getProperty("user"));
        conf.setPwd(properties.getProperty("pwd"));
        conf.setPoPackage(properties.getProperty("poPackage"));
        conf.setSrcPath(properties.getProperty("srcPath"));
        conf.setUsingDB(properties.getProperty("usingDB"));
        conf.setQueryClass(properties.getProperty("queryClass"));
        conf.setPoolMaxSize(Integer.parseInt(properties.getProperty("poolMaxSize")));
        conf.setPoolMinSize(Integer.parseInt(properties.getProperty("poolMinSize")));
        //加载TableContext
        System.out.println(TableContext.class);
    }

    /**
     * 获得数据库的连接
     * @return
     */
    public static Connection getConn(){
        if(pool==null){
            pool=new DBConnPool();
        }
        return pool.getConnection();
    }

    /**
     * 创建一个新的连接
     * @return
     */
    public static Connection createConn(){
        try {
            Class.forName(conf.getDriver());
            return DriverManager.getConnection(conf.getUrl(),conf.getUser(),conf.getPwd());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 关闭传入的ResultSet，Statement，Connection
     * @param rs
     * @param statement
     * @param conn
     */
    public static void close(ResultSet rs,Statement statement,Connection conn){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        close(statement,conn);
    }

    /**
     * 关闭传入的Statement，Connection
     * @param statement
     * @param conn
     */
    public static void close(Statement statement,Connection conn){
        if(statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        pool.close(conn);
    }

    /**
     * 关闭传入的Connection
     * @param conn
     */
    public static void close(Connection conn){
        pool.close(conn);
    }
}
