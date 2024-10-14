package org.burnknuckle.utils;

import java.util.Map;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;

public class Userdata {
    public static String GlobalUsername ;
    public static Map<String, Object> userdata;
    public static String getUsername(){
        return GlobalUsername;
    }
    public static void setUsername(String name){
        GlobalUsername = name;
    }
    public static void getUserDataFromDatabase() {
        try {
            Database db = Database.getInstance();
            db.getConnection();
            userdata = db.getUsernameDetails(GlobalUsername);
        } catch (Exception e){
            logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
        }
    }

}
