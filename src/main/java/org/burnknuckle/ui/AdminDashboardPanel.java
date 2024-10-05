package org.burnknuckle.ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatTabbedPane;
import com.formdev.flatlaf.ui.FlatTabbedPaneUI;
import org.burnknuckle.controllers.LoginSystem;
import org.burnknuckle.utils.Database;

import javax.swing.Timer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.*;

import static org.burnknuckle.controllers.Main.logger;
import static org.burnknuckle.model.ThemeManager.*;
import static org.burnknuckle.ui.subParts.AdminUsersLabel.setTitleBar;
import static org.burnknuckle.ui.subParts.AdminUsersLabel.userFrames;
import static org.burnknuckle.utils.MainUtils.clearProperties;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;

public class AdminDashboardPanel {
    private final JFrame frame;
    private JPanel menuBar;
    private JPanel SlipSidePanel;
    private CardLayout cardLayout;

    private JLabel avatarLabel;
    private JPanel MenuPanel;
    private JSeparator splitter;
    private JSeparator splitter1;
    private JPanel avatarPanel;
    private JPanel buttonPanel;
    private JButton logoutButton;
    private JPanel overlayPanel;
    private JButton MenuButton;
    private JPanel mainContent;
    private Map<String, Object> userdata;
    private final Deque<String> pageStack = new LinkedList<>();
    private String currentPage = "Dashboard";


    public AdminDashboardPanel(JFrame frame, Map<String, Object> userdata ) {
        this.frame = frame;
        this.userdata = userdata;
        frame.setLayout(new BorderLayout());
        frame.setContentPane(createAdminBoard()) ;
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateComponentSizesAndPositions();
            }
        });
        addThemeChangeListener(this::updateThemeColors);
    }
    private void updateThemeColors(String Change) {
        MenuPanel.setBackground(getColorFromHex(ADPThemeData.get("text")));
        menuBar.setBackground(getColorFromHex(ADPThemeData.get("sidebar")));
        logoutButton.setBackground(getColorFromHex(ADPThemeData.get("sidebar-default-menu-button")));
        logoutButton.setForeground(getColorFromHex(ADPThemeData.get("text")));
        MenuButton.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        MenuButton.setForeground(getColorFromHex(ADPThemeData.get("text")));
        overlayPanel.setBackground(getColorFromHex(ADPThemeData.get("overlay")));
        avatarPanel.setBackground(getColorFromHex(ADPThemeData.get("sidebar")));
        buttonPanel.setBackground(getColorFromHex(ADPThemeData.get("sidebar")));
        splitter.setForeground(Color.decode(ADPThemeData.get("splitter")));
        splitter1.setForeground(Color.decode(ADPThemeData.get("splitter")));
        setButtonHoverAndActiveColors(logoutButton, ADPThemeData, "none");
        setButtonHoverAndActiveColors(MenuButton, ADPThemeData, "none");

        ImageIcon ResQitLogo;
        if(Change.equals("dark")){
            ResQitLogo = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("DarkThemes/Resqit-dark.png")));
        } else {
            ResQitLogo = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("LightThemes/Resqit-light.png")));
        }
        Image scaledResQitLogo = ResQitLogo.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        avatarLabel.setIcon(new ImageIcon(scaledResQitLogo));
        frame.revalidate();
        frame.repaint();
    }

    private void updateComponentSizesAndPositions() {
        MenuPanel.setSize(frame.getWidth(), frame.getHeight());
        menuBar.setSize((frame.getWidth()/4) + 10, frame.getHeight());
        overlayPanel.setSize(frame.getWidth() - menuBar.getWidth(), frame.getHeight());
        mainContent.setBounds(SlipSidePanel.getWidth(),0,frame.getWidth()-SlipSidePanel.getWidth(), frame.getHeight());
        SlipSidePanel.setSize(((MenuButton.getWidth() + logoutButton.getWidth()) / 2)+10, 2 + frame.getHeight()-MenuButton.getHeight());
        frame.revalidate();
        frame.repaint();
    }

    private void removeMenu() {
        boolean isVisible = MenuPanel.isVisible();
        MenuPanel.setVisible(!isVisible);
        MenuButton.setVisible(isVisible);
        logoutButton.setVisible(isVisible);
        SlipSidePanel.setVisible(isVisible);
        SwingUtilities.invokeLater(() -> {
            frame.revalidate();
            frame.repaint();
        });
    }


    private JLayeredPane createAdminBoard() {
        JLayeredPane adminBoard = new JLayeredPane();
        adminBoard.setVisible(true);
        adminBoard.setOpaque(true);
        MenuButton = new JButton();
        MenuButton.setIcon(new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/menu.svg"))));
        MenuButton.setPreferredSize(new Dimension(40, 40));
        MenuButton.setSize(40,40);
        MenuButton.setToolTipText("Display Sidebar");
        MenuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                removeMenu();
            }
        });
        setButtonHoverAndActiveColors(MenuButton, ADPThemeData, "none");

        MenuPanel = new JPanel();
        MenuPanel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
        MenuPanel.setLayout(new BorderLayout());
        MenuPanel.setBackground(getColorFromHex(ADPThemeData.get("text")));
        MenuPanel.setOpaque(false);
        MenuPanel.setVisible(false);

        menuBar = new JPanel();
        menuBar.setLayout(new GridBagLayout());
        menuBar.setBackground(getColorFromHex(ADPThemeData.get("sidebar")));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        avatarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        avatarPanel.setSize(150, 100);
        avatarPanel.setBackground(getColorFromHex(ADPThemeData.get("sidebar")));

        ImageIcon ResQitLogo = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("DarkThemes/Resqit-dark.png")));
        Image scaledResQitLogo = ResQitLogo.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        avatarLabel = new JLabel(new ImageIcon(scaledResQitLogo));
        avatarLabel.setOpaque(false);
        avatarPanel.add(avatarLabel);
        avatarLabel.setToolTipText("Unite & Aid");

        gbc.gridx = 0;
        gbc.gridy = 0;
        menuBar.add(avatarPanel,gbc);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBackground(getColorFromHex(ADPThemeData.get("sidebar")));
        String[] buttonLabels = {"Dashboard", "Disaster", "Co-Admins", "Locations", "Delivery Status", "Volunteer Management"};
        for (String label : buttonLabels) {
            JButton button = createSidebarButton(label);
            buttonPanel.add(button);
        }
        splitter = new JSeparator();
        splitter.setOrientation(SwingConstants.HORIZONTAL);
        splitter.setPreferredSize(new Dimension(250, 10));
        splitter.setOpaque(false);
        splitter.setForeground(Color.decode(ADPThemeData.get("splitter")));
        splitter1 = new JSeparator();
        splitter1.setOrientation(SwingConstants.HORIZONTAL);
        splitter1.setPreferredSize(new Dimension(250, 10));
        splitter1.setOpaque(false);
        splitter1.setForeground(Color.decode(ADPThemeData.get("splitter")));
        gbc.gridy = 1;
        menuBar.add(splitter, gbc);
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        menuBar.add(buttonPanel, gbc);
        gbc.gridy = 3;
        menuBar.add(splitter1, gbc);
        logoutButton = new JButton();
        logoutButton.setIcon(new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/log-out.svg"))));
        logoutButton.setPreferredSize(new Dimension(40, 40));
        logoutButton.setToolTipText("Log out");
        logoutButton.setSize(40,40);
        setButtonHoverAndActiveColors(logoutButton, ADPThemeData, "none");
        logoutButton.addMouseListener(new MouseAdapter() {
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
                            currentPage = "";
                            frame.getContentPane().removeAll();
                            frame.repaint();
                            frame.revalidate();
                            frame.setVisible(false);
                            new LoginSystem(frame);
//                            frame.setVisible(true);
                            JOptionPane.showMessageDialog(frame, "You have logged out successfully.");
                        } catch (Exception exx) {
                            logger.error(getStackTraceAsString(exx));
                            JOptionPane.showMessageDialog(frame, "An error occurred during logout.");
                        }
                    });
                }

            }
        });
        overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape roundedRectangle = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setColor(getColorFromHex(ADPThemeData.get("overlay")));
                g2d.fill(roundedRectangle);
            }
        };
        overlayPanel.setOpaque(false);
        overlayPanel.setToolTipText("Out");
        overlayPanel.setDoubleBuffered(true);
        overlayPanel.setSize(frame.getWidth()-250, frame.getHeight());
        overlayPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                removeMenu();
            }
        });
        MenuPanel.add(overlayPanel, BorderLayout.CENTER);
        MenuPanel.add(menuBar, BorderLayout.WEST);

        mainContent = new JPanel();
        cardLayout = new CardLayout();
        mainContent.setLayout(cardLayout);

        mainContent.add(createDashboardPanel(), "Dashboard");
        mainContent.add(createDisasterResourcesPanel(), "Disaster");
        mainContent.add(createCoAdminsPanel(), "Co-Admins");
        mainContent.add(createAddMarkersPanel(), "Locations");
        mainContent.add(createDeliveryStatusPanel(), "Delivery Status");
        mainContent.add(createVolunteerManagementPanel(), "Volunteer Management");

        adminBoard.add(MenuPanel, Integer.valueOf(0));
        adminBoard.add(mainContent, Integer.valueOf(0));

        SlipSidePanel = new JPanel();
        SlipSidePanel.setBackground(getColorFromHex(ADPThemeData.get("overlay")));
        SlipSidePanel.setSize(((MenuButton.getWidth() + logoutButton.getWidth()) / 2)+10, 2 + frame.getHeight()-MenuButton.getHeight());

        SlipSidePanel.setLayout(new GridBagLayout());
        GridBagConstraints sGbc = new GridBagConstraints();
        sGbc.gridx = 0;
        sGbc.gridy = 0;
        sGbc.anchor = GridBagConstraints.NORTH;
        sGbc.insets = new Insets(10, 5, 10, 5);
        sGbc.weightx = 1.0;
        sGbc.weighty = 0.1;
        sGbc.fill = GridBagConstraints.HORIZONTAL;
        SlipSidePanel.add(MenuButton, sGbc);
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        sGbc.insets = new Insets(10, 5, 10, 5);
        sGbc.gridx = 0;
        sGbc.gridy = 1;
        sGbc.weighty = 1.0;
        sGbc.fill = GridBagConstraints.BOTH;
        SlipSidePanel.add(filler, sGbc);

        sGbc.gridx = 0;
        sGbc.gridy = 2;
        sGbc.anchor = GridBagConstraints.SOUTH;
        sGbc.insets = new Insets(10, 5, 10, 5);
        sGbc.weighty = 0.1;
        sGbc.fill = GridBagConstraints.HORIZONTAL;
        SlipSidePanel.add(logoutButton, sGbc);

        adminBoard.add(SlipSidePanel, Integer.valueOf(1));
        updateComponentSizesAndPositions();
        FlatSVGIcon.ColorFilter filter = FlatSVGIcon.ColorFilter.getInstance()
                .add( Color.black, Color.black, Color.white )
                .add( Color.white, Color.black, Color.black );
        ((FlatSVGIcon) logoutButton.getIcon()).setColorFilter(filter);
        ((FlatSVGIcon) MenuButton.getIcon()).setColorFilter(filter);
        return adminBoard;
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(250, 40));
        button.setForeground(getColorFromHex(ADPThemeData.get("text")));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(true);
        setButtonHoverAndActiveColors(button, ADPThemeData, "sidebar");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeMenu();
                cardLayout.show(mainContent, text);
                frame.revalidate();
                frame.repaint();
            }
        });
        return button;
    }

    private JPanel createDashboardPanel() {
        currentPage = "Dashboard";
        JPanel panel = new JPanel();
        panel.setBackground(getColorFromHex(ADPThemeData.get("background")));
        panel.add(new JLabel("Dashboard Content Here"));
        return panel;
    }

    private JPanel createDisasterResourcesPanel() {
        currentPage = "Disaster";
        JPanel panel = new JPanel();
        return panel;
    }
    private String loadIconSort(String sortDirection, JButton sortButton){
        if (sortDirection.equals("inc")){
            sortDirection = "dec";
            sortButton.setIcon(new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Common/south.svg"))));
        } else {
            sortDirection = "inc";
            sortButton.setIcon(new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Common/north.svg"))));

        }
        return sortDirection;
    }
    private JPanel createCoAdminsPanel() {
        currentPage = "CoAdmins";
        Database db = Database.getInstance();
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inner = new JPanel(new GridBagLayout());
        GridBagConstraints innerGBC = new GridBagConstraints();

        inner.setBackground(getColorFromHex(ADPThemeData.get("background")));
        JPanel AdminMenu = new JPanel();
        AdminMenu.setLayout(new GridLayout());

        JLabel label = new JLabel("Co-Admins Management");
        label.setFont(new Font("Fira Code Retina", Font.PLAIN, 35));
        label.setHorizontalAlignment(JLabel.CENTER);
        innerGBC.insets = new Insets(10, 10, 10, 10);
        innerGBC.gridx = 0;
        innerGBC.gridy = 0;
        innerGBC.anchor = GridBagConstraints.NORTH;
        inner.add(label, innerGBC);

        JPanel topBar = new JPanel(new GridBagLayout());
        topBar.setSize(new Dimension(mainContent.getWidth()-70, (mainContent.getHeight() - 10) /5));
        topBar.setOpaque(false);

        GridBagConstraints topBarCont = new GridBagConstraints();

        JButton saveButton = new JButton();
        saveButton.setIcon(new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Common/save.svg"))));
        saveButton.setToolTipText("Save");
        saveButton.setBorder(new EmptyBorder(5,5,5,5));
        saveButton.setOpaque(false);
        setButtonHoverAndActiveColors(saveButton, ADPThemeData, "onBackGround");
        saveButton.setSize(new Dimension(30,30));

        JPanel fakePanel = new JPanel();
        fakePanel.setOpaque(false);
        JLabel sortLabel = new JLabel();
        sortLabel.setText("Sort");
        sortLabel.setFont(new Font("Fira Code Retina", Font.PLAIN, 18));
        JButton sortButton = new JButton();
        final String[] sortDirection = {"inc"};
        sortDirection[0] = loadIconSort(sortDirection[0], sortButton);
        sortButton.setSize(new Dimension(30,30));
        sortButton.setBorder(new EmptyBorder(5,5,5,5));
        sortButton.setOpaque(false);
        setButtonHoverAndActiveColors(sortButton, ADPThemeData, "onBackGround");

        sortButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sortDirection[0] = loadIconSort(sortDirection[0], sortButton);
            }
        });
        sortButton.setToolTipText("Sort");

        topBarCont.insets = new Insets(10,10,10,10);
        topBarCont.gridx = 0;
        topBarCont.gridy = 0;
        topBarCont.weightx = 1.0;
        topBarCont.fill = GridBagConstraints.HORIZONTAL;
        topBarCont.anchor = GridBagConstraints.WEST;
        topBar.add(fakePanel, topBarCont);

        topBarCont.insets = new Insets(10,10,10,10);
        topBarCont.gridx = 1;
        topBarCont.gridy = 0;
        topBarCont.weightx = 0;
        topBarCont.fill = GridBagConstraints.NONE;
        topBarCont.anchor = GridBagConstraints.CENTER;
        topBar.add(saveButton, topBarCont);

        topBarCont.insets = new Insets(10,10,10,10);
        topBarCont.gridx = 2;
        topBarCont.gridy = 0;
        topBarCont.weightx = 0;
        topBarCont.fill = GridBagConstraints.HORIZONTAL;
        topBarCont.anchor = GridBagConstraints.CENTER;
        topBar.add(sortLabel, topBarCont);

        topBarCont.insets = new Insets(10,10,10,10);
        topBarCont.gridx = 3;
        topBarCont.gridy = 0;
        topBarCont.weightx = 0;
        topBarCont.fill = GridBagConstraints.NONE;
        topBarCont.anchor = GridBagConstraints.CENTER;
        topBar.add(sortButton, topBarCont);


        innerGBC.insets = new Insets(30, 30, 0, 30);
        innerGBC.gridx = 0;
        innerGBC.gridy = 1;
        innerGBC.fill = GridBagConstraints.HORIZONTAL;
        inner.add(topBar, innerGBC);

        List<Map<String, Object>> coAdminData = db.getPrivilegeData("co-admin");
        String[] columnNames = {
                "ID",
                "Username",
                "Gender",
                "Role",
                "Email",
                "Privilege",
                "Password",
                "First Name",
                "Last Name",
                "Phone Number",
                "Date of Birth",
                "Account Creation",
                "Last Login",
                "Active Status",
                "Address",
                "Profile Picture URL",
                "Bio",
                "Failed Login Attempts",
                "Password Last Updated"
        };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
//                if (column == 6) {
//                    return false;
//                }
//                return super.isCellEditable(row, column);
                return false;
            }
        };

        int id = 1;
        for (Map<String, Object> userData : coAdminData) {
            Object[] rowData = {
                    id++,
                    userData.get("username"),
                    userData.get("gender"),
                    userData.get("role"),
                    userData.get("email"),
                    userData.get("privilege"),
                    userData.get("password"),
                    userData.get("first_name"),
                    userData.get("last_name"),
                    userData.get("phone_number"),
                    userData.get("date_of_birth"),
                    userData.get("account_created"),
                    userData.get("last_login"),
                    userData.get("is_active"),
                    userData.get("address"),
                    userData.get("profile_picture_url"),
                    userData.get("bio"),
                    userData.get("failed_login_attempts"),
                    userData.get("password_last_updated"),
                    "show"
            };
            tableModel.addRow(rowData);
        }

        int size = coAdminData.size();
        int noD = (String.valueOf(size).length() * 20);

        JTable coAdminTable = new JTable(tableModel);
        coAdminTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));
        coAdminTable.setFont(new Font("Arial", Font.PLAIN, 14));

        coAdminTable.getColumnModel().getColumn(0).setPreferredWidth(noD);
        coAdminTable.getColumnModel().getColumn(0).setMinWidth(noD);
        coAdminTable.getColumnModel().getColumn(0).setMaxWidth(noD);
        coAdminTable.setFillsViewportHeight(true);
        ListSelectionModel selected = coAdminTable.getSelectionModel();
        coAdminTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                int row = coAdminTable.getSelectedRow();
                int col = coAdminTable.getSelectedColumn();

                if (col == 6 && row != -1) {
                    coAdminTable.setToolTipText("Double-click to show or hide");
                } else {
                    coAdminTable.setToolTipText(null);
                }
            }

//        JButton editButton = new JButton();
//        editButton.setIcon(new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/log-out.svg"))));
//        editButton.setToolTipText("Edit");
//        editButton.setSize(new Dimension(30,30));
//        JButton deleteButton = new JButton();
//        deleteButton.setIcon(new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/log-out.svg"))));
//        deleteButton.setToolTipText("Delete");
//        deleteButton.setSize(new Dimension(30,30));

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    int row = coAdminTable.getSelectedRow();
                    int col = coAdminTable.getSelectedColumn();

                    if (col == 6) {
                        String password = (String) coAdminData.get(row).get("password");

                        coAdminTable.setValueAt(password, row, col);
                        Timer timer = new Timer(3000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                coAdminTable.setValueAt("show", row, col);
                                frame.revalidate();
                                frame.repaint();
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();


                    }
                }
            }
        });
//        selected.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                int row = coAdminTable.getSelectedRow();
//                int col = coAdminTable.getSelectedColumn();
//                if(col == 5){
//                    System.out.println(coAdminTable.getValueAt(row, col));
//                    System.out.println(col);
//                    String password = (String) coAdminData.get(row).get("password");
//                    coAdminTable.setValueAt(password, row, col);
//
//                }
//            }
//        });
        JScrollPane tableScrollPane = new JScrollPane(coAdminTable);
        innerGBC.insets = new Insets(0,30,30,10);
        innerGBC.gridx = 0;
        innerGBC.gridy = 2;
        innerGBC.fill = GridBagConstraints.BOTH;
        innerGBC.weightx = 1.0;
        innerGBC.weighty = 1.0;
        inner.add(tableScrollPane, innerGBC);

        panel.add(inner, BorderLayout.CENTER);
        return panel;
    }
    private JPanel createAddMarkersPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(getColorFromHex(ADPThemeData.get("background")));
        panel.add(new JLabel("Add Markers Content Here"));
        return panel;
    }

    private JPanel createDeliveryStatusPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(getColorFromHex(ADPThemeData.get("background")));
        panel.add(new JLabel("Delivery Status Content Here"));
        return panel;
    }
    private static int targetCol(String Key, java.util.List<Map<String, Object>>  coAdminData){
        String targetKey = "username";
        int maxLength = 0;
        for (Map<String, Object> userData : coAdminData) {
            Object value = userData.get(targetKey);
            if (value instanceof String) {
                int length = ((String) value).length();
                if (length > maxLength) {
                    maxLength = length;
                }
            }
        }
        return maxLength;
    }
    private JPanel createVolunteerManagementPanel() {
        Database db = Database.getInstance();
        java.util.List<Map<String, Object>>  coAdminData = db.getPrivilegeData("co-admin");
        ArrayList<String> userDataDetails = new ArrayList<>(Arrays.asList(
                "Sno", "Username", "Gender", "Role", "Email", "Privilege", "Password",
                "First Name", "Last Name", "Phone Number", "Date of Birth",
                "Account Creation", "Last Login", "Active Status", "Address",
                "Profile Picture URL", "Bio", "Failed Login Attempts",
                "Password Last Updated"
        ));

        ArrayList<Integer> selectedTitles = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        ArrayList<String> selectedUserDataDetails = new ArrayList<>();
        for (Integer index : selectedTitles) {
            selectedUserDataDetails.add(userDataDetails.get(index));
        }
        JPanel pageBackGroundPanel = new JPanel();
        pageBackGroundPanel.setBackground(getColorFromHex(ADPThemeData.get("TabTitleBg")));
        pageBackGroundPanel.setLayout(new BorderLayout());

        JPanel roleManagementInner = new JPanel();
        roleManagementInner.setLayout(new GridBagLayout());
        GridBagConstraints page1Gbc = new GridBagConstraints();
        page1Gbc.gridx = 0;
        page1Gbc.gridy = 0;
        page1Gbc.anchor = GridBagConstraints.NORTHWEST;
        page1Gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Roles");
        title.setFont(new Font("Inter", Font.BOLD, 38));
        roleManagementInner.add(title, page1Gbc);

        JPanel pageInner = new JPanel();
        pageInner.setLayout(new GridBagLayout());

        GridBagConstraints pageInnerGbc = new GridBagConstraints();
        pageInnerGbc.gridx = 0;
        pageInnerGbc.gridy = 0;
        pageInnerGbc.fill = GridBagConstraints.HORIZONTAL;
//        pageInnerGbc.weightx = 1.0;
        pageInnerGbc.anchor = GridBagConstraints.NORTHWEST;
        pageInnerGbc.insets = new Insets(0,0,0,10);

        JPanel columnTitles = setTitleBar(selectedUserDataDetails);
        columnTitles.setBackground(Color.red);
        pageInner.add(columnTitles, pageInnerGbc);
        logger.info(coAdminData.toString());
        logger.info(coAdminData.size());

        pageInnerGbc.gridy = 1;
        pageInnerGbc.weightx = 1.0;
        pageInnerGbc.weighty = 1.0;
        pageInnerGbc.fill = GridBagConstraints.BOTH;
        pageInnerGbc.anchor = GridBagConstraints.CENTER;

        JScrollPane userBlock = new JScrollPane(userFrames(coAdminData, selectedUserDataDetails));
        userBlock.setBackground(Color.CYAN);
        pageInner.add(userBlock, pageInnerGbc);

        page1Gbc.gridy = 1;
        page1Gbc.fill = GridBagConstraints.BOTH;
        page1Gbc.weightx = 1.0;
        page1Gbc.weighty = 1.0;
        page1Gbc.insets = new Insets(10, 10, 10, 20);

        roleManagementInner.add(pageInner, page1Gbc);

        JPanel page2 = new JPanel();
        page2.add(new JLabel("This is Tab 2"));
        JPanel page3 = new JPanel();
        page3.add(new JLabel("This is Tab 3"));

        JTabbedPane tabbedPane = new FlatTabbedPane();
        tabbedPane.setUI(new CustomFlatTabbedPaneUI());
        tabbedPane.addTab("Role Management", roleManagementInner);
        tabbedPane.addTab("Users Management", page2);
        tabbedPane.addTab("Request Management", page3);

        pageBackGroundPanel.add(tabbedPane, BorderLayout.CENTER);
        return pageBackGroundPanel;
    }

    static class CustomFlatTabbedPaneUI extends FlatTabbedPaneUI {
        @Override
        protected Insets getTabInsets(int tabPlacement, int tabIndex) {
            return new Insets(20, 20, 20, 20);
        }
        @Override
        protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
            g.setFont(font);
            if (isSelected) {
                g.setColor(getColorFromHex(ADPThemeData.get("TabTitleTextColorSelected")));
            } else {
                g.setColor(getColorFromHex(ADPThemeData.get("TabTitleTextColorNormal")));
            }
            g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
        }
        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            if (isSelected) {
                g.setColor(getColorFromHex(ADPThemeData.get("TabTitleSelected")));
            } else {
                g.setColor(getColorFromHex(ADPThemeData.get("TabTitleBg")));
            }
            g.fillRect(x, y, w, h);
        }
        @Override
        protected void paintTabSelection(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h) {
            g.setColor(getColorFromHex(ADPThemeData.get("TabTitleTextColorSelected")));
            g.fillRect(x, y + h - 3, w, 3);
        }
    }

}
