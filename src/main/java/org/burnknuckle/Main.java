package org.burnknuckle;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.burnknuckle.utils.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.burnknuckle.utils.MainUtils.*;
import static org.burnknuckle.utils.ThemeManager.currentTheme;
import static org.burnknuckle.utils.Userdata.getUsername;

public class Main {
    private static JFrame mainFrame;
    public static boolean rememberMeCheck = false;
    private static JFrame splashFrame;
    public static final Logger logger = LogManager.getLogger(Main.class.getName());

    public static void setIcon(JFrame frame) {
        FlatSVGIcon.ColorFilter colorFilter = FlatSVGIcon.ColorFilter.getInstance()
                .add(Color.black, Color.black, Color.white)
                .add(Color.white, Color.white, Color.black);
        FlatSVGIcon logoSvg = new FlatSVGIcon(Objects.requireNonNull(Main.class.getResource("/icons/logo.svg")));
        logoSvg.setColorFilter(colorFilter);
        frame.setIconImage(logoSvg.getImage());
    }

    private void showSplashScreen() {
        splashFrame = new JFrame();
        setIcon(splashFrame);
        splashFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
            protected Void doInBackground() throws InterruptedException {
                Thread.sleep(3000);
                initializeLoginSystem(mainFrame);
                return null;
            }
            @Override
            protected void done() {
                splashFrame.dispose();
                mainFrame.setVisible(true);
            }
        };
        worker.execute();
    }

    private static void setFont() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            InputStream spaceGrotesk = Main.class.getClassLoader().getResourceAsStream("fonts/Inter.ttf");
            InputStream inter = Main.class.getClassLoader().getResourceAsStream("fonts/SpaceGrotesk.ttf");
            if (spaceGrotesk != null) {
                Font spaceGroteskFont = Font.createFont(Font.TRUETYPE_FONT, spaceGrotesk);
                ge.registerFont(spaceGroteskFont);
            }
            if (inter != null) {
                Font interFont = Font.createFont(Font.TRUETYPE_FONT, inter);
                ge.registerFont(interFont);
            }
        } catch (Exception e) {
            logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
        }
    }
    public static void setUp() {
        setLoggerContext();
        SwingUtilities.invokeLater(() -> {
            try {
                setFont();
                Main resQit = new Main("ResQit");
                resQit.showSplashScreen();
                logger.info("App Loaded Successfully!");
            } catch (Exception e) {
                logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
            }
        });
    }

    public static void main(String[] args) {
        setUp();
        if (!Platform.isFxApplicationThread()) {
            Platform.startup(() -> {});
        }
    }

    public Main(String name) {
        try {
            if (Objects.equals(currentTheme, "dark")) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else if (Objects.equals(currentTheme, "light")) {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        } catch (UnsupportedLookAndFeelException ex) {
            logger.error("Error in: %s".formatted(getStackTraceAsString(ex)));
        }
        mainFrame = new JFrame();
        setIcon(mainFrame);
        mainFrame.setTitle(name);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                String username = getUsername();
                if (username != null){
                    Map<String, Object> lastLogin = new HashMap<>();
                    lastLogin.put("last_login",new Timestamp(System.currentTimeMillis()));
                    lastLogin.put("is_active",false);
                    try {
                        Database db = Database.getInstance();
                        db.connectDatabase();
                        db.updateData12(0, username,lastLogin);
                    } catch (Exception x){
                        logger.error("Error in: %s".formatted(getStackTraceAsString(x)));
                    }
                }
            }
        });
        mainFrame.setSize(new Dimension(1400, 900));
        mainFrame.setMinimumSize(new Dimension(1400, 900));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(false);
        addThemeSelectorMenu(mainFrame);
    }
}
