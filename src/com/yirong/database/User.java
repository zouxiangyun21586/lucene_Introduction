package com.yirong.database;

/**
 * 用户表的信息
 * @author luolinghong
 *
 */
public class User {  
    private String id;   //ID 
    private String account;    //账号
    private String password; //密码
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}  