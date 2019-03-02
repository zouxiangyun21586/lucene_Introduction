package com.yirong.database;
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.SQLException;  
  
/**
 * jdbc获得数据库连接的工具
 * @author luolinghong
 *
 */
public class JdbcUtil {  
    private static Connection conn = null; 
    /**
     * 数据库URL
     */
    private static final String URL = "jdbc:mysql://localhost:3306/mysqltest";
    /**
     * 加载数据库驱动
     */
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    /**
     * 数据库用户名
     */
    private static final String USER_NAME = "root";
    /**
     * 数据库用户密码
     */
    private static final String PASSWORD = "root";  
    
  
    /**
     * 连接数据库
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