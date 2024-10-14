package org.burnknuckle.ui.subParts;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static org.burnknuckle.utils.Resources.eyeClosedIcon;
import static org.burnknuckle.utils.Resources.eyeOpenIcon;

public class PasswordFieldWithToggle extends JPanel {
    private JPasswordField passwordField;
    private JButton toggleButton;
    private boolean isPasswordVisible;

    public PasswordFieldWithToggle() {
        setLayout(new BorderLayout());
        UIManager.put("PasswordField.showRevealButton", false);
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(400, 30));

        toggleButton = new JButton();
        toggleButton.setOpaque(true);
        toggleButton.setPreferredSize(new Dimension(30, 30));
        updateToggleIcon();

        toggleButton.addActionListener(e -> {
            isPasswordVisible = !isPasswordVisible;
            passwordField.setEchoChar(isPasswordVisible ? '\0' : '*');
            updateToggleIcon();
        });
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == ' ') {
                    e.consume();
                }
            }
        });
          JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.add(passwordField, BorderLayout.CENTER);
        fieldPanel.add(toggleButton, BorderLayout.EAST);
        fieldPanel.setBorder(passwordField.getBorder());
        add(fieldPanel, BorderLayout.CENTER);
    }

    private void updateToggleIcon() {
        if (isPasswordVisible) {
            toggleButton.setIcon(eyeOpenIcon);
        } else {
            toggleButton.setIcon(eyeClosedIcon);
        }
    }
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    public void setPassword(String password){
        passwordField.setText(password);
    }
    public JPasswordField getPasswordField(){
        return this.passwordField;
    }
    public void setPlaceholder(String placeholder) {
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);

    }
}

