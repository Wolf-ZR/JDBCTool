package com.zhang.test;

import com.zhang.core.Query;
import com.zhang.core.QueryFactory;
import com.zhang.vo.UserVO;

import java.util.List;

/**
 * 测试连接池效率
 */
public class Test2 {
    public static void test01(){
        Query q= QueryFactory.factory.createQuery();
        String sql="select u.id,u.name,salary+bonus 'xinshui',age,c.city from user u " +
                "join citys c on u.cityid=c.id";
        List<UserVO> list=q.queryRows(sql,UserVO.class,null);
        for(UserVO u:list){
            System.out.println(u.getId()+"--"+u.getAge()+"--"+u.getXinshui()+"--"+u.getName()+"--"+u.getCity());
        }
    }
    public static void main(String[] args) {
        Long start=System.currentTimeMillis();
        for(int i=0;i<3000;i++){
            test01();
        }
        Long end=System.currentTimeMillis();
        System.out.println(end-start);

    }
}
