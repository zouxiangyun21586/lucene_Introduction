package com.yirong.database;

/**
 * �û������Ϣ
 * @author luolinghong
 *
 */
public class User {  
    private String id;   //ID 
    private String account;    //�˺�
    private String password; //����
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