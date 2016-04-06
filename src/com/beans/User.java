package com.beans;

public class User {
	private String email;
	private String name;
	private String password;
	private String type;
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	public boolean validate(){
		
		if(this.email.equals("")){
			this.msg += "Please Enter your email";
		}
		if(this.name.equals("")){
			this.msg += "Please Enter your name";
		}
		if(this.password.equals("")){
			this.msg += "Please Enter your password";
		}
		else if(this.password.length() < 6){
			this.msg += "Password should be more than 6 characters in length";
		}
		if(this.type.equals("")){
			this.msg += "Enter whether you are parent or volunteer";
		}
		
		if(this.msg.equals("")){
			return true;
		}
		return false;
	}	
}