package com.zhang.core;

import com.zhang.bean.ColumnInfo;
import com.zhang.bean.TableInfo;
import com.zhang.util.JDBCUtil;
import com.zhang.util.ReflectUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责查询（对外提供服务的核心类）
 */
public abstract class Query implements Cloneable{
    /**
     * 采用模板方法
     * 执行查询语句的模板
     * @param sql
     * @param params
     * @param back 回调方法
     * @return
     */
    public Object executeQueryTemplate(String sql, Object[] params, CallBack back) {
        Connection conn = DBManage.getConn();
        ResultSet rs = null;
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);
            //设参数
            JDBCUtil.handleParams(statement, params);
            System.out.println(statement);
            rs = statement.executeQuery();
            return back.doExecute(conn, statement, rs);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 直接执行一个DML语句
     * @param sql sql语句
     * @param params 参数
     * @return 影响记录的行数
     */
    public int executeDML(String sql, Object[] params) {
        Connection conn=DBManage.getConn();
        int count=0;
        PreparedStatement statement=null;
        try {
            statement=conn.prepareStatement(sql);
            //设参数
            JDBCUtil.handleParams(statement,params);
            System.out.println(statement);
            count=statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBManage.close(statement,conn);
        }
        return count;
    }

    /**
     * 将一个对象存储到数据库中
     * @param obj 要存储的对象
     */
    public void insert(Object obj) {
        Class c=obj.getClass();
        //获取表名
        List<Object> params=new ArrayList<>();
        TableInfo tableInfo=TableContext.poClassTableMap.get(c);
        Field[] fs=c.getDeclaredFields();
        StringBuilder sql=new StringBuilder("insert into "+tableInfo.getTname()+" (");
        int countNotNullField=0;
        for(Field f:fs){
            String fieldName=f.getName();
            Object fieldValue= ReflectUtil.invokeGet(fieldName,obj);
            if(fieldValue!=null){
                countNotNullField++;
                sql.append(fieldName+",");
                params.add(fieldValue);
            }
        }
        sql.setCharAt(sql.length()-1,')');
        sql.append(" values (");
        for(int i=0;i<countNotNullField;i++){
            sql.append("?,");
        }
        sql.setCharAt(sql.length()-1,')');
        executeDML(sql.toString(),params.toArray());
    }

    /**
     * 删除clazz表示类对应的表中数据（指定主键值id的记录）
     * @param clazz 跟表对应的Class类
     * @param id 主键的值
     * @return
     */
    public void delete(Class clazz, Object id) {
        //通过Class对象找TableInfo
        TableInfo tableInfo=TableContext.poClassTableMap.get(clazz);
        //获得主键
        ColumnInfo onlyPriKey=tableInfo.getOnlyPriKey();

        String sql="delete from "+tableInfo.getTname()+" where "+onlyPriKey.getName()+"=?";
        executeDML(sql,new Object[]{id});
    }

    /**
     * 删除对象在数据库中对应的记录（对象所在的类对应到表，对象的主键值对应到记录）
     * @param obj
     */
    public void delete(Object obj) {
        Class c=obj.getClass();
        TableInfo tableInfo=TableContext.poClassTableMap.get(c);
        ColumnInfo onlyPriKey=tableInfo.getOnlyPriKey();
        //通过反射机制，调用属性的get方法
        Object priKeyValue=ReflectUtil.invokeGet(onlyPriKey.getName(),obj);
        delete(c,priKeyValue);
    }

    /**
     * 更新对象对应的记录，并且只更新指定字段的值
     * @param obj 所要更新的对象
     * @param fileNames 更新的属性列表
     * @return影响的行数
     */
    public int update(Object obj, String[] fileNames) {
        //update 表名 set name=?,pwd=? where id=?
        Class c=obj.getClass();
        TableInfo tableInfo=TableContext.poClassTableMap.get(c);
        List<Object> params=new ArrayList<>();
        ColumnInfo priKey=tableInfo.getOnlyPriKey();
        StringBuilder sql=new StringBuilder("update "+tableInfo.getTname()+" set ");

        for(String fname:fileNames){
            Object fvalue= ReflectUtil.invokeGet(fname,obj);
            params.add(fvalue);
            sql.append(fname+"=?,");
        }
        sql.setCharAt(sql.length()-1,' ');
        sql.append(" where ");
        sql.append(priKey.getName()+"=?");
        params.add(ReflectUtil.invokeGet(priKey.getName(), obj));
        return executeDML(sql.toString(),params.toArray());
    }

    /**
     * 查询返回多行记录，并将每行记录封装到指定类的对象中
     * @param sql 查询的语句
     * @param clazz 封装数据的JavaBean类的Class对象
     * @param params sql的参数
     * @return查询到的结果
     */
    public List queryRows(String sql,final Class clazz,Object[] params) {
        return (List)executeQueryTemplate(sql,params,new CallBack() {
            @Override
            public Object doExecute(Connection conn, PreparedStatement statement, ResultSet rs) {
                List list=new ArrayList();
                try {

                    ResultSetMetaData metaData=rs.getMetaData();
                    //多行
                    while (rs.next()){
                        Object rowObj=clazz.newInstance();
                        //多列
                        for(int i=0;i<metaData.getColumnCount();i++){
                            String columnName=metaData.getColumnLabel(i+1);
                            Object columnValue=rs.getObject(i+1);
                            //调用rowObj对象的setUsername的方法，将columnValue的值设进去
                            ReflectUtil.invokeSet(rowObj,columnName,columnValue);

                        }
                        list.add(rowObj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    DBManage.close(rs,statement,conn);
                }
                return list;
            }
        });
    }

    /**
     *查询返回一行记录，并将该记录封装到clazz指定类的对象中
     * @param sql 查询语句
     * @param clazz 封装数据的JavaBean类的Class对象
     * @param params sql的参数
     * @return 查询的结果
     */
    public Object queryRow(String sql, Class clazz, Object[] params) {
        List list=queryRows(sql,clazz,params);

        return (list!=null&&list.size()>0)?list.get(0):null;
    }
    /**
     *查询返回一个值，并将该值返回
     * @param sql 查询语句
     * @param params sql的参数
     * @return 查询的结果
     */
    public Object queryValue(String sql, Object[] params) {

        return executeQueryTemplate(sql, params, new CallBack() {
            Object value=null;
            @Override
            public Object doExecute(Connection conn, PreparedStatement statement, ResultSet rs) {
                try {
                    while (rs.next()){
                        value=rs.getObject(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return value;
            }
        });
    }

    /**
     * 查询返回一个数字，并将该值返回
     * @param sql sql语句
     * @param params sql参数
     * @return
     */
    public Number queryNumber(String sql, Object[] params) {
        return (Number)queryValue(sql,params);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
