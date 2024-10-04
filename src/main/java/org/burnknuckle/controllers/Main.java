package org.burnknuckle.controllers;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static org.burnknuckle.model.ThemeManager.currentTheme;
import static org.burnknuckle.utils.MainUtils.*;

public class Main {
    private static JFrame mainFrame;
    public static boolean rememberMeCheck = false;
    private static JFrame splashFrame;
    public static final Logger logger = LogManager.getLogger(Main.class.getName());


    public static void setIcon(JFrame frame){
        FlatSVGIcon.ColorFilter colorFilter = FlatSVGIcon.ColorFilter.getInstance()
                .add(Color.black, Color.black, Color.white)
                .add(Color.white, Color.white, Color.black);

        FlatSVGIcon logoSvg = new FlatSVGIcon(Objects.requireNonNull(Main.class.getResource("/icons/logo.svg")));
        logoSvg.setColorFilter(colorFilter);
        frame.setIconImage(logoSvg.getImage());
    }

    private static void SplashScreenShow(){
        splashFrame = new JFrame();
        setIcon(splashFrame);
        splashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        splashFrame.setSize(600, 600);
        splashFrame.setLocationRelativeTo(null);
        splashFrame.setUndecorated(true);

        ImageIcon splashIcon = new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("Common/introImage.jpeg")));
        JLabel splashLabel = new JLabel(new ImageIcon(splashIcon.getImage().getScaledInstance(splashFrame.getWidth(), splashFrame.getHeight(), Image.SCALE_SMOOTH)));
        splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
        splashLabel.setVerticalAlignment(SwingConstants.CENTER);
        splashFrame.getContentPane().add(splashLabel);
        splashFrame.setVisible(true);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() { // 4000
                Timer timer = new Timer(500, _ -> initializeLoginSystem(mainFrame));
                timer.setRepeats(false);
                timer.start();
                return null;
            }
            @Override
            protected void done() {
                if (!splashFrame.isVisible()) {
                    mainFrame.setVisible(true);
                }
            }
        };
        worker.execute(); // 5000
        Timer timer = new Timer(500, _ -> {
            splashFrame.dispose();
            try {
                worker.get();
                mainFrame.setVisible(true);
            } catch (InterruptedException | ExecutionException ex) {
                logger.warn("SplashScreen Worker: %s".formatted(getStackTraceAsString(ex)));
            }
        });

        timer.setRepeats(false);
        timer.start();
    }
    private static void setFont(){
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            InputStream spaceGrotesk = Main.class.getClassLoader().getResourceAsStream("fonts/Inter.ttf");
            InputStream inter = Main.class.getClassLoader().getResourceAsStream("fonts/SpaceGrotesk.ttf");
            if(spaceGrotesk != null) {Font spaceGroteskFont = Font.createFont(Font.TRUETYPE_FONT, spaceGrotesk); ge.registerFont(spaceGroteskFont);}
            if(inter!=null) {Font interFont = Font.createFont(Font.TRUETYPE_FONT, inter);ge.registerFont(interFont);}
        } catch (Exception e) {
            logger.error("Error in Main.java: [setFont]: %s".formatted(getStackTraceAsString(e)));
        }
    }
    public static void main(String[] args) {
        setLoggerContext();
        SwingUtilities.invokeLater(() -> {
            try {
                setFont();
                Main window =  new Main();
                SplashScreenShow();
                logger.info("App Loaded Successfully!");
            } catch (Exception e) {
                logger.error("Error in Main.java: [Exception while invoking the worker] %s \n".formatted(getStackTraceAsString(e)));
            }
        });
    }
    public Main() {
        try {
            if (Objects.equals(currentTheme, "dark")){
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else if (Objects.equals(currentTheme, "light")) {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        } catch (UnsupportedLookAndFeelException ex) {
            logger.error("Error in Main.java: [UnsupportedLookAndFeelException]: %s ".formatted(ex));
        }
        mainFrame = new JFrame();
        setIcon(mainFrame);
        mainFrame.setTitle("ResQit");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(new Dimension(1400, 900));
        mainFrame.setMinimumSize(new Dimension(1400, 900));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(false);
        addThemeSelectorMenu(mainFrame);
    }

}
