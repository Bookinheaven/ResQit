package org.burnknuckle.utils;

import java.util.Map;

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
            e.printStackTrace();
        }
    }

}
