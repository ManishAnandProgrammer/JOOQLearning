package com.manish.utils;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public final class ConnectionUtils {
    private ConnectionUtils() {
        throw new UnsupportedOperationException("No Object For This Class..!");
    }

    private static final String URL = "jdbc:mysql://localhost:3306/sakila";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "myPassword";

    @SneakyThrows
    public static Connection getConnection() {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
