package kl.se;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySingleTon {
    
    private static Connection con;
    /**
     * Create private constructor
     */
    private MySingleTon(){
         
    }
    /**
     * Create a static method to get instance.
     */
    public static Connection getInstance(){
        if(con == null){
        	
        	try {
				Class.forName(DbConnectConstants.DRIVER_NAME );
			
				 con = DriverManager.getConnection(
						DbConnectConstants.CONNECTION_URL, DbConnectConstants.DB_NAME,
						DbConnectConstants.DB_PASSWORD);
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Opened database successfully");
			 return con;
        }
        return con;
    }
     
    public void getSomeThing(){
        // do something here
        System.out.println("I am here....");
    }
     
   
}
