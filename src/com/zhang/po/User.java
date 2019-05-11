package com.zhang.po;

import java.sql.*;
import java.util.*;

public class User{

	private Integer id;
	private Integer age;
	private String name;
	private Integer bonus;
	private Integer number;
	private Integer salary;
	private Integer cityid;
	public Integer getId(){
		return id;
	}
	public Integer getAge(){
		return age;
	}
	public String getName(){
		return name;
	}
	public Integer getBonus(){
		return bonus;
	}
	public Integer getNumber(){
		return number;
	}
	public Integer getSalary(){
		return salary;
	}
	public Integer getCityid(){
		return cityid;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setAge(Integer age){
		this.age=age;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setBonus(Integer bonus){
		this.bonus=bonus;
	}
	public void setNumber(Integer number){
		this.number=number;
	}
	public void setSalary(Integer salary){
		this.salary=salary;
	}
	public void setCityid(Integer cityid){
		this.cityid=cityid;
	}
}
