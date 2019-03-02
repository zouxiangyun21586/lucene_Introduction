package com.yirong.database;
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.SQLException;  
  
/**
 * jdbc������ݿ����ӵĹ���
 * @author luolinghong
 *
 */
public class JdbcUtil {  
    private static Connection conn = null; 
    /**
     * ���ݿ�URL
     */
    private static final String URL = "jdbc:mysql://localhost:3306/mysqltest";
    /**
     * �������ݿ�����
     */
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    /**
     * ���ݿ��û���
     */
    private static final String USER_NAME = "root";
    /**
     * ���ݿ��û�����
     */
    private static final String PASSWORD = "root";  
    
  
    /**
     * �������ݿ�
     * @return
     */
    public static Connection getConnection() {  
        try {  
            Class.forName(JDBC_DRIVER);  
            conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return conn;  
    }  
}  