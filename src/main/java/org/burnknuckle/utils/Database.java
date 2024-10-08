package org.burnknuckle.utils;

import java.sql.Date;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static org.burnknuckle.Main.logger;
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
                "privilege VARCHAR(10) NOT NULL, " +
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
                "FOREIGN KEY (privilege) REFERENCES %s (privilege) ".formatted(TABLE_NAME[0]) +
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
    private Timestamp convertStringToTimestamp(String dateString) {
        if (dateString == null || dateString.equalsIgnoreCase("null")) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return new Timestamp(dateFormat.parse(dateString).getTime());
        } catch (ParseException e) {
            logger.error("Error parsing date: {} - {}", dateString, e.getMessage());
            return null;
        }
    }

    public void updateData(int TableNo, Map<String, Object> data) {
        Object idObj = data.get("id");
        if (idObj != null) {
            data.remove("id");
        }
        Set<String> columns = data.keySet();
        String setClause = String.join(", ", columns.stream().map(col -> col + " = ?").toArray(String[]::new));
        String updateSQL = "UPDATE %s SET %s WHERE id = ?".formatted(TABLE_NAME[TableNo], setClause);
        try (PreparedStatement pStmt = con.prepareStatement(updateSQL)) {
            int index = 1;
            for (String column : columns) {
                Object value = data.get(column.toLowerCase());
                switch (value) {
                    case String s -> {
                        String strValue = s.toLowerCase();
                        if (column.equalsIgnoreCase("startdate") || column.equalsIgnoreCase("enddate")) {
                            if (strValue.isEmpty()) {
                                pStmt.setNull(index, Types.TIMESTAMP);
                            } else {
                                pStmt.setTimestamp(index, convertStringToTimestamp(strValue));
                            }
                        } else {
                            pStmt.setString(index, (String) value);
                        }
                    }
                    case Integer i -> pStmt.setInt(index, i);
                    case Boolean b -> pStmt.setBoolean(index, b);
                    case Timestamp timestamp -> pStmt.setTimestamp(index, timestamp);
                    case null, default -> pStmt.setObject(index, value);
                }
                index++;
            }
            if (idObj instanceof Integer) {
                pStmt.setInt(index, (Integer) idObj);
            } else if (idObj instanceof String) {
                pStmt.setInt(index, Integer.parseInt((String) idObj));
            } else {
                logger.info("Error in Database.java: [updateData] ID is of an unknown type: {}", Objects.requireNonNull(idObj).getClass().getName());
            }
            pStmt.executeUpdate();
            logger.info("Data updated successfully!");
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while updateData| %s \n".formatted(getStackTraceAsString(e)));
        }
    }
    public void insertData(int TableNo, Map<String, Object> data) {
        Set<String> columns = data.keySet();
        String columnsString = String.join(", ", columns);
        String placeholders = String.join(", ", columns.stream().map(_ -> "?").toArray(String[]::new));
        String insertSQL = "INSERT INTO %s(".formatted(TABLE_NAME[TableNo]) + columnsString + ") VALUES (" + placeholders + ")";

        try (PreparedStatement pStmt = con.prepareStatement(insertSQL)) {
            int index = 1;
            for (String column : columns) {
                Object value = data.get(column.toLowerCase());
                switch (value) {
                    case String s -> {
                        String strValue = s.toLowerCase();

                        if (column.equalsIgnoreCase("startdate") || column.equalsIgnoreCase("enddate")) {
                            if (strValue.isEmpty()) {
                                pStmt.setNull(index, Types.TIMESTAMP);
                            } else {
                                pStmt.setTimestamp(index, convertStringToTimestamp(strValue));
                            }
                        } else if (column.equalsIgnoreCase("id")) {
                            try {
                                int idValue = Integer.parseInt(strValue);
                                pStmt.setInt(index, idValue);
                            } catch (NumberFormatException e) {
                                logger.error("Error in Database.java: [insertData] Invalid id value: {} - {}", strValue, e.getMessage());
                                pStmt.setNull(index, Types.INTEGER);
                            }
                        } else {
                            pStmt.setString(index, strValue);
                        }
                    }
                    case Integer i -> pStmt.setInt(index, i);
                    case Boolean b -> pStmt.setBoolean(index, b);
                    case Timestamp timestamp -> pStmt.setTimestamp(index, timestamp);
                    case null, default -> pStmt.setObject(index, value);
                }
                index++;
            }
            pStmt.executeUpdate();
//            logger.info("Data inserted into userdata successfully!");
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while insertUserData| %s \n".formatted(getStackTraceAsString(e)));
        }
    }

    public void deleteData(int TableNo, String columnName, Object value) {
        columnName = columnName.toLowerCase();
        String deleteSQL = "DELETE FROM %s WHERE %s = ?".formatted(TABLE_NAME[TableNo], columnName);

        try (PreparedStatement pStmt = con.prepareStatement(deleteSQL)) {
            if(columnName.equals("id")){
                if (value instanceof Integer) {
                    pStmt.setInt(1, (Integer) value);
                } else if (value instanceof String) {
                    pStmt.setInt(1, Integer.parseInt((String) value));
                } else {
                    logger.info("Error in Database.java: [deleteData] ID is of an unknown type: {}", value.getClass().getName());
                }
            } else {
                switch (value) {
                    case String s -> pStmt.setString(1, s.toLowerCase());
                    case Integer i -> pStmt.setInt(1, i);
                    case Boolean b -> pStmt.setBoolean(1, b);
                    case Timestamp timestamp -> pStmt.setTimestamp(1, timestamp);
                    case null, default -> pStmt.setObject(1, value);
                }
            }
            int rowsAffected = pStmt.executeUpdate();
            logger.info("Successfully deleted %d row(s) from table: %s\n".formatted(rowsAffected, TABLE_NAME[TableNo]));

        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while deleteData| %s \n".formatted(getStackTraceAsString(e)));
        }
    }

    public List<Map<String, Object>> getData(int TableNo, String parameters) {
        List<Map<String, Object>> data = new ArrayList<>();
        String getSQL;

        if (!parameters.isEmpty()) {
            Set<String> columns = new HashSet<>(Arrays.asList(parameters.trim().split(" ")));
            String columnsString = String.join(", ", columns);
            getSQL = "SELECT " + columnsString + " FROM %s;".formatted(TABLE_NAME[TableNo]);
        } else {
            getSQL = "SELECT * FROM %s;".formatted(TABLE_NAME[TableNo]);
        }
        try (PreparedStatement pStmt = con.prepareStatement(getSQL);
             ResultSet rs = pStmt.executeQuery()) {

            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            while (rs.next()) {
                Map<String, Object> cell = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rsMetaData.getColumnName(i);
                    Object columnValue = rs.getObject(columnName);
                    cell.put(columnName, columnValue);
                }
                data.add(cell);
            }
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while retrieving data| %s \n".formatted(getStackTraceAsString(e)));
        }
        return data;
    }
    // for disaster database
    public boolean checkForDuplicateEntries(Map<String, Object> data) {
        String disasterType = (String) data.get("disastertype");
        String disasterName = (String) data.get("disastername");
        LocalDate startDate = (LocalDate) data.get("startdate");

        String checkSQL = "SELECT COUNT(*) FROM %s WHERE disastertype = ? AND disastername = ? AND startdate = ?".formatted(TABLE_NAME[2]);

        try (PreparedStatement checkStmt = con.prepareStatement(checkSQL)) {
            checkStmt.setString(1, disasterType.toLowerCase());
            checkStmt.setString(2, disasterName.toLowerCase());
            checkStmt.setDate(3, Date.valueOf(startDate));

            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while checkForDuplicateEntries| %s \n".formatted(getStackTraceAsString(e)));
        }
        return false;
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
    public void updateData12(int TableNo, String username, Map<String, Object> data) {
        Set<String> columns = data.keySet();
        String setClause = String.join(", ", columns.stream().map(col -> col + " = ?").toArray(String[]::new));
        String updateSQL = "UPDATE %s SET %s WHERE username = ?".formatted(TABLE_NAME[TableNo], setClause);
        try (PreparedStatement pStmt = con.prepareStatement(updateSQL)) {
            int index = 1;
            for (String column : columns) {
                Object value = data.get(column.toLowerCase());
                switch (value) {
                    case String s -> pStmt.setString(index, s);
                    case Integer i -> pStmt.setInt(index, i);
                    case Timestamp t -> pStmt.setTimestamp(index, t);
                    case null -> pStmt.setNull(index, Types.INTEGER);
                    default -> logger.error("Error in Database.java: [updateData12]: Type not found %s".formatted(value.toString()));
                }
            }
            pStmt.setString(index+1, username);
            pStmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error in Database.java: |SQLException while updateUserPrivilege| %s \n".formatted(getStackTraceAsString(e)));
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

}
