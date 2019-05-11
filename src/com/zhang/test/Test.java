package com.zhang.test;

import com.zhang.core.MysqlQuery;
import com.zhang.core.Query;
import com.zhang.core.QueryFactory;
import com.zhang.vo.UserVO;

import java.util.List;
import java.util.Queue;

/**
 * 客户端调用测试类
 */
public class Test {
    public static void main(String[] args) {
        Query q= QueryFactory.factory.createQuery();
        String sql="select u.id,u.name,salary+bonus 'xinshui',age,c.city from user u " +
               "join citys c on u.cityid=c.id";
        List<UserVO> list=q.queryRows(sql,UserVO.class,null);
        for(UserVO u:list){
           System.out.println(u.getId()+"--"+u.getAge()+"--"+u.getXinshui()+"--"+u.getName()+"--"+u.getCity());
        }
    }
}
