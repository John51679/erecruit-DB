package utils;

import MultiUserLoginForm.LoginController;

import java.sql.*;

public class ConnectionUtils {
    Connection conn = null;

    //This method connects our java program with the MySQL database
    //The name of the server is 'root'
    //The password is 'password'
    public static Connection conDB(){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/erecruit?user=root","root", "password");
            return con;
        }catch (Exception ex){
            System.err.println("Got an exception on the conDB method");
            return null;
        }
    }
}
