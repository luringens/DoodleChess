package com.syntax_highlighters.chess;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertRecords {

    private Connection connect () {
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


    public void insert (String name, int score, int wins, int losses) {
        String sql = "INSERT INTO person (name, score, wins, losses) VALUES(?,?,?,?)";

        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, score);
            pstmt.setInt(3, wins);
            pstmt.setInt(4, losses);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

   /* public static void main (String[] args) {

        InsertRecords app = new InsertRecords();
        // insert three new rows
        app.insert("Aryan", 30000,1,1);
        app.insert("Robert", 40000,1,5);
        app.insert("Jerry", 50000,0,0);
    }*/
}