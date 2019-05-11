package com.zhang.core;

/**
 * mysql数据类型和Java数据类型的转换
 */
public class MysqlTypeConvertor implements TypeConvertor{
    @Override
    public String databaseTypeToJavaType(String columnType) {
        if("varchar".equalsIgnoreCase(columnType)||"char".equalsIgnoreCase(columnType)){
            return "String";
        }else if("int".equalsIgnoreCase(columnType)
        ||"tinyint".equalsIgnoreCase(columnType)||"smallint".equalsIgnoreCase(columnType)
        ||"integer".equalsIgnoreCase(columnType)){
            return "Integer";
        } else if ("bigint".equalsIgnoreCase(columnType)) {
            return "Long";
        } else if ("double".equalsIgnoreCase(columnType)||"float".equalsIgnoreCase(columnType)) {
            return "Double";
        }else if("clob".equalsIgnoreCase(columnType)){
            return "java.sql.Clob";
        } else if ("blob".equalsIgnoreCase(columnType)) {
            return "java.sql.Blob";
        } else if ("longblob".equalsIgnoreCase(columnType)) {
            return "java.sql.Blob";
        }else if("date".equalsIgnoreCase(columnType)){
            return "java.sql.Date";
        }else if("timestamp".equalsIgnoreCase(columnType)){
            return "java.sql.Timestamp";
        }

        return null;
    }

    @Override
    public String javaTypeToDatabaseType(String columnType) {
        return null;
    }
}
