package com.zhang.core;

/**
 * 负责Java数据类型和数据库类型的转换
 */
public interface TypeConvertor {
    /**
     * 将数据库类型转换为Java数据类型
     * @param columnType 数据库字段的数据类型
     * @return Java的数据类型
     */
    public String databaseTypeToJavaType(String columnType);
    /**
     * 将Java数据类型转换为数据库数据类型
     * @param columnType Java字段的数据类型
     * @return 数据库数据类型
     */
    public String javaTypeToDatabaseType(String columnType);
}
