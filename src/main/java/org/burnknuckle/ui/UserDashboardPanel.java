package org.burnknuckle.ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.burnknuckle.controllers.LoginSystem;
import org.burnknuckle.ui.SubPages.User.*;
import org.burnknuckle.utils.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.*;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.ui.SubPages.User.AccountPage.accountTabs;
import static org.burnknuckle.utils.MainUtils.clearProperties;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;
import static org.burnknuckle.utils.ThemeManager.*;
import static org.burnknuckle.utils.Userdata.*;

public class UserDashboardPanel {
    private final JFrame frame;
    private static JPanel menuBar;
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
    private String subPage = "";

    public UserDashboardPanel(JFrame frame) {
        this.frame = frame;
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.add(createUserDashboard(),BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }
    public static boolean checkAccountData(JTabbedPane tabbedPane) {
        String[] checkList = new String[]{
                "username", "password", "privilege", "email", "gender", "role",
                "first_name", "last_name", "date_of_birth", "account_created", "last_login", "is_active",
                "profile_picture_url", "bio", "failed_login_attempts", "password_last_updated",
                "volunteer_reg_time", "zip_code", "state", "road", "city", "country", "emergency_contact",
                "availability", "preferred_volunteering_location", "professional_background", "skills",
                "languages_spoken", "prior_experiences", "preferred_volunteering_work", "willingness",
                "physical_limitations", "blood_group", "phone_number", "allergies"
        };

        Set<String> optionalFields = Set.of(
                "bio", "profile_picture_url", "skills", "physical_limitations",
                "prior_experiences", "allergies", "languages_spoken",
                "phone_number", "failed_login_attempts", "password_last_updated",
                "volunteer_reg_time", "availability", "username", "privilege", "account_created", "last_login", "is_active"
        );

        Map<String, String> fieldNames = Map.ofEntries(
                Map.entry("password", "Password"),
                Map.entry("email", "Email"),
                Map.entry("gender", "Gender"),
                Map.entry("role", "Role"),
                Map.entry("first_name", "First Name"),
                Map.entry("last_name", "Last Name"),
                Map.entry("date_of_birth", "Date of Birth"),
                Map.entry("zip_code", "Zip Code"),
                Map.entry("state", "State"),
                Map.entry("road", "Road"),
                Map.entry("city", "City"),
                Map.entry("country", "Country"),
                Map.entry("emergency_contact", "Emergency Contact"),
                Map.entry("preferred_volunteering_location", "Preferred Volunteering Location"),
                Map.entry("professional_background", "Professional Background"),
                Map.entry("preferred_volunteering_work", "Preferred Volunteering Work"),
                Map.entry("willingness", "Willingness"),
                Map.entry("blood_group", "Blood Group")
        );

        Map<String, Integer> fieldTabMapping = Map.ofEntries(
                Map.entry("password", 0),
                Map.entry("email", 0),
                Map.entry("gender", 0),
                Map.entry("role", 0),
                Map.entry("first_name", 0),
                Map.entry("last_name", 0),
                Map.entry("date_of_birth", 0),
                Map.entry("zip_code", 1),
                Map.entry("state", 1),
                Map.entry("road", 1),
                Map.entry("city", 1),
                Map.entry("country", 1),
                Map.entry("emergency_contact", 1),
                Map.entry("preferred_volunteering_location", 2),
                Map.entry("professional_background", 3),
                Map.entry("preferred_volunteering_work", 2),
                Map.entry("willingness", 3),
                Map.entry("blood_group", 2)
        );

        try {
            Database db = Database.getInstance();
            db.getConnection();
            Map<String, Object> checkData = db.getUsernameDetails(getUsername());

            StringBuilder warnMessage = new StringBuilder();
            Integer firstMissingFieldTab = null;
            for (String column : checkList) {
                Object value = checkData.get(column);
                if (optionalFields.contains(column)) {
                    continue;
                }
                if (value == null || value.toString().isBlank()) {
                    String displayName = fieldNames.getOrDefault(column, column);
                    warnMessage.append(String.format("%s\n", displayName));

                    Integer tabIndex = fieldTabMapping.getOrDefault(column, -1);
                    if (tabIndex != -1 && firstMissingFieldTab == null) {
                        firstMissingFieldTab = tabIndex;
                    }
                }
            }

            if (!warnMessage.isEmpty()) {
                final Integer navigateToTab = firstMissingFieldTab;
                SwingUtilities.invokeLater(() -> {
                    int result = JOptionPane.showOptionDialog(null, warnMessage.toString(), "Please Fill These Fields",
                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Go to Tab", "Cancel"}, "Go to Tab");

                    if (result == JOptionPane.YES_OPTION && navigateToTab != null) {
                        tabbedPane.setSelectedIndex(navigateToTab);
                    }
                });
                return false;
            }

        } catch (Exception e) {
            logger.error(String.format("Error in checkAccountData: %s", getStackTraceAsString(e)));
        }
        return true;
    }

    public static boolean checkStatusOfUser(){
        getUserDataFromDatabase();
        return !userdata.get("privilege").toString().equals("user");
    }
    private void updateThemeColors(String change){
        menuBar.setBackground(getColorFromHex(ADPThemeData.get("sidebar")));
        changeSelectionMenu();
        accountLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        logOut.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        fakeSpace.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        fakeSpace1.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        backOptionLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
    }

    private JPanel createUserDashboard() {
        JPanel block = new JPanel();
        block.setLayout(new BorderLayout());
        menuBar = new JPanel();
        dashSpace = new JPanel(new BorderLayout());
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
                if(!currentPage.equals("HomePage") && checkAccountData(accountTabs) && checkStatusOfUser()){
                    changePage("HomePage");
                    changeSelectionMenu();
                    switchTabs("HomePage", mainContent);
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
                if(!currentPage.equals("RequestPage") && checkAccountData(accountTabs) && checkStatusOfUser()) {
                    changePage("RequestPage");
                    changeSelectionMenu();
                    switchTabs("RequestPage", mainContent);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!currentPage.equals("RequestPage")) disasterReqLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));

            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!currentPage.equals("RequestPage")) disasterReqLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
            }
        });
        backOptionLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(checkAccountData(accountTabs) && checkStatusOfUser()){
                    String back = goBack();
                    changePage(back);
                    changeSelectionMenu();
                    switchTabs(back, mainContent);
                }
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
                if(!currentPage.equals("Account") && checkAccountData(accountTabs) && checkStatusOfUser()) {
                    addPages("Account");
                    changePage("Account");
                    cardLayout.show(mainContent, "Account");
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
                    clearProperties(new Properties());
                    SwingUtilities.invokeLater(() -> {
                        try {
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
                            currentPage = "";
                            frame.getContentPane().removeAll();
                            frame.repaint();
                            frame.revalidate();
                            frame.setVisible(false);
                            new LoginSystem(frame);
                        } catch (Exception exx) {
                            logger.error("Error in: %s".formatted(getStackTraceAsString(exx)));
                            JOptionPane.showMessageDialog(frame, "An error occurred during logout.");
                        }
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

        mainContent.add(new HomePage().createHomePage(mainContent), "HomePage");
        mainContent.add(new RequestPage().createRequestPage(cardLayout, mainContent), "RequestPage");
        mainContent.add(new AccountPage().createAccountPage(frame, cardLayout, mainContent), "Account");
        mainContent.add(new RequestResources().createRequestResourcesSubPage(), "Request");
        mainContent.add(new TeamCreationPage().createTeamCreationPage(), "Team Creation");
        mainContent.add(new RequestDisaster().createDisasterSubPage(dashSpace),"Disaster");
        mainContent.add(new VolunteerRegistration().createVolunteerRegistrationSubPage(cardLayout, mainContent),"Volunteer Registration");

        if(checkAccountData(accountTabs)){
            if (checkStatusOfUser()){
                addPages("HomePage");
                cardLayout.show(mainContent, "HomePage");
            } else {
                cardLayout.show(mainContent, "Volunteer Registration");
            }
        } else {
            addPages("Account");
            cardLayout.show(mainContent, "Account");
        }

        block.add(menuBar, BorderLayout.WEST);
        block.add(mainContent, BorderLayout.CENTER);
        addThemeChangeListener(this::updateThemeColors);
        return block;
    }

    private void switchTabs(String tabName, JPanel mainContent){
        cardLayout.show(mainContent, tabName);
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
//        logger.info("removed page current %s".formatted(pageStack.peek()));
        return pageStack.peek();
    }

    public void addPages(String newPage) {
        if(newPage == null) return;
        if (pageStack.peek() == null || !pageStack.peek().equals(newPage)){
            if (pageStack.size() >= MAX_PAGES) {
                pageStack.removeLast();
            }
            pageStack.push(newPage);
//            logger.info("Added new page %s".formatted(newPage));
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
            case "RequestPage" -> {
                changeSubPage("");
                homeLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
                disasterReqLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
                accountLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
            }
            case "Account"-> {
                changeSubPage("");
                homeLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
                disasterReqLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
                accountLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
            }
        }
    }
}
