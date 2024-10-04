package org.burnknuckle.utils;

import java.sql.*;
import java.util.*;

import static org.burnknuckle.controllers.Main.logger;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;

public class Database {
    private static final String[] TABLE_NAME = {"userdata", "resdata"};
    private Connection con; 
    public void connectDatabase() {
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
    private boolean checkTableExists(String tableName) {
        try (ResultSet rs = con.getMetaData().getTables(null, null, tableName.toLowerCase(), null)) {
            return !rs.next();
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while checkTableExists| %s \n".formatted(getStackTraceAsString(e)));
            return true;
        }
    }
    private void createTable() {
        String createUserData = "CREATE TABLE IF NOT EXISTS %s (".formatted(TABLE_NAME[0]) +
                "username VARCHAR(25) PRIMARY KEY, " +
                "password VARCHAR(255) NOT NULL, " +
                "privilege VARCHAR(10) NOT NULL, " +
                "email VARCHAR(50) UNIQUE NOT NULL, " +
                "gender VARCHAR(6), " +
                "role VARCHAR(50), " +
                "first_name VARCHAR(30), " +
                "last_name VARCHAR(30), " +
                "phone_number VARCHAR(15), " +
                "date_of_birth DATE, " +
                "account_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "last_login TIMESTAMP, " +    // Timestamp for the last login
                "is_active BOOLEAN DEFAULT TRUE, " + // Status of the user account
                "address TEXT, " +            // Additional field for address
                "profile_picture_url VARCHAR(255), " + // Field for storing URL to profile picture
                "bio TEXT, " +                // Field for a user bio or description
                "failed_login_attempts INT DEFAULT 0, " + // Track failed login attempts for security
                "password_last_updated TIMESTAMP, " + // Timestamp for last password update
                "CONSTRAINT chk_phone_number CHECK (phone_number ~ '^\\+?[0-9]*$'))"; // Ensure valid phone number format

        String createResData = "CREATE TABLE IF NOT EXISTS resData ("
                + "id SERIAL PRIMARY KEY, "                // Unique ID for each request, SERIAL handles auto-increment
                + "username TEXT NOT NULL, "               // Username of the person making the request
                + "userType TEXT CHECK(userType IN ('admin', 'user')) NOT NULL, " // Whether the requester is an admin or a user
                + "location TEXT NOT NULL, "               // Location of the requester
                + "resources TEXT NOT NULL, "              // Resources needed in JSON format
                + "severity TEXT CHECK(severity IN ('low', 'moderate', 'high', 'critical')) NOT NULL, " // Severity level of the disaster
                + "disasterType TEXT NOT NULL, "           // Type of disaster (e.g., flood, earthquake)
                + "priorityLevel TEXT CHECK(priorityLevel IN ('low', 'medium', 'high', 'urgent')) NOT NULL, " // Priority level of the request
                + "dateOfRequest TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " // Date when the request was made
                + "isApproved BOOLEAN DEFAULT FALSE, "     // Whether the request is approved or not
                + "dateOfApproval TIMESTAMP, "             // Date when the request was approved (nullable)
                + "comment TEXT, "                         // Comments regarding the request
                + "contactInfo TEXT, "                     // Contact information of the requester
                + "urgencyLevel TEXT CHECK(urgencyLevel IN ('immediate', 'within_24_hours', 'within_3_days')) NOT NULL, " // Urgency of the request
                + "responseTeamAssigned TEXT, "            // Response team assigned to the request
                + "lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP " // Timestamp of the last update
                + ");";
        try (Statement st = con.createStatement()) {
            st.executeUpdate(createUserData);
            logger.info("Table '%s' created successfully or already exists".formatted(TABLE_NAME[0]));
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while createUserData| %s \n".formatted(getStackTraceAsString(e)));
        }
        try (Statement st = con.createStatement()) {
            st.executeUpdate(createResData);
            logger.info("Table '%s' created successfully or already exists".formatted(TABLE_NAME[0]));
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while createResData| %s \n".formatted(getStackTraceAsString(e)));
        }
    }

    public void insertUserData(String username, String password, String privilege, String email, String gender, String role) {
        String insertSQL = "INSERT INTO userdata(username, password, privilege, email, gender, role) VALUES (?, ?, ?, ?, ?, ?)";
        System.out.println(username+" "+password+" "+privilege+" "+email+" "+ gender+" "+role);
        try (PreparedStatement pStmt = con.prepareStatement(insertSQL)) {
            pStmt.setString(1, username);
            pStmt.setString(2, password);
            pStmt.setString(3, privilege);
            pStmt.setString(4, email);
            pStmt.setString(5, gender);
            pStmt.setString(6, role);
            pStmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while insertUserData| %s \n".formatted(getStackTraceAsString(e)));
        }
    }

    public void queryUserData() {
        String querySQL = "SELECT * FROM %s".formatted(TABLE_NAME[0]);
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(querySQL)) {
            while (rs.next()) {
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Privilege: " + rs.getString("privilege"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println("Role: "+ rs.getString("role"));
                System.out.println("-------------");
            }
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while queryUserData| %s \n".formatted(getStackTraceAsString(e)));
        }
    }
    public int updateUserPrivilege(String username, String newPrivilege) {
        String updateSQL = "UPDATE %s SET privilege = ? WHERE username = ?".formatted(TABLE_NAME[0]);
        try (PreparedStatement pStmt = con.prepareStatement(updateSQL)) {
            pStmt.setString(1, newPrivilege);
            pStmt.setString(2, username);
            return pStmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while updateUserPrivilege| %s \n".formatted(getStackTraceAsString(e)));
            return -1;
        }
    }

    public Map<String, Object> getUsernameDetails(String username) {
        String searchByUsername = "SELECT username, password, privilege, email, gender FROM %s WHERE username = ?".formatted(TABLE_NAME[0]);
        try (PreparedStatement pStmt = con.prepareStatement(searchByUsername)) {
            pStmt.setString(1, username);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    String retrievedUsername = rs.getString("username");
                    String privilege = rs.getString("privilege");
                    String email = rs.getString("email");
                    String gender = rs.getString("gender");
                    String password = rs.getString("password");
                    return new TreeMap<>() {{
                        put("username", retrievedUsername);
                        put("password", password);
                        put("privilege", privilege);
                        put("email", email);
                        put("gender", gender);
                    }};
                } else {
                    logger.warn("No user found with username: %s".formatted(username));
                    return Collections.emptyMap();
                }
            }
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while getUsernameDetails| %s \n".formatted(getStackTraceAsString(e)));
            return Collections.emptyMap();
        }
    }
    public int deleteUserData(String username) {
        String deleteSQL = "DELETE FROM %s WHERE username = ?".formatted(TABLE_NAME[0]);
        try (PreparedStatement pStmt = con.prepareStatement(deleteSQL)) {
            pStmt.setString(1, username);
            return pStmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while deleteUserData| %s \n".formatted(getStackTraceAsString(e)));
            return -1;
        }
    }

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

    public boolean isAdmin(String username) {
        String query = "SELECT privilege FROM %s WHERE username = ?".formatted(TABLE_NAME[0]);
        try (PreparedStatement pStmt = con.prepareStatement(query)) {
            pStmt.setString(1, username);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    String privilege = rs.getString("privilege");
                    return "admin".equalsIgnoreCase(privilege);
                }
            }
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while isAdmin| %s \n".formatted(getStackTraceAsString(e)));
        }
        return false;
    }

    public boolean canCreateCoAdmin(String username) {
        return isAdmin(username);
    }
    public List<Map<String, Object>> getPrivilegeData(String privilege) {
        List<Map<String, Object>> coAdmins = new ArrayList<>();
        String querySQL = "SELECT * FROM userdata WHERE privilege = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(querySQL)) {
            ps.setString(1, privilege);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("username", rs.getString("username"));
                    row.put("password", rs.getString("password"));
                    row.put("privilege", rs.getString("privilege"));
                    row.put("email", rs.getString("email"));
                    row.put("gender", rs.getString("gender"));
                    row.put("role", rs.getString("role"));
                    row.put("first_name", rs.getString("first_name"));
                    row.put("last_name", rs.getString("last_name"));
                    row.put("phone_number", rs.getString("phone_number"));
                    row.put("date_of_birth", rs.getDate("date_of_birth"));
                    row.put("account_created", rs.getTimestamp("account_created"));
                    row.put("last_login", rs.getTimestamp("last_login"));
                    row.put("is_active", rs.getBoolean("is_active"));
                    row.put("address", rs.getString("address"));
                    row.put("profile_picture_url", rs.getString("profile_picture_url"));
                    row.put("bio", rs.getString("bio"));
                    row.put("failed_login_attempts", rs.getInt("failed_login_attempts"));
                    row.put("password_last_updated", rs.getTimestamp("password_last_updated"));
                    coAdmins.add(row);
                }
            }
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while canCreateCoAdmin| %s \n".formatted(getStackTraceAsString(e)));
        }
        return coAdmins;
    }
}
