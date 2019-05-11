package com.zhang.pool;

import com.zhang.core.DBManage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 连接池的类
 */
public class DBConnPool {
    /**
     * 连接池对象
     */
    private List<Connection> pool;
    /**
     * 最大连接数
     */
    private static final int POOL_MAX_SIZE=DBManage.getConf().getPoolMaxSize();
    /**
     * 最小连接数
     */
    private static final int POOL_MIN_SIZE=DBManage.getConf().getPoolMinSize();

    /**
     * 初始化连接池，使连接数达到最小值
     */
    public void init(){
        if(pool==null){
            pool=new ArrayList<Connection>();
        }
        while (pool.size()<DBConnPool.POOL_MIN_SIZE){
            pool.add(DBManage.createConn());
            System.out.println("初始化池连接数："+pool.size());
        }
    }

    /**
     * 从池中取出一个连接
     * @return
     */
    public synchronized Connection getConnection(){
        int last_index=pool.size()-1;
        Connection conn=pool.get(last_index);
        pool.remove(last_index);
        return conn;
    }

    /**
     * 将连接放回池中
     * @param conn
     */
    public synchronized void close(Connection conn){
       if(pool.size()>=POOL_MAX_SIZE){
           if(conn!=null){
               try {
                   conn.close();
               } catch (SQLException e) {
                   e.printStackTrace();
               }
           }
       }else{
           pool.add(conn);

       }
    }
    public DBConnPool() {
        init();
    }
}
