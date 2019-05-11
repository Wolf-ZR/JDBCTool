package com.zhang.vo;

public class UserVO {
    /*select u.id,u.name,salary+bonus 'xinshui',age,c.city from user u
    join citys c on u.cityid=c.id;*/
    private Integer id;
    private String name;
    private Long xinshui;
    private Integer age;
    private String city;

    public UserVO() {
    }

    public UserVO(Integer id, String name, Long xinshui, Integer age, String city) {
        this.id = id;
        this.name = name;
        this.xinshui = xinshui;
        this.age = age;
        this.city = city;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getXinshui() {
        return xinshui;
    }

    public void setXinshui(Long xinshui) {
        this.xinshui = xinshui;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
