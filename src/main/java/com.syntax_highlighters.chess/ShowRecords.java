package com.syntax_highlighters.chess;

import java.io.File;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ShowRecords {

    private Connection connect() {
        // SQLite connection string
        String pathh = new File("sample.db").getAbsolutePath();
        String url = "jdbc:sqlite:"+pathh;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public void selectAll(){
        String sql = "SELECT * FROM person";

        try {
            Connection conn = this.connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("score") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getInt("wins") + "\t" +
                        rs.getInt("losses"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ShowRecords app = new ShowRecords();
        String query = "SELECT name FROM accounts where name='robin'";
        System.out.println(query);

        app.selectAll();
    }

}