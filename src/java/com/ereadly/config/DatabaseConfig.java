package com.ereadly.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/db_ereadly";
    private static final String USER = "root";
    private static final String PASSWORD = " "; //sesuaikan pass dan namausernya

    static {
        try {
            Class.forName(DRIVER);
        } catch (Exception e) {
            System.err.println("X Gagal load JDBC Driver");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception {
        System.out.println(" Mencoba koneksi ke database...");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
