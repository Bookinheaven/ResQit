package org.burnknuckle.utils;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.burnknuckle.controllers.LoginSystem;
import org.burnknuckle.Main;
import org.burnknuckle.ui.UserDashboardPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.burnknuckle.ui.AdminDashboardPanel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static org.burnknuckle.Main.*;
import static org.burnknuckle.utils.ThemeManager.currentTheme;
import static org.burnknuckle.utils.ThemeManager.writeTheme;
import static org.burnknuckle.utils.LoginUtils.UserCredentialsCheck;
import static org.burnknuckle.utils.Userdata.setUsername;

public class MainUtils {
    private static final long ONE_WEEK_IN_MILLIS = 7 * 24 * 60 * 60 * 1000L;
    public static void clearProperties(Properties props) {
        rememberMeCheck = false;
        props.remove("us");
        props.remove("ps");
        props.remove("lastClearTime");
        try (FileOutputStream output = new FileOutputStream("config.properties")) {
            props.store(output, null);
            logger.info("Properties Cleared!");

        } catch (IOException e) {
            logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
        }
    }
    public static void setLoggerContext(){
        try {
            URL resourceUrl = Main.class.getResource("/log4j.xml");
            if (resourceUrl == null) {
                System.err.println("Configuration file not found in the classpath: /log4j.xml");
                return;
            }
            URI configUri = resourceUrl.toURI();
            File configFile = Paths.get(configUri).toFile();

            if (configFile.exists() && configFile.isFile()) {
                LoggerContext context = (LoggerContext) LogManager.getContext(false);
                context.setConfigLocation(configUri);
                context.reconfigure();
                System.out.println("Log4j configuration loaded successfully from: " + configUri);
            } else {
                System.err.println("Configuration file not found at: " + configFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Error occurred while loading Log4j configuration: " + e.getMessage() + "\n" +  getStackTraceAsString(e));
        }
    }

    public static void RememberMeReader(JFrame mainFrame) {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            props.load(input);
            long lastClearTime = Long.parseLong(props.getProperty("lastClearTime", "0"));
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastClearTime > ONE_WEEK_IN_MILLIS) {
                clearProperties(props);
            } else {
                String username = props.getProperty("us");
                String password = props.getProperty("ps");
                if(username == null|| password == null) {
                    rememberMeCheck = false;
                    return;
                }
                setUsername(username);
                String status = UserCredentialsCheck(username, password);
                Database db = Database.getInstance();
                db.connectDatabase();
                Map<String, Object> userdata = db.getUsernameDetails(username);
                db.closeConnection();
                switch (status) {
                    case "admin", "co-admin" -> {
                        rememberMeCheck = true;
                        new AdminDashboardPanel(mainFrame);
                        logger.info("Admin Login successful!");
                    }
                    case "user", "vol" -> {
                        rememberMeCheck = true;
                        new UserDashboardPanel(mainFrame);
                        logger.info("User Login successful!");
                    }
                }
                logger.info("Username: %s Password: %s".formatted(username,password));
            }

        } catch (FileNotFoundException e) {
            logger.warn("Warning in MainUtils.java: [FileNotFoundException] while reading in RememberMeReader");
        } catch (IOException e) {
            logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
        }
    }
    public static String getStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    public static void initializeLoginSystem(JFrame mainFrame) {
        RememberMeReader(mainFrame);
        if (!rememberMeCheck) { LoginSystem _ = new LoginSystem(mainFrame);}
    }
    public static void addThemeSelectorMenu(JFrame mainFrame) {
        JMenuBar menuBar = new JMenuBar();
        JMenu themeMenu = new JMenu();
        themeMenu.setToolTipText("Theme");

        FlatSVGIcon settingsIcon = new FlatSVGIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("Common/settings.svg")));
        FlatSVGIcon darkThemeIcon = new FlatSVGIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("Common/dark_mode.svg")));
        FlatSVGIcon lightThemeIcon = new FlatSVGIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("Common/light_mode.svg")));
        themeMenu.setIcon(settingsIcon);
        FlatSVGIcon.ColorFilter colorFilter = FlatSVGIcon.ColorFilter.getInstance()
                .add(Color.black, Color.black, Color.white)
                .add(Color.white, Color.white, Color.black);

        settingsIcon.setColorFilter(colorFilter);
        darkThemeIcon.setColorFilter(colorFilter);
        lightThemeIcon.setColorFilter(colorFilter);

        JMenuItem darkTheme = new JMenuItem("Dark Theme");
        darkTheme.setIcon(darkThemeIcon);
        JMenuItem lightTheme = new JMenuItem("Light Theme");
        lightTheme.setIcon(lightThemeIcon);


        darkTheme.addActionListener(_ -> {
            try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                currentTheme = "dark";
                writeTheme();
                setIcon(mainFrame);
                SwingUtilities.updateComponentTreeUI(mainFrame);
            } catch (UnsupportedLookAndFeelException ex) {
                logger.error("Error in Main.java: [UnsupportedLookAndFeelException while setting the theme 'dark'] %s \n".formatted(getStackTraceAsString(ex)));
            }
        });
        lightTheme.addActionListener(_ -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
                currentTheme = "light";
                writeTheme();
                setIcon(mainFrame);
                SwingUtilities.updateComponentTreeUI(mainFrame);
            } catch (UnsupportedLookAndFeelException ex) {
                logger.error("Error in Main.java: [UnsupportedLookAndFeelException while setting the theme 'light'] %s \n".formatted(getStackTraceAsString(ex)));
            }
        });
        themeMenu.add(darkTheme);
        themeMenu.add(lightTheme);
        menuBar.add(themeMenu);
        mainFrame.setJMenuBar(menuBar);
    }


}
