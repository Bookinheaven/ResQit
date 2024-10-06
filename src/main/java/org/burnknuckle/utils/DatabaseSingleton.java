package org.burnknuckle.utils;

public class DatabaseSingleton {
}

/*
package org.burnknuckle.utils;

import java.sql.*;
import java.util.*;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;

public class Database {
    private static final String[] TABLE_NAME = {"userdata", "resdata", "disasterData"};
    private static Database instance;
    private Connection con;

    // Private constructor to prevent instantiation
    private Database() {
        connectDatabase();
    }

    // Method to get the singleton instance
    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private void connectDatabase() {
        try {
            con = getConnection();
            if (checkTableExists(TABLE_NAME[0]) || checkTableExists(TABLE_NAME[1])) {
                createTable();
            }
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while connectDatabase| %s \n".formatted(getStackTraceAsString(e)));
        }
    }

    public Connection getConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            String dbUrl = "jdbc:postgresql://localhost:5432/resqit";
            String dbUsername = "burn";
            String dbPassword = "i3urnknuckle#124";
            con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        }
        return con;
    }

    // The rest of your methods remain unchanged...

    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                logger.info("Database connection closed.");
            }
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while closeConnection| %s \n".formatted(getStackTraceAsString(e)));
        }
    }
}

 */