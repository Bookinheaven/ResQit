package org.burnknuckle.ui;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.controllers.swing.LoginSystem.BtwLS;
import static org.burnknuckle.controllers.swing.LoginSystem.Login;
import static org.burnknuckle.utils.ThemeManager.*;

public class LoginPanel {
    private final JFrame frame;
    private final JLayeredPane MainPane;
    private JPanel loginOverlayPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox rememberMeCheckbox;
    private JButton loginButton;
    private JLabel warningLabel;
    private Graphics2D graphic;


    public LoginPanel(JFrame frame, JLayeredPane MainPane) {
        this.frame = frame;
        this.MainPane = MainPane;
        initialize();
    }

    private void initialize() {
        loginOverlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                graphic = g2d;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape roundedRectangle = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5, 5);
                g2d.setColor(getColorFromHex(ADPThemeData.get("loginPageRecBg")));

                g2d.fill(roundedRectangle);
                g2d.setStroke(new BasicStroke(0, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.draw(roundedRectangle);
            }
        };
        addThemeChangeListener(_ -> {
            if(graphic != null){
                graphic.setColor(getColorFromHex(ADPThemeData.get("loginPageRecBg")));
                loginOverlayPanel.setBorder(BorderFactory.createLineBorder(getColorFromHex(ADPThemeData.get("loginPageRecBorder"))));
            }
        });
        loginOverlayPanel.setOpaque(false);
        loginOverlayPanel.setBounds(0, 0, 457, 521);
        loginOverlayPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        loginOverlayPanel.setLayout(new GridBagLayout());
        loginOverlayPanel.setDoubleBuffered(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title Label
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Fira Code Retina", Font.PLAIN, 34));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginOverlayPanel.add(titleLabel, gbc);
        gbc.insets = new Insets(40,10,10,10);

        usernameField = new JTextField(25);
        usernameField.setFont(new Font("Fira Code Retina", Font.PLAIN, 20));
        usernameField.setPreferredSize(new Dimension(400, 45));
        usernameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");
        usernameField.putClientProperty("JTextField.showClearButton", true);
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == ' ') {
                    e.consume();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                validateFields();
            }
        });
        usernameField.setMargin(new Insets(10,10,10,10));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        loginOverlayPanel.add(usernameField, gbc);
        gbc.insets = new Insets(20,10,10,10);

        UIManager.put("PasswordField.showRevealButton", true);
        passwordField = new JPasswordField(25);

        passwordField.setFont(new Font("Fira Code Retina", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        passwordField.setPreferredSize(new Dimension(300, 45));
        passwordField.setMargin(new Insets(10,10,10,10));
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        passwordField.putClientProperty("PasswordField.showCapsLock", true);
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == ' ') {
                    e.consume();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                validateFields();
            }
        });
        loginOverlayPanel.add(passwordField, gbc);
        // Remember Me Checkbox
        rememberMeCheckbox = new JCheckBox("Remember me");
        rememberMeCheckbox.setFont(new Font("Fira Code Retina", Font.PLAIN, 12));
        rememberMeCheckbox.setForeground(Color.WHITE);
        rememberMeCheckbox.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginOverlayPanel.add(rememberMeCheckbox, gbc);

        // Forgot Password Label
        JLabel forgotPasswordLabel = new JLabel("Forgot password?");
        forgotPasswordLabel.setFont(new Font("Fira Code Retina", Font.PLAIN, 12));
        forgotPasswordLabel.setForeground(new Color(77, 193, 255));
        forgotPasswordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        loginOverlayPanel.add(forgotPasswordLabel, gbc);

        warningLabel = new JLabel();
        warningLabel.setFont(new Font("Fira Code Retina", Font.PLAIN, 14));
        warningLabel.setForeground(Color.RED);
        warningLabel.setVisible(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginOverlayPanel.add(warningLabel, gbc);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Fira Code Retina", Font.PLAIN, 27));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton.setMargin(new Insets(5,30,5,30));
        loginButton.setEnabled(false);
        ArrayList<Object> Listeners = new ArrayList<>(Arrays.asList(loginButton, usernameField, passwordField, rememberMeCheckbox));
        for (Object i : Listeners){
            if (i instanceof JComponent) {
                ((JComponent)i).addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            Login(frame, usernameField, passwordField, rememberMeCheckbox, loginButton, warningLabel);
                        }
                    }
                });

            }
        }
        loginButton.addActionListener(_ -> Login(frame, usernameField, passwordField, rememberMeCheckbox, loginButton, warningLabel));
        loginOverlayPanel.add(loginButton, gbc);

        // Sign Up Label
        JLabel signUpLabel = new JLabel("Sign Up?");
        signUpLabel.setFont(new Font("Fira Code Retina", Font.PLAIN, 18));
        signUpLabel.setForeground(Color.WHITE);
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        signUpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loginOverlayPanel.setVisible(false);
                BtwLS(frame, MainPane, "signup");
                MainPane.revalidate();
                MainPane.repaint();
            }
        });
        loginOverlayPanel.add(signUpLabel, gbc);
        MainPane.add(loginOverlayPanel, Integer.valueOf(2));
        loginOverlayPanel.setVisible(false);

    }
    private void validateFields() {
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();
        loginButton.setEnabled(username.length() >= 4 && password.length >= 3);
    }
    public JPanel getPanel() {
        return loginOverlayPanel;
    }
}
