package org.burnknuckle.utils;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.burnknuckle.Main.logger;

public class LoginUtils {
    public static String UserCredentialsCheck(String username, String password){
        Database db = Database.getInstance();
        db.connectDatabase();
        Map<String, Object> data = db.getUsernameDetails(username);
        if (data.isEmpty()){
            logger.warn("No user found with username ");
            return "no user";
        }
        String checkPassword = (String) data.get("password");
        String checkPrivilege = (String) data.get("privilege");
        db.closeConnection();

        if (Objects.equals(checkPassword, password)){
            if (checkPrivilege.equals("admin")){
//                logger.warn("He is admin");
                return "admin";
            }
            return "user";
        } else {
            return "wrong password";
        }
    }
    public static String SignUpUser(Map<String, Object> userdata){
        Database db = Database.getInstance();
        db.connectDatabase();
        Map<String, Object> inData = db.getUsernameDetails(userdata.get("username").toString());
        if (!inData.isEmpty()){
            return "user exist";
        }
        String username = userdata.get("username").toString();
        String password = userdata.get("password").toString();
        String privilege = userdata.get("privilege").toString();
        String email = userdata.get("email").toString();
        String gender = userdata.get("gender").toString();
        String role = userdata.get("role").toString();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Map<String, Object> outData= new HashMap<>();
        outData.put("username", username);
        outData.put("password", password);
        outData.put("privilege", privilege);
        outData.put("email", email);
        outData.put("gender", gender);
        outData.put("role", role);
        outData.put("account_created", currentTime);
        outData.put("last_login", currentTime);

        db.insertData(0, outData);
        db.closeConnection();
        return "done";
    }
    public static void updatePanelSizes(Dimension newSize, JPanel fullscreen, JPanel center, JPanel bgFullscreen, JLayeredPane MainPane, String currentPage) {
        int lxPos = 0, lyPos = 0;
        int width = 0, height = 0;
        if (currentPage.equals("login")){
            height = 521;
            width = 457;
            lxPos = (newSize.width - width) / 2;
            lyPos = (newSize.height - height) / 2;
        } else if (currentPage.equals("signup")){
            height = 721;
            width = 857;
            lxPos = (newSize.width - width) / 2;
            lyPos = (newSize.height - height) / 2;
        }
        if (width != 0 && height != 0 && lxPos != 0 && lyPos != 0){
            fullscreen.setBounds(0, 0, newSize.width, newSize.height);
            center.setBounds(lxPos, lyPos, width, height);
        }
        if (bgFullscreen != null)  bgFullscreen.setBounds(0, 0, newSize.width, newSize.height);
        if (!newSize.equals(center.getSize())) {
            MainPane.revalidate();
            MainPane.repaint();
        }
    }
}
