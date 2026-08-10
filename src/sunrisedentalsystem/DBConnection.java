package sunrisedentalsystem;

import java.sql.Connection;
import java.sql.DriverManager;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Future_Mind
 */
public class DBConnection 
{
    private static DBConnection instance;
    private Connection connection;

    private DBConnection() 
    {
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sunrise_dental_db", "root", "");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public static DBConnection getInstance() 
    {
        if (instance == null) instance = new DBConnection();
        return instance;
    }

    public Connection getConnection() 
    {
        return connection;
    }
}