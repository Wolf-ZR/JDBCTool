package com.zhang.test;

import java.util.ArrayList;
import java.util.List;

public class Test3 {
    public static void main(String[] args) {
        List list= new ArrayList();
        list.add(10);
        list.add(9);
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i));
        }
    }
}
