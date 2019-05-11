package com.zhang.util;

import com.zhang.bean.ColumnInfo;
import com.zhang.bean.Configuration;
import com.zhang.bean.JavaFieldGetSet;
import com.zhang.bean.TableInfo;
import com.zhang.core.DBManage;
import com.zhang.core.MysqlTypeConvertor;
import com.zhang.core.TableContext;
import com.zhang.core.TypeConvertor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *封装了生成Java文件（源代码）常用的操作
 */
public class JavaFileUtil {
    /**
     * 根据字段信息生成Java属性信息
     * @param columnInfo 字段信息
     * @param convertor 类型转化器
     * @return Java属性和set/get方法源代码
     */
    public static JavaFieldGetSet createFieldGetSetSrc(ColumnInfo columnInfo, TypeConvertor convertor){
        JavaFieldGetSet javaFieldGetSet=new JavaFieldGetSet();
        String javaFieldType=convertor.databaseTypeToJavaType(columnInfo.getDataType());
        javaFieldGetSet.setFieldInfo("\tprivate "+javaFieldType+" "+columnInfo.getName()+";\n");
        //生成get方法源代码
        StringBuilder getSrc=new StringBuilder();
        getSrc.append("\tpublic "+javaFieldType+" get"+StringUtil.
                firstCharToUpperCase(columnInfo.getName())+"(){\n");
        getSrc.append("\t\treturn "+columnInfo.getName()+";\n");
        getSrc.append("\t}\n");
        javaFieldGetSet.setGetInfo(getSrc.toString());
        //生成et方法源代码
        StringBuilder setSrc=new StringBuilder();
        setSrc.append("\tpublic void set"+StringUtil.
                firstCharToUpperCase(columnInfo.getName())+"(");
        setSrc.append(javaFieldType+" "+columnInfo.getName()+"){\n");
        setSrc.append("\t\tthis."+columnInfo.getName()+"="+columnInfo.getName()+";\n");
        setSrc.append("\t}\n");
        javaFieldGetSet.setSetInfo(setSrc.toString());
        return javaFieldGetSet;

    }

    /**
     * 根据表信息生成Java源代码
     * @param tableInfo 表信息
     * @param convertor 数据类型转化器
     * @return Java类源代码
     */
    public static String createJavaSrc(TableInfo tableInfo,TypeConvertor convertor){
        StringBuilder src=new StringBuilder();
        Map<String,ColumnInfo> columns=tableInfo.getColumns();
        List<JavaFieldGetSet> javaFields=new ArrayList<>();
        for(ColumnInfo c:columns.values()){
            javaFields.add(createFieldGetSetSrc(c,convertor));
        }
        //生成package语句
        src.append("package "+ DBManage.getConf().getPoPackage()+";\n\n");
        //生成import语句
        src.append("import java.sql.*;\n");
        src.append("import java.util.*;\n\n");
        //生成类声明语句
        src.append("public class "+StringUtil.firstCharToUpperCase(tableInfo.getTname())+"{\n\n");
        //生成属性列表
        for(JavaFieldGetSet f:javaFields){
            src.append(f.getFieldInfo());
        }
        //生成get方法列表
        for(JavaFieldGetSet f:javaFields){
            src.append(f.getGetInfo());
        }
        //生成set方法列表
        for(JavaFieldGetSet f:javaFields){
            src.append(f.getSetInfo());
        }
        //生成结束符
        src.append("}\n");
        return src.toString();
    }
    public static void createJavaPoFile(TableInfo tableInfo,TypeConvertor convertor){
        String src=createJavaSrc(tableInfo,convertor);
        String srcPath=DBManage.getConf().getSrcPath()+"/";
        String popackagePath=DBManage.getConf().getPoPackage().replaceAll("\\.","/");
        File f=new File(srcPath+popackagePath);
        if(f.exists()){
            f.mkdirs();
        }
        BufferedWriter bw=null;
        try {
            bw=new BufferedWriter(new FileWriter(f.getAbsolutePath()+
                    "/"+StringUtil.firstCharToUpperCase(tableInfo.getTname())+".java"));

            bw.write(src);
            bw.flush();
            System.out.println("建立表"+tableInfo.getTname()+"对应的Java类"+
                    StringUtil.firstCharToUpperCase(tableInfo.getTname())+".java");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(bw!=null){
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        /*ColumnInfo ci=new ColumnInfo("username","varchar",0);
        JavaFieldGetSet f=createFieldGetSetSrc(ci,new MysqlTypeConvertor());
        System.out.println(f);*/
        /*Map<String,TableInfo> tables=TableContext.tables;
        for(TableInfo tableInfo:tables.values()){
            JavaFileUtil.createJavaPoFile(tableInfo,new MysqlTypeConvertor());
        }*/

    }
}
