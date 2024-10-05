package org.burnknuckle.javafx.model;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.burnknuckle.controllers.LoginSystem;
import org.burnknuckle.model.ThemeManager;
import org.burnknuckle.ui.subParts.LoadingPageController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;

import static org.burnknuckle.controllers.Main.logger;
import static org.burnknuckle.model.ThemeManager.*;
import static org.burnknuckle.utils.MainUtils.*;

public class UserDashboardPanel {
    private final JFrame frame;
    private JPanel menuBar;
    private JPanel dashSpace;
    private CardLayout cardLayout;
    private JPanel mainContent;
    private JLabel homeLabel;
    private JLabel disasterReqLabel;
    private JLabel accountLabel;
    private JLabel backOptionLabel;
    private JLabel logOut;
    private JPanel fakeSpace;
    private JPanel fakeSpace1;
    private String currentPage = "HomePage";
    private final Deque<String> pageStack = new LinkedList<>();
    private static final int MAX_PAGES = 5;
    private String priPage = "HomePage";
    private String subPage = "";
    private LoadingPageController loadingController;
    public static String currentUser;
    private JLabel resRequestLabel;
    private JLabel disasterAddReqMainLabel;
    private JLabel tasksLabel;

    public UserDashboardPanel(JFrame frame, Map<String, Object> userdata) {
        this.frame = frame;
        currentUser = (String) userdata.get("username");
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.add(createUserDashboard(),BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logger.info(pageStack.toString());
            }
        });
    }
    private void updateThemeColors(String change){
        menuBar.setBackground(getColorFromHex(ADPThemeData.get("sidebar")));
        changeSelectionMenu();
        accountLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        logOut.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        fakeSpace.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        fakeSpace1.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        resRequestLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        disasterAddReqMainLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        tasksLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        backOptionLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
    }
    private JPanel createUserDashboard() {
        JPanel block = new JPanel();
        block.setLayout(new BorderLayout());
        block.setBackground(Color.BLUE);
        menuBar = new JPanel();
        dashSpace = new JPanel();
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);

        menuBar.setPreferredSize(new Dimension(55, this.frame.getHeight()));
        menuBar.setBackground(getColorFromHex(ADPThemeData.get("sidebar")));

        Icon homePageIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/home.svg")));
        Icon requestPageIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("UserDashboardPanel/request.svg")));
        Icon UserIconDefault = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/account.svg")));
        Icon logOutIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/log-out.svg")));
        Icon backOptionIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("UserDashboardPanel/back-option.svg")));

        homeLabel = new JLabel();
        homeLabel.setIcon(homePageIcon);
        homeLabel.setOpaque(true);
        homeLabel.setToolTipText("Home");
        homeLabel.setPreferredSize(new Dimension(55,55));
        homeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        homeLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));

        disasterReqLabel = new JLabel();
        disasterReqLabel.setIcon(requestPageIcon);
        disasterReqLabel.setToolTipText("Request");
        disasterReqLabel.setHorizontalAlignment(SwingConstants.CENTER);
        disasterReqLabel.setPreferredSize(new Dimension(55,55));
        disasterReqLabel.setOpaque(true);
        disasterReqLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));

        accountLabel = new JLabel();
        accountLabel.setIcon(UserIconDefault);
        accountLabel.setToolTipText("Account");
        accountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        accountLabel.setPreferredSize(new Dimension(55,55));
        accountLabel.setOpaque(true);
        accountLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));

        backOptionLabel = new JLabel();
        backOptionLabel.setIcon(backOptionIcon);
        backOptionLabel.setToolTipText("Back");
        backOptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backOptionLabel.setVisible(false);
        backOptionLabel.setPreferredSize(new Dimension(55,55));
        backOptionLabel.setOpaque(true);
        backOptionLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        addPages("HomePage");

        logOut = new JLabel();
        logOut.setIcon(logOutIcon);
        logOut.setToolTipText("Log out");
        logOut.setHorizontalAlignment(SwingConstants.CENTER);
        logOut.setPreferredSize(new Dimension(55,55));
        logOut.setOpaque(true);
        logOut.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));

        FlatSVGIcon.ColorFilter colorFilter = FlatSVGIcon.ColorFilter.getInstance()
                .add(Color.black, Color.black, Color.white)
                .add(Color.white, Color.white, Color.black);

        ((FlatSVGIcon) homeLabel.getIcon()).setColorFilter(colorFilter);
        ((FlatSVGIcon) disasterReqLabel.getIcon()).setColorFilter(colorFilter);
        menuBar.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.CENTER;

        fakeSpace = new JPanel();
        fakeSpace.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        fakeSpace.setPreferredSize(new Dimension(55,55));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0;
        menuBar.add(backOptionLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        menuBar.add(fakeSpace, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0;
        menuBar.add(homeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0;
        menuBar.add(disasterReqLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        fakeSpace1 = new JPanel();
        fakeSpace1.setPreferredSize(new Dimension(55,55));
        fakeSpace1.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        menuBar.add(fakeSpace1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        menuBar.add(accountLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        menuBar.add(logOut, gbc);

        homeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!currentPage.equals("HomePage")){
                    changePage("HomePage");
                    changeSelectionMenu();
                    switchTabs("HomePage", mainContent);
                    logger.info("Clicked HomePage");
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!currentPage.equals("HomePage")) homeLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));

            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!currentPage.equals("HomePage")) homeLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
            }
        });
        disasterReqLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!currentPage.equals("DisasterPage")) {
                    changePage("DisasterPage");
                    changeSelectionMenu();
                    switchTabs("DisasterPage", mainContent);
                    logger.info("Clicked DisasterPage");
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!currentPage.equals("DisasterPage")) disasterReqLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));

            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!currentPage.equals("DisasterPage")) disasterReqLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
            }
        });
        backOptionLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String back = goBack();
                    changePage(back);
                    changeSelectionMenu();
                    switchTabs(back, mainContent);
                    logger.info("Clicked backOption");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                backOptionLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));

            }
            @Override
            public void mouseEntered(MouseEvent e) {
                backOptionLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
            }
        });
        accountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!currentPage.equals("Account")) {
                    addPages("Account");
                    changePage("Account");
                    cardLayout.show(mainContent, "Account");
                    logger.info("Clicked Account");
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!currentPage.equals("Account")) accountLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!currentPage.equals("Account")) accountLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
            }
        });
        logOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to logout?",
                        "Logout Confirmation",
                        JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION) {
                    frame.dispose();
                    clearProperties(new Properties());
                    SwingUtilities.invokeLater(() -> {
                        currentPage = "";
                        priPage = "";
                        changeSubPage("");
                        JFrame mainFrame = new JFrame();
                        mainFrame.setTitle("ResQit");
                        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        mainFrame.setSize(new Dimension(1400, 900));
                        mainFrame.setMinimumSize(new Dimension(1400, 900));
                        mainFrame.setLocationRelativeTo(null);
                        mainFrame.setVisible(false);
                        addThemeSelectorMenu(mainFrame);
                        new LoginSystem(mainFrame);
                    });
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                logOut.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                logOut.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
            }
        });

        mainContent.add(createHomePage(), "HomePage");
        mainContent.add(createDisasterPage(), "DisasterPage");
        mainContent.add(AccountSubPage(), "Account");
        mainContent.add(createRequestResourcesSubPage(), "Request");
        mainContent.add(createDisasterSubPage(),"Disaster");
        cardLayout.show(mainContent, "HomePage");

        block.add(menuBar, BorderLayout.WEST);
        block.add(mainContent, BorderLayout.CENTER);
        addThemeChangeListener(this::updateThemeColors);
            return block;
    }
    private void switchTabs(String tabName, JPanel mainContent){
        cardLayout.show(mainContent, tabName);
    }

    private JFXPanel createHomePage() {
        JFXPanel homePage = new JFXPanel();
        dashSpace.setLayout(new BorderLayout());
        dashSpace.add(homePage, BorderLayout.CENTER);
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/user/homepage.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                homePage.setScene(scene);
            } catch (IOException e) {
                logger.error("Error in UserDashboardPanel.java: [IOException]: %s".formatted(getStackTraceAsString(e)));
            }
        });
        return homePage;
    }
    private JPanel createDisasterPage() {
        JPanel disasterPanel = new JPanel();
        disasterPanel.setLayout(new GridBagLayout());
        GridBagConstraints bgbc = new GridBagConstraints();
        bgbc.gridx = 0;
        bgbc.gridy = 0;
        bgbc.fill = GridBagConstraints.HORIZONTAL;
        bgbc.weighty = 1.0;
        bgbc.weightx = 1.0;
        bgbc.insets = new Insets(10,10,10,10);
        JPanel ResRequestPanel = new JPanel();
        resRequestLabel = new JLabel(new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("UserDashboardPanel/resRequest.svg"))));
        resRequestLabel.setPreferredSize(new Dimension(250,250));
        resRequestLabel.setOpaque(true);
        resRequestLabel.setText("Resource Request");
        resRequestLabel.setFont( new Font("Fira Code Retina",Font.PLAIN,20));
        resRequestLabel.setVerticalTextPosition(JLabel.BOTTOM);
        resRequestLabel.setHorizontalTextPosition(JLabel.CENTER);

        resRequestLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        ResRequestPanel.add(resRequestLabel, BorderLayout.CENTER);

        JLabel resReqText = new JLabel();
        resReqText.setText("Resource Request");
        ResRequestPanel.add(resRequestLabel, BorderLayout.CENTER);
        ResRequestPanel.add(resReqText, BorderLayout.SOUTH);

        disasterAddReqMainLabel = new JLabel(new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("UserDashboardPanel/disasterMain.svg"))));
        disasterAddReqMainLabel.setPreferredSize(new Dimension(250,250));
        disasterAddReqMainLabel.setOpaque(true);
        disasterAddReqMainLabel.setText("Disaster Request");
        disasterAddReqMainLabel.setFont( new Font("Fira Code Retina",Font.PLAIN,20));
        disasterAddReqMainLabel.setVerticalTextPosition(JLabel.BOTTOM);
        disasterAddReqMainLabel.setHorizontalTextPosition(JLabel.CENTER);
        disasterAddReqMainLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));

        tasksLabel = new JLabel(new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("UserDashboardPanel/tasks.svg"))));
        tasksLabel.setPreferredSize(new Dimension(250,250));
        tasksLabel.setOpaque(true);
        tasksLabel.setText("Task Request");
        tasksLabel.setFont( new Font("Fira Code Retina",Font.PLAIN,20));
        tasksLabel.setVerticalTextPosition(JLabel.BOTTOM);
        tasksLabel.setHorizontalTextPosition(JLabel.CENTER);
        tasksLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));

        disasterPanel.add(resRequestLabel,bgbc);
        bgbc.gridx = 1;
        bgbc.gridy = 0;
        disasterPanel.add(disasterAddReqMainLabel,bgbc);
        bgbc.gridx = 2;
        bgbc.gridy = 0;
        disasterPanel.add(tasksLabel,bgbc);

        resRequestLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainContent, "Request");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                resRequestLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                resRequestLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
            }
        });

        disasterAddReqMainLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainContent, "Disaster");

            }

            @Override
            public void mouseExited(MouseEvent e) {
                disasterAddReqMainLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                disasterAddReqMainLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
            }
        });

        tasksLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainContent, "Request");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                tasksLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                tasksLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
            }
        });

        dashSpace.setLayout(new BorderLayout());
        dashSpace.add(disasterPanel, BorderLayout.CENTER);
        return disasterPanel;
    }
    private JFXPanel createRequestResourcesSubPage() {
        JFXPanel requestPanel = new JFXPanel();
        dashSpace.setLayout(new BorderLayout());
        dashSpace.add(requestPanel, BorderLayout.CENTER);
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/user/resourcesRequest.fxml")));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
//                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fxml/user/style.css")).toExternalForm());
                requestPanel.setScene(scene);
            } catch (IOException e) {
                logger.error("Error in UserDashboardPanel.java:[createRequestResourcesSubPage] [IOException]: %s".formatted(getStackTraceAsString(e)));
            }
        });
        return requestPanel;
    }
    private void setCSSTheme(String theme, Scene scene){
        if (theme.equals("dark") || currentTheme.equals("dark")) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fxml/user/disaster-dark.css")).toExternalForm());
        } else {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fxml/user/disaster-light.css")).toExternalForm());
        }
    }
    private JFXPanel createDisasterSubPage() {
        JFXPanel disasterRequestPanel = new JFXPanel();
        dashSpace.setLayout(new BorderLayout());
        dashSpace.add(disasterRequestPanel, BorderLayout.CENTER);
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/user/disaster.fxml")));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                setCSSTheme(currentTheme,scene);
                disasterRequestPanel.setScene(scene);
                ThemeManager.addThemeChangeListener(theme -> {
                    setCSSTheme(theme,scene);
                });
            } catch (IOException e) {
                logger.error("Error in UserDashboardPanel.java: [createDisasterSubPage] [IOException]: %s".formatted(getStackTraceAsString(e)));
            }
        });
        return disasterRequestPanel;
    }

    private JPanel AccountSubPage() {
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        JPanel accountPanel = new JPanel();
        accountPanel.setPreferredSize(new Dimension(frame.getWidth() / 2, frame.getHeight() / 2));
        accountPanel.setBackground(Color.GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        backgroundPanel.add(accountPanel, gbc);
        return backgroundPanel;
    }
    private void changePage(String newPage){
        currentPage = newPage;
        addPages(newPage);
    }
    private void changeSubPage(String newSubPage){
        subPage = newSubPage;
    }
    public String goBack() {
        if (pageStack.size() > 1) {
            pageStack.pop();
        }
        backOptionLabel.setVisible(pageStack.size() > 1);

        logger.info("removed page current %s".formatted(pageStack.peek()));
        return pageStack.peek();
    }

    public void addPages(String newPage) {
        if(newPage == null) return;
        if (pageStack.peek() == null || !pageStack.peek().equals(newPage)){
            if (pageStack.size() >= MAX_PAGES) {
                pageStack.removeLast();
            }
            pageStack.push(newPage);
            logger.info("Added new page %s".formatted(newPage));
        }
        backOptionLabel.setVisible(pageStack.size() > 1);

    }

    private void changeSelectionMenu() {
        switch (currentPage) {
            case "HomePage" -> {
                changeSubPage("");
                homeLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
                disasterReqLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
                accountLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
            }
            case "DisasterPage" -> {
                changeSubPage("");
                homeLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
                disasterReqLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
                accountLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
            }
        }
    }
}
