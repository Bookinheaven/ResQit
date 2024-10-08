package org.burnknuckle.ui;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

import static org.burnknuckle.controllers.swing.LoginSystem.BtwLS;
import static org.burnknuckle.Main.logger;
import static org.burnknuckle.controllers.swing.SignUpSystem.SignUp;
import static org.burnknuckle.controllers.swing.SignUpSystem.validateFields;
import static org.burnknuckle.utils.ThemeManager.currentTheme;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;

public class SignUpPanel {
    private final JFrame frame;
    private final JLayeredPane MainPane;
    private JPanel signUpOverlayPanel;

    private JProgressBar passwordProgressBar;
    private JProgressBar conformPasswordProgressBar;
    private JProgressBar usernameProgressBar;

    private JTextField emailField;
    private JTextField usernameField;

    private JPasswordField passwordField;
    private JPasswordField conformPasswordField;

    private JComboBox<String> genderComboBox;
    private JComboBox<String> roleComboBox;

    private JLabel warningLabel;

    private JButton signUpButton;

    public SignUpPanel(JFrame frame, JLayeredPane MainPane) {
        this.frame = frame;
        this.MainPane = MainPane;
        initialize();
    }

    private void SignReloadImage(GridBagConstraints leftGbc, JPanel leftPanel){
        ImageIcon volunteersLogo;
        ImageIcon globalImpact;
        if (currentTheme.equals("dark")){
            volunteersLogo = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("DarkThemes/volunteers-dark.png")));
            globalImpact = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("DarkThemes/global-dark.png")));
        }
        else {
            volunteersLogo = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("LightThemes/volunteers-light.png")));
            globalImpact = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("LightThemes/global-light.png")));
        }
        try {
            Image scaledVolunteersLogo = volunteersLogo.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            JLabel VLogoLabel = new JLabel(new ImageIcon(scaledVolunteersLogo));
            VLogoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            leftGbc.gridx = 0;
            leftGbc.gridy = 2;
            leftGbc.gridwidth = 2;
            leftGbc.insets = new Insets(0,0,10,10);
            leftGbc.fill = GridBagConstraints.BOTH;
            leftPanel.add(VLogoLabel, leftGbc);

            Image GlobalsScaledLogo = globalImpact.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel GLogoLabel = new JLabel(new ImageIcon(GlobalsScaledLogo));
            GLogoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            leftGbc.gridx = 0;
            leftGbc.gridy = 3;
            leftGbc.gridwidth = 2;
            leftGbc.fill = GridBagConstraints.BOTH;
            leftPanel.add(GLogoLabel, leftGbc);
        } catch (NullPointerException e) {
            logger.error("Error in SignUpPanel.java: [NullPointerException]: %s".formatted(getStackTraceAsString(e)));
        }
    }

    private void initialize() {
        signUpOverlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape roundedRectangle = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5, 5);
                g2d.setColor(new Color(54, 54, 54, 184));
                g2d.fill(roundedRectangle);
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(0, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.draw(roundedRectangle);
            }
        };
        signUpOverlayPanel.setOpaque(false);
        signUpOverlayPanel.setPreferredSize(new Dimension(847, 721));
        signUpOverlayPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        signUpOverlayPanel.setLayout(new BorderLayout());
        signUpOverlayPanel.setDoubleBuffered(true);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());

        GridBagConstraints leftGbc = new GridBagConstraints();
        leftPanel.setBackground(new Color(255, 255, 255 ,25));
        leftPanel.setPreferredSize(new Dimension(273, signUpOverlayPanel.getHeight()));
        signUpOverlayPanel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBackground(new Color(0,0,0 ,25));
        rightPanel.setPreferredSize(new Dimension(574, signUpOverlayPanel.getHeight()));
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.insets = new Insets(5, 10, 5, 10);
        signUpOverlayPanel.add(rightPanel, BorderLayout.EAST);


        // Title Label
        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setFont(new Font("Fira Code Retina", Font.PLAIN, 34));
        titleLabel.setForeground(Color.WHITE);
        leftGbc.gridx = 0;
        leftGbc.gridy = 1;
        leftGbc.gridwidth = 2;
        leftGbc.insets = new Insets(10,10,30,10);
        leftGbc.fill = GridBagConstraints.NONE;
        leftGbc.anchor = GridBagConstraints.CENTER;
        leftPanel.add(titleLabel, leftGbc);

        // Volunteer logo
        SignReloadImage(leftGbc, leftPanel);

        usernameField = new JTextField(25);
        usernameField.setFont(new Font("Fira Code Retina", Font.PLAIN, 18));
        usernameField.setPreferredSize(new Dimension(300, 45));
        usernameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");
        usernameField.putClientProperty("JTextField.showClearButton", true);
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == ' ') {
                    e.consume();
                }
                if (usernameField.getText().length() > 23){
                    e.consume();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                validateFields(usernameField, passwordField, conformPasswordField, usernameProgressBar, passwordProgressBar, conformPasswordProgressBar, signUpButton);
            }
        });
        usernameField.setMargin(new Insets(10,10,10,10));

        usernameProgressBar = new JProgressBar();
        usernameProgressBar.setMaximum(23);
        usernameProgressBar.putClientProperty("ProgressBar.arc", 30);
        usernameProgressBar.setValue(0);
        usernameProgressBar.setPreferredSize(new Dimension(297, 5));

        UIManager.put("PasswordField.showRevealButton", true);
        passwordField = new JPasswordField(25);

        passwordField.setFont(new Font("Fira Code Retina", Font.PLAIN, 18));
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
                validateFields(usernameField, passwordField, conformPasswordField, usernameProgressBar, passwordProgressBar, conformPasswordProgressBar, signUpButton);
            }
        });
        passwordProgressBar = new JProgressBar();
        passwordProgressBar.putClientProperty("ProgressBar.arc", 30);
        passwordProgressBar.setValue(0);
        passwordProgressBar.setPreferredSize(new Dimension(297, 5));
        UIManager.put("PasswordField.showRevealButton", true);

        conformPasswordField = new JPasswordField(25);
        conformPasswordField.setFont(new Font("Fira Code Retina", Font.PLAIN, 18));
        conformPasswordField.setPreferredSize(new Dimension(300, 45));
        conformPasswordField.setMargin(new Insets(10,10,10,10));
        conformPasswordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Conform Password");
        conformPasswordField.putClientProperty("PasswordField.showCapsLock", true);
        conformPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == ' ') {
                    e.consume();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                validateFields(usernameField, passwordField, conformPasswordField, usernameProgressBar, passwordProgressBar, conformPasswordProgressBar, signUpButton);
            }
        });
        conformPasswordProgressBar = new JProgressBar();
        conformPasswordProgressBar.putClientProperty("ProgressBar.arc", 30);
        conformPasswordProgressBar.setMaximum(20);
        conformPasswordProgressBar.setPreferredSize(new Dimension(297, 5));

        warningLabel = new JLabel();
        warningLabel.setFont(new Font("Fira Code Retina", Font.PLAIN, 14));
        warningLabel.setForeground(Color.RED);
        warningLabel.setVisible(false);

        // Login Button
        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Fira Code Retina", Font.PLAIN, 27));
        signUpButton.setMargin(new Insets(5,30,5,30));
        signUpButton.setEnabled(false);
        signUpButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SignUp(frame, usernameField, passwordField, conformPasswordField, emailField, genderComboBox, roleComboBox, warningLabel);
                }
            }
        });

        signUpButton.addActionListener(_ ->SignUp(frame, usernameField, passwordField, conformPasswordField, emailField, genderComboBox, roleComboBox, warningLabel));

        // Sign Up Label
        JLabel signUpLabel = new JLabel("Have an account?");
        signUpLabel.setFont(new Font("Fira Code Retina", Font.PLAIN, 18));
        signUpLabel.setForeground(Color.WHITE);
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                signUpOverlayPanel.setVisible(false);
                BtwLS(frame, MainPane, "login");
                frame.revalidate();
                frame.repaint();
            }
        });

        emailField = new JTextField(25);
        emailField.setFont(new Font("Fira Code Retina", Font.PLAIN, 18));
        emailField.setPreferredSize(new Dimension(300, 45));
        emailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Email");
        emailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validateFields(usernameField, passwordField, conformPasswordField, usernameProgressBar, passwordProgressBar, conformPasswordProgressBar, signUpButton);

            }
        });
        emailField.setMargin(new Insets(10,10,10,10));


// Gender Field
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        genderComboBox.setFont(new Font("Fira Code Retina", Font.PLAIN, 16));
        genderComboBox.setPreferredSize(new Dimension(300, 45));

// Role Field
        String[] roles = {
                "Role",
                "Firefighter",
                "Police Officer",
                "Paramedics and EMTs",
                "Logistics Coordinators",
                "Communications Specialists",
                "Medical Personnel",
                "Volunteers",
                "Search and Rescue Volunteers",
                "Crisis Counselors",
                "Disaster Manager",
                "Emergency Planner",
                "HAZMAT Teams",
                "Environmental Scientists",
                "Engineers and Construction Workers",
                "Economic Recovery Specialists",
                "Mental Health Professionals",
                "Community Leaders",
                "Red Cross/Red Crescent Workers",
                "NGOs",
                "Social Workers",
                "Others"
        };

        String[] tooltips = {
                "Select the role",
                "Individuals who fight fires and respond to fire emergencies.",
                "Officers who ensure safety and law enforcement during disasters.",
                "Medical professionals providing emergency medical services.",
                "Coordinate logistics for resource distribution and management.",
                "Manage communication and information dissemination during crises.",
                "Provide medical care and support during emergencies.",
                "People who volunteer to help during disasters.",
                "Volunteers who conduct search and rescue operations.",
                "Provide psychological support and counseling during crises.",
                "Manage overall disaster response and recovery efforts.",
                "Plan and prepare for emergency situations and disaster responses.",
                "Teams trained to handle hazardous materials and situations.",
                "Experts who assess and manage environmental impacts of disasters.",
                "Professionals involved in rebuilding infrastructure after disasters.",
                "Specialists focusing on economic recovery and rebuilding efforts.",
                "Provide mental health support and counseling during emergencies.",
                "Leaders within the community who aid in coordination and support.",
                "Workers from Red Cross/Red Crescent providing humanitarian aid.",
                "Non-Governmental Organizations involved in disaster response.",
                "Provide social support and services to affected individuals.",
                "Others"
        };
            roleComboBox = new JComboBox<>(roles);
        roleComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (index >= 0 && index < tooltips.length) {
                    setToolTipText(tooltips[index]);
                } else {
                    setToolTipText(null);
                }
                return component;
            }
        });
        roleComboBox.setFont(new Font("Fira Code Retina", Font.PLAIN, 16));
        roleComboBox.setPreferredSize(new Dimension(300, 45));

        // Username Field
        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        rightGbc.insets = new Insets(40, 10, 5, 10);
        rightPanel.add(usernameField, rightGbc);

        // Username Progress Bar
        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        rightGbc.insets = new Insets(80,10,0,10);
        rightPanel.add(usernameProgressBar, rightGbc);

        // Password Field
        rightGbc.gridx = 0;
        rightGbc.gridy = 2;
        rightGbc.insets = new Insets(10,10,5,10);
        rightPanel.add(passwordField, rightGbc);

        // Password Progress Bar
        rightGbc.gridx = 0;
        rightGbc.gridy = 2;
        rightGbc.insets = new Insets(50,10,0,10);
        rightPanel.add(passwordProgressBar, rightGbc);

        // Confirm Password Field
        rightGbc.gridx = 0;
        rightGbc.gridy = 3;
        rightGbc.insets = new Insets(10,10,5,10);
        rightPanel.add(conformPasswordField, rightGbc);

        // Confirm Password Progress Bar
        rightGbc.gridx = 0;
        rightGbc.gridy = 3;
        rightGbc.insets = new Insets(50,10,0,10);
        rightPanel.add(conformPasswordProgressBar, rightGbc);

        rightGbc.gridx = 0;
        rightGbc.gridy = 4;
        rightGbc.insets = new Insets(10,10,5,10);
        rightPanel.add(emailField, rightGbc);

        rightGbc.gridx = 0;
        rightGbc.gridy = 5;
        rightGbc.insets = new Insets(10,10,5,10);
        rightPanel.add(genderComboBox, rightGbc);

        rightGbc.gridx = 0;
        rightGbc.gridy = 6;
        rightGbc.insets = new Insets(10,10,5,10);
        rightPanel.add(roleComboBox, rightGbc);


        // Sign Up Button
        rightGbc.gridx = 0;
        rightGbc.gridy = 7;
        rightGbc.insets = new Insets(10,10,10,10);
        rightPanel.add(signUpButton, rightGbc);

        // Sign Up Label
        rightGbc.gridx = 0;
        rightGbc.gridy = 8;
        rightGbc.insets = new Insets(10,10,10,10);
        rightPanel.add(signUpLabel, rightGbc);

        // Warning Label
        rightGbc.gridx = 0;
        rightGbc.gridy = 9;
        rightGbc.insets = new Insets(10,10,5,10);
        rightPanel.add(warningLabel, rightGbc);

        assert signUpOverlayPanel != null;
        MainPane.add(signUpOverlayPanel, Integer.valueOf(2));
        signUpOverlayPanel.setVisible(false);

    }
    public JPanel getPanel() {
        return signUpOverlayPanel;
    }
}
