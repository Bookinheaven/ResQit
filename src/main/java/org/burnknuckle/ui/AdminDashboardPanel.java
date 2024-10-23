package org.burnknuckle.ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.burnknuckle.controllers.LoginSystem;
import org.burnknuckle.ui.SubPages.Admin.*;
import org.burnknuckle.utils.Database;
import raven.datetime.component.date.DatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.utils.MainUtils.clearProperties;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;
import static org.burnknuckle.utils.ThemeManager.*;
import static org.burnknuckle.utils.Userdata.getUsername;

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
    private final Deque<String> pageStack = new LinkedList<>();

    public AdminDashboardPanel(JFrame frame) {
        this.frame = frame;
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
        menuBar.setBackground(getColorFromHex(ADPThemeData.get("sidebar")));
        buttonPanel.setBackground(getColorFromHex(ADPThemeData.get("sidebar")));
        splitter.setForeground(Color.decode(ADPThemeData.get("splitter")));
        for (Component button: buttonPanel.getComponents() ) {
            if(button instanceof JButton){
                setButtonHoverAndActiveColors((JButton) button, ADPThemeData, "sidebar");
            }
        }
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
        SlipSidePanel.setSize(((MenuButton.getWidth() + logoutButton.getWidth()) / 2)+10, 2 + frame.getHeight()-MenuButton.getHeight());
        mainContent.setBounds(SlipSidePanel.getWidth(),0, frame.getWidth()-SlipSidePanel.getWidth()-15, frame.getHeight()-40);
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

        ImageIcon ResQitLogo;
        if(currentTheme.equals("dark")){
            ResQitLogo = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("DarkThemes/Resqit-dark.png")));
        } else {
            ResQitLogo = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("LightThemes/Resqit-light.png")));
        }
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
        String[] buttonLabels = {"Dashboard", "Disaster Management", "User Management", "Resources Status", "Volunteer Management"};
        for (String label : buttonLabels) {
            JButton button = createSidebarButton(label);
            button.setBackground(getColorFromHex(ADPThemeData.get("sidebar")));
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
                            String username = getUsername();
                            if (username != null) {
                                Map<String, Object> lastLogin = new HashMap<>();
                                lastLogin.put("last_login", new Timestamp(System.currentTimeMillis()));
                                lastLogin.put("is_active", false);
                                try {
                                    Database db = Database.getInstance();
                                    db.connectDatabase();
                                    db.updateData12(0, username, lastLogin);
                                } catch (Exception x) {
                                    logger.error("Error in: %s".formatted(getStackTraceAsString(x)));
                                }
                            }
                            frame.getContentPane().removeAll();
                            frame.repaint();
                            frame.revalidate();
                            frame.setVisible(false);
                            new LoginSystem(frame);
//                            frame.setVisible(true);
                            JOptionPane.showMessageDialog(frame, "You have logged out successfully.");
                        } catch (Exception exx) {
                            logger.error("Error in: %s".formatted(getStackTraceAsString(exx)));
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
        mainContent.add(new DashboardPanel().createDashboardPanel(), "Dashboard");
        mainContent.add(new DisasterResourcesPanel().createDisasterResourcesPanel(), "Disaster Management");
        mainContent.add(new UsersManagementPanel().createUsersCoAdminsPanel(), "User Management");
        mainContent.add(new ResourcesStatus().createDeliveryStatusPanel(), "Resources Status");
        mainContent.add(new VolunteerManagementPanel().createVolunteerManagementPanel(), "Volunteer Management");

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
        button.addActionListener(_ -> {
            removeMenu();
            cardLayout.show(mainContent, text);
            frame.revalidate();
            frame.repaint();
        });
        return button;
    }

    private void saveAllChanges(JTable table, DefaultTableModel tableModel, Map<String, String> columnMapping, JButton button) {
        int rowCount = tableModel.getRowCount();
        boolean hasChanges = false;
        for (int i = 0; i < rowCount; i++) {
            Map<String, Object> rowData = new LinkedHashMap<>();
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                String columnName = tableModel.getColumnName(j);
                String dbColumnName = columnMapping.get(columnName);
                if (dbColumnName != null) {
                    Object value = table.getValueAt(i, j);
                    if (value != null) {
                        rowData.put(dbColumnName, value.toString());
                    }
                }
            }
            Object idValue = table.getValueAt(i, 0);
            if (idValue != null) {
                rowData.put("id", idValue.toString());
                Database.getInstance().updateData(2, rowData);
                hasChanges = true;
            }
        }
        if (hasChanges) {
            JOptionPane.showMessageDialog(null, "All changes saved successfully!", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            logger.info("All changes saved successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "No changes detected to save.", "No Changes", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void showDateTimePicker(JTable table, int row, int column) {
        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new java.util.Date());

        DatePicker datePicker = new DatePicker();
        JFormattedTextField editor = new JFormattedTextField();
        datePicker.setEditor(editor);

        JPanel panel = new JPanel();
        panel.add(datePicker);
        panel.add(timeSpinner);

        int option = JOptionPane.showConfirmDialog(null, panel, "Select Date and Time", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            LocalDateTime selectedDateTime = LocalDateTime.of(datePicker.getSelectedDate(), ((java.util.Date) timeSpinner.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime());

            String formattedDateTime = selectedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            table.setValueAt(formattedDateTime, row, column);
        }
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


}
