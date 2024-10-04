package org.burnknuckle.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.*;
import java.util.function.Consumer;

import static org.burnknuckle.controllers.Main.logger;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;


public class ThemeManager {
    private static final java.util.List<Consumer<String>> themeChangeListeners = new ArrayList<>();
    public static String currentTheme = readTheme();
    private static String lastTheme = currentTheme;
    public static Map<String, String> ADPThemeData = UpdateADPThemeData(currentTheme); // Admin DashBoard Color
    static {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkForThemeChange();
            }
        }, 0, 500);
    }
    public static Map<String, String> UpdateADPThemeData(String newTheme){
        ADPThemeData = new TreeMap<>();
        if (Objects.equals(newTheme, "dark")) {
            ADPThemeData.put("sidebar", "#4f4f4f");
            ADPThemeData.put("background", "#5d5d5d");
            ADPThemeData.put("TabBackground", "#262626");
            ADPThemeData.put("splitter", "#f6f6f6");
            ADPThemeData.put("overlay", "#363636cc");
            ADPThemeData.put("TabTitleBg", "#474747");
            ADPThemeData.put("TabTitleSelected", "#4f4f4f");
            ADPThemeData.put("TabTitleTextColorSelected", "#6D8FFF");
            ADPThemeData.put("TabTitleTextColorNormal", "#FFFFFF");

            ADPThemeData.put("default-menu-button", "#4f4f4f");
            ADPThemeData.put("hover-menu-button", "#6d6d6d");
            ADPThemeData.put("active-menu-button", "#4f4f4f");
            ADPThemeData.put("sidebar-default-menu-button", "#4f4f4f");
            ADPThemeData.put("text", "#f6f6f6");

            // UserDashBoardPanel
//            ADPThemeData.put("", "#f6f6f6");

        } else if (Objects.equals(newTheme, "light")) {
            ADPThemeData.put("sidebar", "#e0e0e0");
            ADPThemeData.put("background", "#656565");
            ADPThemeData.put("TabBackground", "#eeeeee");
            ADPThemeData.put("TabTitleBg", "#4f4f4f");
            ADPThemeData.put("TabTitleSelected", "#4f4f4f");
            ADPThemeData.put("TabTitleTextColorSelected", "#0661ff");
            ADPThemeData.put("TabTitleTextColorNormal", "#edf7ff");
            ADPThemeData.put("spliter", "#cccccc");
            ADPThemeData.put("overlay", "#989898cc");
            ADPThemeData.put("default-menu-button", "#f0f0f0");
            ADPThemeData.put("hover-menu-button", "#e0e0e0");
            ADPThemeData.put("active-menu-button", "#d0d0d0");
            ADPThemeData.put("sidebar-default-menu-button", "#e0e0e0");
            ADPThemeData.put("text", "#333333");

        } else {
            ADPThemeData.put("sidebar", "#e0e0e0");
            ADPThemeData.put("background", "#ffffff");
            ADPThemeData.put("text", "#000000");
        }
        return ADPThemeData;
    }
    public static Color getColorFromHex(String hex) {
        if (hex.length() == 9) {
            int r = Integer.parseInt(hex.substring(1, 3), 16);
            int g = Integer.parseInt(hex.substring(3, 5), 16);
            int b = Integer.parseInt(hex.substring(5, 7), 16);
            int a = Integer.parseInt(hex.substring(7, 9), 16);
            return new Color(r, g, b, a);
        } else {
            return Color.decode(hex);
        }
    }
    public static void addThemeChangeListener(Consumer<String> listener) {
        themeChangeListeners.add(listener);
    }

    private static void notifyThemeChangeListeners() {
        for (Consumer<String> listener : themeChangeListeners) {
            UpdateADPThemeData(currentTheme);
            listener.accept(currentTheme);
        }
    }
    private static void checkForThemeChange() {
        if (!lastTheme.equals(currentTheme)) {
            lastTheme = currentTheme;
            notifyThemeChangeListeners();
        }
    }
    public static void setButtonHoverAndActiveColors(JButton button, Map<String, String> themeColors, String component) {
        Color defaultColor;
        if (component.equals("sidebar")){
            defaultColor = getColorFromHex(themeColors.get("sidebar-default-menu-button"));
        } else if (component.equals("onBackGround")) {
            defaultColor = getColorFromHex(themeColors.get("background"));
        } else {
            defaultColor = getColorFromHex(themeColors.get("default-menu-button"));
        }
        Color hoverColor = getColorFromHex(themeColors.get("hover-menu-button"));
        Color activeColor = getColorFromHex(themeColors.get("active-menu-button"));
        button.setBackground(defaultColor);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(activeColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(hoverColor);
            }
        });
    }
    public static String readTheme() {
        Properties props = new Properties();
        String theme = "dark";

        try (FileInputStream input = new FileInputStream("config.properties")) {
            props.load(input);
            theme = props.getProperty("theme", "dark");
        } catch (FileNotFoundException e) {
            logger.warn("Warning in ThemeManager.java: [FileNotFoundException while reading properties] ");
        } catch (IOException e) {
            logger.error("Error in ThemeManager.java: [IOException while reading properties]: \n%s \n".formatted(getStackTraceAsString(e)));
        }
        return theme;
    }
    public static void writeTheme() {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            props.load(input);
        } catch (FileNotFoundException e) {
            logger.warn("Warning in ThemeManager.java: [FileNotFoundException in writeTheme] : Config file not found, a new one will be created");
        } catch (IOException e) {
            logger.error("Error in ThemeManager.java: [IOException while loading properties]: \n%s \n".formatted(getStackTraceAsString(e)));
        }
        props.setProperty("theme", (!currentTheme.isEmpty()) ? currentTheme : "dark");
        try (FileOutputStream output = new FileOutputStream("config.properties")) {
            props.store(output, null);
        } catch (FileNotFoundException e) {
            logger.error("Error in ThemeManager.java: [FileNotFoundException]: \n%s \n".formatted(getStackTraceAsString(e)));
        } catch (IOException e) {
            logger.error("Error in ThemeManager.java: [IOException while saving properties]: \n%s \n".formatted(getStackTraceAsString(e)));
        }
    }
}
