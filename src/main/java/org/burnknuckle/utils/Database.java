package org.burnknuckle.utils;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

import static org.burnknuckle.controllers.Main.logger;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;

public class Database {
    private static final String[] TABLE_NAME = {"userdata", "resdata", "disasterdata"};
    private static Database instance;
    private Connection con;
    private Database() {
        connectDatabase();
    }
    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void connectDatabase() {
        try {
            con = getConnection();
            if (checkTableExists(TABLE_NAME[0]) || checkTableExists(TABLE_NAME[1]) || checkTableExists(TABLE_NAME[2])) {
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
                "last_login TIMESTAMP, " +
                "is_active BOOLEAN DEFAULT TRUE, " +
                "address TEXT, " +
                "profile_picture_url VARCHAR(255), " +
                "bio TEXT, " +
                "failed_login_attempts INT DEFAULT 0, " +
                "password_last_updated TIMESTAMP, " +
                "CONSTRAINT chk_phone_number CHECK (phone_number ~ '^\\+?[0-9]*$'))";

        String createResData = "CREATE TABLE IF NOT EXISTS %s (".formatted(TABLE_NAME[1]) +
                "id SERIAL PRIMARY KEY, " +
                "username VARCHAR(25) NOT NULL, " +
                "userType TEXT CHECK(userType IN ('admin', 'user')) NOT NULL, " +
                "location TEXT NOT NULL, " +
                "resources TEXT NOT NULL, " +
                "severity TEXT CHECK(severity IN ('low', 'moderate', 'high', 'critical')) NOT NULL, " +
                "disasterType TEXT NOT NULL, " +
                "priorityLevel TEXT CHECK(priorityLevel IN ('low', 'medium', 'high', 'urgent')) NOT NULL, " +
                "dateOfRequest TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "isApproved BOOLEAN DEFAULT FALSE, " +
                "dateOfApproval TIMESTAMP, " +
                "comment TEXT, " +
                "contactInfo TEXT, " +
                "urgencyLevel TEXT CHECK(urgencyLevel IN ('immediate', 'within_24_hours', 'within_3_days')) NOT NULL, " +
                "responseTeamAssigned TEXT, " +
                "lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (username) REFERENCES %s (username) ".formatted(TABLE_NAME[0]) +
                ");";

        String createDisasterData = "CREATE TABLE IF NOT EXISTS %s (".formatted(TABLE_NAME[2]) +
                "id SERIAL PRIMARY KEY, " +
                "disasterName VARCHAR(100) UNIQUE NOT NULL, " +
                "disasterType TEXT NOT NULL, " +
                "scaleMeter TEXT," +
                "scale TEXT CHECK(scale IN ('local', 'regional', 'national')) NOT NULL, " +
                "severity TEXT CHECK(severity IN ('low', 'moderate', 'high', 'critical')) NOT NULL, " +
                "description TEXT, " +
                "location TEXT NOT NULL, " +
                "startDate TIMESTAMP NOT NULL, " +
                "endDate TIMESTAMP, " +
                "responseStatus TEXT CHECK(responseStatus IN ('requested', 'ongoing', 'resolved')) NOT NULL, " +
                "impactAssessment TEXT, " +
                "dateOfEntry TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "userUploaded VARCHAR(25) NOT NULL, "+
                "FOREIGN KEY (userUploaded) REFERENCES %s (username) ".formatted(TABLE_NAME[0]) +
                ");";
        try (Statement st = con.createStatement()) {
            st.executeUpdate(createUserData);
            logger.info("Table '%s' created successfully or already exists".formatted(TABLE_NAME[0]));
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while createUserData| %s \n".formatted(getStackTraceAsString(e)));
        }
        try (Statement st = con.createStatement()) {
            st.executeUpdate(createResData);
            logger.info("Table '%s' created successfully or already exists".formatted(TABLE_NAME[1]));
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while createResData| %s \n".formatted(getStackTraceAsString(e)));
        }
        try (Statement st = con.createStatement()) {
            st.executeUpdate(createDisasterData);
            logger.info("Table '%s' created successfully or already exists".formatted(TABLE_NAME[2]));
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while DisasterData| %s \n".formatted(getStackTraceAsString(e)));
        }
    }

    public void insertData(int TableNo, Map<String, Object> data) {
        Set<String> columns = data.keySet();
        String columnsString = String.join(", ", columns);
        String placeholders = String.join(", ", columns.stream().map(col -> "?").toArray(String[]::new));
        String insertSQL = "INSERT INTO %s(".formatted(TABLE_NAME[TableNo]) + columnsString + ") VALUES (" + placeholders + ")";
            try (PreparedStatement pStmt = con.prepareStatement(insertSQL)) {
                int index = 1;
                for (String column : columns) {
                    Object value = data.get(column);
                    if (value instanceof String) {
                        pStmt.setString(index, ((String) value).toLowerCase());
                    } else if (value instanceof Integer) {
                        pStmt.setInt(index, (Integer) value);
                    } else if (value instanceof Boolean) {
                        pStmt.setBoolean(index, (Boolean) value);
                    } else if (value instanceof java.sql.Timestamp) {
                        pStmt.setTimestamp(index, (java.sql.Timestamp) value);
                    } else {
                        pStmt.setObject(index, value);
                    }
                    index++;
                }
                pStmt.executeUpdate();
                System.out.println("Data inserted into userdata successfully!");

        } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Error in Database.java: |SQLException while insertUserData| %s \n".formatted(getStackTraceAsString(e)));
        }
    }
    public boolean checkForDuplicateEntries(Map<String, Object> data) {
        String disasterType = (String) data.get("disasterType");
        String disasterName = (String) data.get("disasterName");
        LocalDate startDate = (LocalDate) data.get("startDate");

        String checkSQL = "SELECT COUNT(*) FROM %s WHERE disasterType = ? AND disasterName = ? AND startDate = ?".formatted(TABLE_NAME[2]);

        try (PreparedStatement checkStmt = con.prepareStatement(checkSQL)) {
            checkStmt.setString(1, disasterType.toLowerCase());
            checkStmt.setString(2, disasterName.toLowerCase());
            checkStmt.setDate(3, java.sql.Date.valueOf(startDate));

            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error in Database.java: |SQLException while checkForDuplicateEntries| %s \n".formatted(getStackTraceAsString(e)));
        }
        return false;
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
//    public Map<String, Object> getDisasterDetails() {
//    }
//    public void setDisasterDetails() {
//    }
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
