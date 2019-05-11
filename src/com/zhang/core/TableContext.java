package com.zhang.core;

import com.zhang.bean.ColumnInfo;
import com.zhang.bean.TableInfo;
import com.zhang.util.JavaFileUtil;
import com.zhang.util.StringUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 负责获取管数据库所有表结构和类结构的关系
 * 并可以根据表结构生成类结构
 */
public class TableContext {
    /**
     * 表名为key，表信息对象为value
     */
    public static Map<String, TableInfo> tables=new HashMap<String,TableInfo>();
    /**
     * 将po的class对象和表对象信息关联起来便于重用
     */
    public static Map<Class,TableInfo> poClassTableMap=new HashMap<>();
    private TableContext(){}
    static {
        //初始化获得的表信息

        try {
            Connection conn=DBManage.getConn();
            DatabaseMetaData dbmd=conn.getMetaData();
            ResultSet tableSet=dbmd.getTables(null,"%",
                    "%",new String[]{"TABLE"});
            while (tableSet.next()){
                String tableName=(String)tableSet.getObject("TABLE_NAME");
                TableInfo tableInfo=new TableInfo(tableName, new ArrayList<ColumnInfo>(),
                        new HashMap<String,ColumnInfo>());
                tables.put(tableName,tableInfo);
                /**
                 * 查询表中的所有字段
                 */
                ResultSet set=dbmd.getColumns(null,"%",tableName,
                        "%");
                while (set.next()){
                    ColumnInfo columnInfo=new ColumnInfo(set.getString("COLUMN_NAME"),
                            set.getString("TYPE_NAME"),0);
                    tableInfo.getColumns().put(set.getString("COLUMN_NAME"),columnInfo);
                }
                /**
                 * 查询user表中的主键
                 */
                ResultSet set1=dbmd.getPrimaryKeys(null,"%",tableName);
                while (set1.next()){
                    ColumnInfo columnInfo=(ColumnInfo)tableInfo.getColumns()
                            .get(set1.getObject("COLUMN_NAME"));
                    columnInfo.setKeyType(1);//设置为主键类型
                    tableInfo.getPriKeys().add(columnInfo);
                }
                if(tableInfo.getPriKeys().size()>0){//取唯一主键，如果是联合主键，则为null
                    tableInfo.setOnlyPriKey(tableInfo.getPriKeys().get(0));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //更新类结构
        updateJavaPoFile();
        //加载po包下面的类
        loadPoTables();
    }

    /**
     * 根据表结构，更新po包下面的Java类
     * 实现了从表结构转化到类结构
     */
    public static void updateJavaPoFile(){
        Map<String,TableInfo> tables=TableContext.tables;
        for(TableInfo tableInfo:tables.values()){
            JavaFileUtil.createJavaPoFile(tableInfo,new MysqlTypeConvertor());
        }
    }

    /**
     * 加载po包下面的类
     */
    public static void loadPoTables(){
        for(TableInfo tableInfo:tables.values()){
            try {
                Class c=Class.forName(DBManage.getConf().getPoPackage()+
                        "."+ StringUtil.firstCharToUpperCase(tableInfo.getTname()));
                poClassTableMap.put(c,tableInfo);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    /*  public static void main(String[] args) {
        Map<String,TableInfo> tableInfoMap=TableContext.tables;
        System.out.println(tables);
    }*/
}
