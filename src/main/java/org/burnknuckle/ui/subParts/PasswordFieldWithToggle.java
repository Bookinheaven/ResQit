package org.burnknuckle.ui.subParts;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class PasswordFieldWithToggle extends JPanel {
    private JPasswordField passwordField;
    private JButton toggleButton;
    private boolean isPasswordVisible;
    private String password;
    private ImageIcon eyeOpenIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Common/eyeOpen.svg")));
    private ImageIcon eyeClosedIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Common/eyeClosed.svg")));


    public PasswordFieldWithToggle() {
        setLayout(new BorderLayout());

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
        this.password = password;
        passwordField.setText(password);
    }
    public JPasswordField getPasswordField(){
        return this.passwordField;
    }
    public void setPlaceholder(String placeholder) {
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);

    }
}

