package com.zhang.po;

import java.sql.*;
import java.util.*;

public class Citys{

	private Integer id;
	private String name;
	private String city;
	public Integer getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getCity(){
		return city;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setCity(String city){
		this.city=city;
	}
}
