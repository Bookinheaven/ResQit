package org.burnknuckle.controllers;

import org.burnknuckle.javafx.model.UserDashboardPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.utils.LoginUtils.SignUpUser;

public class SignUpSystem {
    public static void SignUp(JFrame frame, JTextField usernameField, JPasswordField passwordField, JPasswordField conformPasswordField, JTextField emailField, JComboBox<String> genderComboBox, JComboBox<String> roleComboBox, JLabel warningLabel) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(conformPasswordField.getPassword());
        String email = emailField.getText();
        String role = Objects.requireNonNull(roleComboBox.getSelectedItem()).toString();
        String gender = Objects.requireNonNull(genderComboBox.getSelectedItem()).toString();
        logger.info("%s %s %s %s %s %s".formatted(username, password, confirmPassword, email, role, gender));
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder();
        if (username.trim().isEmpty()) {
            errorMessage.append("Username cannot be empty.\n");
            isValid = false;
        }
        if (password.trim().isEmpty()) {
            errorMessage.append("Password cannot be empty.\n");
            isValid = false;
        }
        if (confirmPassword.trim().isEmpty()) {
            errorMessage.append("Confirm Password cannot be empty.\n");
            isValid = false;
        }
        if (email.trim().isEmpty()) {
            errorMessage.append("Email cannot be empty.\n");
            isValid = false;
        }
        if (!isValidEmail(email)) {
            errorMessage.append("Invalid email format.\n");
            isValid = false;
        }
        if (isValid) {
            warningLabel.setVisible(false);
            Map<String, Object> userdata = new TreeMap<>() {{
                put("username", username);
                put("password", password);
                put("privilege", "fxml/user");
                put("email", email);
                put("gender", gender);
                put("role", role);
            }};
            String out = SignUpUser(userdata);
            frame.getContentPane().removeAll();
            frame.revalidate();
            frame.repaint();
            new UserDashboardPanel(frame,userdata);

            logger.info(out);
            logger.info("Sign Up successful!");
        } else {
            showWarning(frame, errorMessage.toString(), warningLabel);
        }
    }
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    private static void showWarning(JFrame frame, String message, JLabel warningLabel) {
        warningLabel.setText(message);
        warningLabel.setVisible(true);
        frame.revalidate();
        frame.repaint();
        Timer timer = new Timer(3000, _ -> {
            warningLabel.setVisible(false);
            frame.revalidate();
            frame.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }
    public static void validateFields(JTextField usernameField, JPasswordField passwordField, JPasswordField conformPasswordField, JProgressBar usernameProgressBar, JProgressBar passwordProgressBar, JProgressBar conformPasswordProgressBar, JButton signUpButton) {
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();
        char[] confirmPassword = conformPasswordField.getPassword();
        usernameProgressBar.setValue(username.length());
        if (username.length() > 4) {
            usernameProgressBar.setForeground(new Color(0, 255, 0));
        } else {
            usernameProgressBar.setForeground(new Color(255, 0, 0));
        }
        int passwordStrength = getPasswordStrength(password);
        passwordProgressBar.setValue(passwordStrength);
        if (passwordStrength >= 85) {
            passwordProgressBar.setForeground(new Color(0, 128, 0));
        } else if (passwordStrength >= 75) {
            passwordProgressBar.setForeground(new Color(0, 255, 0));
        } else if (passwordStrength >= 60) {
            passwordProgressBar.setForeground(new Color(255, 165, 0));
        } else if (passwordStrength >= 50) {
            passwordProgressBar.setForeground(new Color(255, 255, 0));
        } else if (passwordStrength >= 25) {
            passwordProgressBar.setForeground(new Color(255, 69, 0));
        } else {
            passwordProgressBar.setForeground(new Color(255, 0, 0));
        }
        conformPasswordProgressBar.setValue(passwordStrength);
        if (Arrays.equals(password, confirmPassword)) {
            conformPasswordProgressBar.setForeground(new Color(0, 255, 0)); // Green for match
        } else if (confirmPassword.length < password.length) {
            conformPasswordProgressBar.setValue(0);
        } else {
            conformPasswordProgressBar.setForeground(new Color(255, 0, 0)); // Red for no match
        }
        boolean isUsernameValid = username.length() >= 4;
        boolean isPasswordValid = passwordStrength >= 50;
        boolean isConfirmPasswordValid = Arrays.equals(password, confirmPassword);
        signUpButton.setEnabled(isUsernameValid && isPasswordValid && isConfirmPasswordValid);
    }
    private static int getPasswordStrength(char[] password) {
        int strength = 0;
        if (password.length >= 8) strength += 25;
        if (hasUpperCase(password)) strength += 25;
        if (hasLowerCase(password)) strength += 25;
        if (hasDigit(password)) strength += 25;
        return strength;
    }
    private static boolean hasUpperCase(char[] password) {
        for (char c : password) {
            if (Character.isUpperCase(c)) return true;
        }
        return false;
    }
    private static boolean hasLowerCase(char[] password) {
        for (char c : password) {
            if (Character.isLowerCase(c)) return true;
        }
        return false;
    }
    private static boolean hasDigit(char[] password) {
        for (char c : password) {
            if (Character.isDigit(c)) return true;
        }
        return false;
    }
}