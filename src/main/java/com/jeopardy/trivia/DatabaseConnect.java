package com.jeopardy.trivia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnect {

     private final String url = "jdbc:postgresql://localhost:5432/test_db";
     private final String user = "root";
     private final String password = "root";

     /**
      * Connect to the PostgreSQL database
      *
      * @return a Connection object
      */
     public Connection connect() {
         Connection conn = null;
         try {
             conn = DriverManager.getConnection(url, user, password);
             System.out.println("Connected to the PostgreSQL server successfully.------");
         } catch (SQLException e) {
             System.out.println(e.getMessage());
         }

         return conn;
     }

//     /**
//      * @param args the command line arguments
//      */
//     public static void main(String[] args) {
//         database_connect app = new database_connect();
//         app.connect();
//    }

}
