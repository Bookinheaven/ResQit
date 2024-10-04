package org.burnknuckle.utils;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;

import static org.burnknuckle.controllers.Main.logger;

public class LoginUtils {
    public static String UserCredentialsCheck(String username, String password){
        Database db = new Database();
        db.connectDatabase();
        Map<String, Object> data = db.getUsernameDetails(username);
        if (data.isEmpty()){
            logger.warn("No user found with username ");
            return "no user";
        }
        String checkPassword = (String) data.get("password");
        String checkPrivilege = (String) data.get("privilege");
        db.closeConnection();
        System.out.println(checkPassword+ checkPrivilege);

        if (Objects.equals(checkPassword, password)){
            if (checkPrivilege.equals("admin")){
                logger.warn("He is admin");
                return "admin";
            }
            return "fxml/user";
        } else {
            return "wrong password";
        }
    }
    public static String SignUpUser(Map<String, Object> userdata){
        Database db = new Database();
        db.connectDatabase();
        Map<String, Object> data = db.getUsernameDetails(userdata.get("username").toString());
        if (!data.isEmpty()){
            return "user exist";
        }
        String username = userdata.get("username").toString();
        String password = userdata.get("password").toString();
        String privilege = userdata.get("privilege").toString();
        String email = userdata.get("email").toString();
        String gender = userdata.get("gender").toString();
        String role = userdata.get("role").toString();

        db.insertUserData(username, password,privilege ,email, gender, role);
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
