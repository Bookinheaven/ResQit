package org.burnknuckle.controllers.swing;

import org.burnknuckle.ui.AdminDashboardPanel;
import org.burnknuckle.ui.LoginPanel;
import org.burnknuckle.ui.SignUpPanel;
import org.burnknuckle.ui.UserDashboardPanel;
import org.burnknuckle.ui.subParts.LoginBgPanel;
import org.burnknuckle.utils.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.utils.LoginUtils.UserCredentialsCheck;
import static org.burnknuckle.utils.LoginUtils.updatePanelSizes;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;
import static org.burnknuckle.utils.Userdata.setUsername;

public class LoginSystem {
    private final JFrame MainFrame;
    public static LoginPanel LoginPanel1;
    public static SignUpPanel signUpPanel1;
    private static JPanel CenterOverlayPanel;
    private JLayeredPane MainPane;
    private static LoginBgPanel LoginBgPanel;
    private static JPanel overlayPanel;
    public static String currentPage = "";

    public LoginSystem(JFrame frame) {
        this.MainFrame = frame;
        currentPage = "";
        initialize();
        frame.repaint();
        frame.revalidate();
    }
    private static void rememberMeSaver(String username, String password) {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            props.load(input);
        } catch (FileNotFoundException e) {
            logger.warn("Warning in LoginPanel.java: [FileNotFoundException in rememberMeSaver] : Config file not found, a new one will be created");
        } catch (IOException e) {
            logger.error("Error in LoginPanel.java: [IOException while loading properties]: \n%s \n".formatted(getStackTraceAsString(e)));
        }
        props.setProperty("us", username);
        props.setProperty("ps", password);
        props.setProperty("lastClearTime", String.valueOf(System.currentTimeMillis()));
        try (FileOutputStream output = new FileOutputStream("config.properties")) {
            props.store(output, "Properties updated");
        } catch (FileNotFoundException e) {
            logger.error("Error in LoginPanel.java: [FileNotFoundException]: \n%s \n".formatted(getStackTraceAsString(e)));
        } catch (IOException e) {
            logger.error("Error in LoginPanel.java: [IOException while saving properties]: \n%s \n".formatted(getStackTraceAsString(e)));
        }
    }

    public static JPanel BtwLS(JFrame MainFrame, JLayeredPane MainPane, String ChangeTo) {
        if (!currentPage.equals(ChangeTo)) {
            if (CenterOverlayPanel != null) CenterOverlayPanel.setVisible(false);
            if (ChangeTo.equals("login")) {
                CenterOverlayPanel = LoginPanel1.getPanel();
            } else if (ChangeTo.equals("signup")) {
                CenterOverlayPanel = signUpPanel1.getPanel();
            }
            if (CenterOverlayPanel == null) {
                logger.warn("Warning in LoginSystem.java: [BtwLS : CenterOverlayPanel] No Page");
                System.exit(0);
            }
            currentPage = ChangeTo;
            Dimension newSize = MainFrame.getSize();
            MainPane.setPreferredSize(newSize);
            MainPane.setBounds(0, 0, newSize.width, newSize.height);
            updatePanelSizes(newSize, overlayPanel, CenterOverlayPanel, LoginBgPanel, MainPane, currentPage);
            MainFrame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        Dimension newSize = MainFrame.getSize();
                        MainPane.setPreferredSize(newSize);
                        MainPane.setBounds(0, 0, newSize.width, newSize.height);
                        updatePanelSizes(newSize, overlayPanel, CenterOverlayPanel, LoginBgPanel, MainPane, currentPage);
                    });
                }
            });
            CenterOverlayPanel.setVisible(true);
        }
        return CenterOverlayPanel;
    }

    private void initialize() {
        MainPane = new JLayeredPane();
        overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape roundedRectangle = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setColor(new Color(54, 54, 54, 100));
                g2d.fill(roundedRectangle);
            }
        };
        overlayPanel.setOpaque(false);
        overlayPanel.setDoubleBuffered(true);
        overlayPanel.setBounds(0, 0, 1400, 900);

        SwingWorker<Void, Void> bgLoader = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                LoginBgPanel = new LoginBgPanel("Common/loginBg.jpg");
                LoginBgPanel.setBounds(0, 0, MainFrame.getWidth(), MainFrame.getHeight());
                return null;
            }

            @Override
            protected void done() {
                MainPane.add(LoginBgPanel, Integer.valueOf(0));
                MainPane.add(overlayPanel, Integer.valueOf(1));
                MainFrame.setContentPane(MainPane);
                MainFrame.setVisible(true);
            }
        };
        LoginPanel1 = new LoginPanel(MainFrame, MainPane);
        signUpPanel1 = new SignUpPanel(MainFrame, MainPane);
        bgLoader.execute();
        CenterOverlayPanel = BtwLS(MainFrame, MainPane, "login");
        CenterOverlayPanel.setVisible(true);
    }

    public static void Login(JFrame frame, JTextField usernameField, JPasswordField passwordField, JCheckBox rememberMeCheckbox, JButton loginButton, JLabel warningLabel) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        boolean check = rememberMeCheckbox.isSelected();
        if (username.isEmpty() || password.isEmpty()) {
            return;
        }
        loginButton.setEnabled(username.length() > 3 && password.length() > 3);
        String status = UserCredentialsCheck(username, password);
        Map<String, Object> userdata = Map.of();
        if (status.equals("admin") || status.equals("user") || status.equals("vol")) {
            Database db = Database.getInstance();
            db.connectDatabase();
            userdata = db.getUsernameDetails(username);
            Map<String, Object> lastLogin = new HashMap<>();
            setUsername(username);
            lastLogin.put("last_login",new Timestamp(System.currentTimeMillis()));
            lastLogin.put("is_active",true);
            try {
                db.updateData12(0, username,lastLogin);
            } catch (Exception e){
                logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
            }
        }
        Map<String, Object> finalUserdata = userdata;
        switch (status) {
            case "admin" -> {
                frame.getContentPane().removeAll();
                frame.revalidate();
                frame.repaint();
                if (check) rememberMeSaver(username, password);
                new AdminDashboardPanel(frame);
                logger.info("Admin Login successful!");
            }
            case "no user" -> showWarning(frame, "No User Found!", warningLabel);
            case "user", "vol" -> {
                frame.getContentPane().removeAll();
                frame.revalidate();
                frame.repaint();
                if (check) rememberMeSaver(username, password);
                new UserDashboardPanel(frame);
                frame.revalidate();
                frame.repaint();
                logger.info("User Login successful!");
            }
            case "wrong password" -> showWarning(frame, "Incorrect Username or Password!", warningLabel);
        }
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
}
