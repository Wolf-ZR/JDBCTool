package com.zhang.core;

import com.zhang.bean.ColumnInfo;
import com.zhang.bean.TableInfo;
import com.zhang.po.User;
import com.zhang.po.Users;
import com.zhang.util.JDBCUtil;
import com.zhang.util.ReflectUtil;
import com.zhang.util.StringUtil;
import com.zhang.vo.UserVO;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责Mysql数据库的查询
 */
public class MysqlQuery extends Query{
    public static void testDML(){
        User user=new User();
        user.setId(7);
        MysqlQuery query=new MysqlQuery();
        query.delete(user);
    }

    public static void main(String[] args) {
       /*List<User> list=new MysqlQuery().queryRows("select id,name,age,number from user where age=? and id>?"
       ,User.class,new Object[]{20,1});
       for(User u:list){
           System.out.println(u.getId()+"--"+u.getName()+"--"+u.getAge()+"--"+u.getNumber());
       }*/
       /*String sql="select u.id,u.name,salary+bonus 'xinshui',age,c.city from user u " +
               "join citys c on u.cityid=c.id";
       List<UserVO> list=new MysqlQuery().queryRows(sql,UserVO.class,null);
       for(UserVO u:list){
           System.out.println(u.getId()+"--"+u.getAge()+"--"+u.getXinshui()+"--"+u.getName()+"--"+u.getCity());
       }*/
      /* Object obj=new MysqlQuery().queryValue("select count(*) from user where age=?",new Object[]{20});
        System.out.println(obj);*/
    }

}
