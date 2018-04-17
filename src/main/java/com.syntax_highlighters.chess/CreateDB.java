package com.syntax_highlighters.chess;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateDB {



    /**
     *
     * @author sqlitetutorial.net
     */
    public class Main {

        /**
         * Connect to a sample database
         *
         * @param fileName the database file name
         */
        public static void createNewDatabase(String fileName) {

            String pathh = new File("accountsDatabase.db").getAbsolutePath();
            String url = "jdbc:sqlite:"+pathh;

            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    DatabaseMetaData meta = conn.getMetaData();
                    System.out.println("The driver name is " + meta.getDriverName());
                    System.out.println("A new database has been created.");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        /**
         * @param args the command line arguments
         */
        public static void main(String[] args) {
            createNewDatabase("test.db");
        }
    }
}
