package com.zhang.po;

import java.sql.*;
import java.util.*;

public class Users{

	private Integer id;
	private String name;
	private java.sql.Blob image;
	public Integer getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public java.sql.Blob getImage(){
		return image;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setImage(java.sql.Blob image){
		this.image=image;
	}
}
