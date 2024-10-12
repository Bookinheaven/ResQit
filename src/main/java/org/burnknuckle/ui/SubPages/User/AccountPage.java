package org.burnknuckle.ui.SubPages.User;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import net.miginfocom.swing.MigLayout;
import org.burnknuckle.ui.subParts.PasswordFieldWithToggle;
import org.burnknuckle.utils.Database;
import raven.datetime.component.date.DatePicker;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.burnknuckle.utils.ThemeManager.getColorFromHex;

public class AccountPage {
//    private final String username = getUsername();
    private final String username = "admin";
    private Map<String, Object> userdata;

    private final int BioLIMIT = 400;
    private JButton saveButtonPersonalInfo;
    private JButton saveButtonAvInfo;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private PasswordFieldWithToggle passwordField;
    private JComboBox<String> genderField;
    private JComboBox<String> roleField;
    private JTextField emailField;
    private JTextArea bioField;
    private DatePicker dobPicker;

    private JTextField countryField;
    private JTextField stateField;
    private JTextField zipCodeField;
    private JTextField roadField;
    private JTextField cityField;
    private JTextField emergencyContactField;
    private JButton saveButtonContactInfo;

    private void checkValueChangeTextField(String value, JTextField textField){
        value = value == null || value.isBlank() ? "Not" : value;
        if(value.equals("Not")){
            textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Not Filled");
            Border paddingBorder = new EmptyBorder(5, 5, 5, 5);
            Border lineBorder = new LineBorder(getColorFromHex("#725555"), 2);
            Border defaultBorder = textField.getBorder();
            textField.setBorder(new CompoundBorder(lineBorder, paddingBorder));

            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    if(textField.getText().isBlank()){
                        textField.setBorder(new CompoundBorder(lineBorder, paddingBorder));
                    } else {
                        textField.setBorder(defaultBorder);
                    }
                }
            });

        } else {
            textField.setText(value);
        }
    }

    public JPanel createAccountPage(JFrame frame) {
        try {
            System.out.println(username);
            Database db = Database.getInstance();
            db.getConnection();
            userdata = db.getUsernameDetails(username);
            System.out.println(userdata.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel backgroundPanel = new JPanel(new GridBagLayout());

        JPanel accountPanel = new JPanel(new BorderLayout());
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                SwingUtilities.invokeLater(() -> {
                    int newWidth = (int) (frame.getWidth() * 0.70);
                    int newHeight = (int) (frame.getHeight() * 0.70);
                    if (newHeight != accountPanel.getHeight() || newWidth != accountPanel.getWidth()) {
                        accountPanel.setPreferredSize(new Dimension(newWidth, newHeight));
                        accountPanel.revalidate();
                        accountPanel.repaint();
                        frame.revalidate();
                        frame.repaint();
                    }
                });
            }
        });

        accountPanel.setPreferredSize(new Dimension(frame.getWidth() / 2, frame.getHeight() / 2));
        accountPanel.setBackground(new Color(100, 100, 100, 100));
        accountPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Account Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(10,20,10,20));
        accountPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel skillsExperienceInfoPanel = new JPanel();
        skillsExperienceInfoPanel.add(new JLabel("Skills and Experience"));

        JPanel healthMedicalInfoPanel = new JPanel();
        healthMedicalInfoPanel.add(new JLabel("Health and Medical Information"));

        JPanel TeamInfoPanel = new JPanel();
        TeamInfoPanel.add(new JLabel("Team Information"));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Personal Info", personalInfoPanel());
        tabs.addTab("Contact Info", contactInfoPanel());
        tabs.addTab("Availability Info", availabilityInfoPanel());
        tabs.addTab("Skills and Experience", skillsExperienceInfoPanel);
        tabs.addTab("Health and Medical Info", healthMedicalInfoPanel);
        tabs.addTab("Team Info", TeamInfoPanel);

        accountPanel.add(tabs, BorderLayout.CENTER);
//        statusLabel = new JLabel();
//        statusLabel.setForeground(Color.RED); // Set color for feedback messages
//        accountPanel.add(statusLabel, "span, grow");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(accountPanel, gbc);
        return backgroundPanel;
    }

    private void saveChangesPersonalInfo() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String password = passwordField.getPassword();
        String gender = (String) genderField.getSelectedItem();
        String role = (String) roleField.getSelectedItem();
        String email = emailField.getText();
        String bio = bioField.getText();
        LocalDate dob = dobPicker.getSelectedDate();

        Map<String, Object> data = new HashMap<>();
        if (!firstName.equals("Not Filled")){
            data.put("first_name", firstName);
        }
        if (!firstName.equals("Not Filled")){
            data.put("last_name", lastName);
        }
        data.put("password", password);
        data.put("gender", gender);
        data.put("role", role);
        data.put("email", email);
        data.put("bio", bio);
        if(dob != null){
            data.put("date_of_birth", Date.valueOf(dob));
        }
        try {
            Database db = Database.getInstance();
            db.getConnection();
            db.updateData12(0,username, data);
            System.out.println("Updated");
            saveButtonPersonalInfo.setVisible(false);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveChangesAvInfo(){
        saveButtonAvInfo.setVisible(false);
    }

    private boolean checkEmptyFields(String field){
        return field.equals("Not Filled");
    }

    private void saveChangesContactInfo(){
        String country = countryField.getText();
        String state = stateField.getText();
        String zipCode = zipCodeField.getText();
        String city = cityField.getText();
        String road = roadField.getText();
        String emergencyContact = emergencyContactField.getText();
        Map<String, Object> data = new HashMap<>();
        if (!checkEmptyFields(country)){
            data.put("country", country);
        }
        if (!checkEmptyFields(state)){
            data.put("state", state);
        }
        if (!checkEmptyFields(zipCode)){
            data.put("zip_code", zipCode);
        }
        if (!checkEmptyFields(city)){
            data.put("city", city);
        }
        if (!checkEmptyFields(road)){
            data.put("road", road);
        }
        if (!checkEmptyFields(emergencyContact)){
            data.put("emergency_contact", emergencyContact);
        }
        try {
            Database db = Database.getInstance();
            db.getConnection();
            db.updateData12(0,username, data);
            System.out.println("Updated Contact Info");
            saveButtonContactInfo.setVisible(false);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private JScrollPane contactInfoPanel(){
        JScrollPane OuterScrollBar = new JScrollPane();
        JPanel contactInfoPanel = new JPanel(new MigLayout("wrap 2", "push[][]push", ""));
        OuterScrollBar.setViewportView(contactInfoPanel);
        saveButtonContactInfo = new JButton("Save");
        saveButtonContactInfo.setVisible(false);
        saveButtonContactInfo.addActionListener(_ -> saveChangesContactInfo());

        JLabel countryLabel = new JLabel("Country");
        countryLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        countryField = new JTextField();
        checkValueChangeTextField((String) userdata.get("country"), countryField);
        countryField.setPreferredSize(new Dimension(400, 30));
        countryField.setFont(new Font("Arial", Font.PLAIN, 14));
        addListenersToFieldsEditMode(countryField, countryLabel, "country", saveButtonContactInfo);
        contactInfoPanel.add(countryLabel, "gaptop 30px, span, grow, align center");
        contactInfoPanel.add(countryField, "span,grow, align center");

        JLabel stateLabel = new JLabel("State");
        stateLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        stateField = new JTextField();
        checkValueChangeTextField((String) userdata.get("state"), stateField);
        stateField.setPreferredSize(new Dimension(400, 30));
        stateField.setFont(new Font("Arial", Font.PLAIN, 14));
        addListenersToFieldsEditMode(stateField, stateLabel, "state", saveButtonContactInfo);
        contactInfoPanel.add(stateLabel, "gaptop 10px, span, grow, align center");
        contactInfoPanel.add(stateField, "span,grow, align center");

        JLabel zipCodeLabel = new JLabel("Zip Code");
        zipCodeLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        zipCodeField = new JTextField();
        checkValueChangeTextField((String) userdata.get("zip_code"), zipCodeField);
        zipCodeField.setPreferredSize(new Dimension(400, 30));
        zipCodeField.setFont(new Font("Arial", Font.PLAIN, 14));
        addListenersToFieldsEditMode(zipCodeField, zipCodeLabel, "zip_code", saveButtonContactInfo);
        contactInfoPanel.add(zipCodeLabel, "gaptop 10px, span, grow, align center");
        contactInfoPanel.add(zipCodeField, "span,grow, align center");


        JLabel cityLabel = new JLabel("City");
        cityLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        cityField = new JTextField();
        checkValueChangeTextField((String) userdata.get("city"), cityField);
        cityField.setPreferredSize(new Dimension(400, 30));
        cityField.setFont(new Font("Arial", Font.PLAIN, 14));
        addListenersToFieldsEditMode(cityField, cityLabel, "city", saveButtonContactInfo);
        contactInfoPanel.add(cityLabel, "gaptop 10px, span, grow, align center");
        contactInfoPanel.add(cityField, "span,grow, align center");

        JLabel roadLabel = new JLabel("Road");
        roadLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        roadField = new JTextField();
        checkValueChangeTextField((String) userdata.get("road"), roadField);
        roadField.setPreferredSize(new Dimension(400, 30));
        roadField.setFont(new Font("Arial", Font.PLAIN, 14));
        addListenersToFieldsEditMode(roadField, roadLabel, "road", saveButtonContactInfo);
        contactInfoPanel.add(roadLabel, "gaptop 10px, span, grow, align center");
        contactInfoPanel.add(roadField, "span,grow, align center");

        JLabel emgLabel = new JLabel("Emergency Contact");
        emgLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        emergencyContactField = new JTextField();
        checkValueChangeTextField((String) userdata.get("emergency_contact"), emergencyContactField);
        emergencyContactField.setPreferredSize(new Dimension(400, 30));
        emergencyContactField.setFont(new Font("Arial", Font.PLAIN, 14));
        addListenersToFieldsEditMode(emergencyContactField, emgLabel, "emergency_contact", saveButtonContactInfo);
        contactInfoPanel.add(emgLabel, "gaptop 10px, span, grow, align center");
        contactInfoPanel.add(emergencyContactField, "span,grow, align center");


        contactInfoPanel.add(saveButtonContactInfo, "span, grow, align center, gaptop 20px, gapbottom 20px");
        return OuterScrollBar;
    }

    private JScrollPane availabilityInfoPanel(){
        JScrollPane OuterScrollBar = new JScrollPane();
        JPanel availabilityInfoPanel = new JPanel(new MigLayout("wrap 2", "push[][]push", ""));
        OuterScrollBar.setViewportView(availabilityInfoPanel);
        saveButtonAvInfo = new JButton("Save");
        saveButtonAvInfo.setVisible(false);
        saveButtonAvInfo.addActionListener(_ -> saveChangesAvInfo());

        availabilityInfoPanel.add(saveButtonAvInfo, "span, grow, align center, gaptop 20px, gapbottom 20px");
        return OuterScrollBar;
    }

    private JScrollPane personalInfoPanel(){
        JScrollPane OuterScrollBar = new JScrollPane();
        JPanel personalInfoPanel = new JPanel(new MigLayout("wrap 2", "push[][]push", ""));
        OuterScrollBar.setViewportView(personalInfoPanel);
        saveButtonPersonalInfo = new JButton("Save");
        saveButtonPersonalInfo.addActionListener(_ -> saveChangesPersonalInfo());

        JLabel accountPrivilegeLabel = new JLabel("Account Privilege");
        accountPrivilegeLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JTextField accountPrivilegeField = new JTextField();
        String privilege = switch ((String)userdata.get("privilege")) {
            case null -> "User";
            case "user" -> "User";
            case "vol"-> "Volunteer";
            case "admin" -> "Administrator";
            case "co-admin" -> "Co Administrator";
            default -> throw new IllegalStateException("Unexpected value: " + userdata.get("privilege"));
        };
        accountPrivilegeField.setText(privilege);
        accountPrivilegeField.setEnabled(false);
        accountPrivilegeField.setPreferredSize(new Dimension(400, 30));
        accountPrivilegeField.setFont(new Font("Arial", Font.PLAIN, 14));

        personalInfoPanel.add(accountPrivilegeLabel, "gaptop 40px, span, grow, align center");
        personalInfoPanel.add(accountPrivilegeField, "span,grow, align center");

        JLabel firstNameLabel = new JLabel("First Name");
        firstNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        firstNameField = new JTextField();
        checkValueChangeTextField((String) userdata.get("first_name"), firstNameField);
        firstNameField.setPreferredSize(new Dimension(400, 30));
        firstNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        addListenersToFieldsEditMode(firstNameField, firstNameLabel, "first_name", saveButtonPersonalInfo);
        personalInfoPanel.add(firstNameLabel, "gaptop 10px, span, grow, align center");
        personalInfoPanel.add(firstNameField, "span,grow, align center");

        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        lastNameField = new JTextField();
        checkValueChangeTextField((String) userdata.get("last_name"), lastNameField);
        lastNameField.setPreferredSize(new Dimension(400, 30));
        lastNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        addListenersToFieldsEditMode(lastNameField, lastNameLabel, "last_name", saveButtonPersonalInfo);
        personalInfoPanel.add(lastNameLabel, "gaptop 10px, span, grow, align center");
        personalInfoPanel.add(lastNameField, "span,grow, align center");

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JTextField usernameField = new JTextField();
        checkValueChangeTextField((String) userdata.get("username"), usernameField);
        usernameField.setEnabled(false);
        usernameField.setPreferredSize(new Dimension(400, 30));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        personalInfoPanel.add(usernameLabel, "gaptop 10px, span, grow, align center");
        personalInfoPanel.add(usernameField, "span,grow, align center");

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new PasswordFieldWithToggle();
        passwordField.setPassword((String) userdata.get("password"));
        passwordField.getPasswordField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkForChanges();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                checkForChanges();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                checkForChanges();
            }
            private void checkForChanges() {
                if (!userdata.get("password").toString().equals(passwordField.getPassword())) {
                    saveButtonPersonalInfo.setVisible(true);
                    if (!passwordLabel.getText().contains("*")) {
                        passwordLabel.setText(passwordLabel.getText().concat("*"));
                    }
                } else {
                    passwordLabel.setText(passwordLabel.getText().replace("*", ""));
                }
            }
        });
        passwordField.setPreferredSize(new Dimension(400, 30));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        personalInfoPanel.add(passwordLabel, "gaptop 10px, span, grow, align center");
        personalInfoPanel.add(passwordField, "span,grow, align center");

        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        genderField = new JComboBox<>();
        genderField.addItem("Male");
        genderField.addItem("Female");
        String gender = (String) userdata.get("gender");
        if (gender != null) {
            genderField.setSelectedItem(gender.equals("Male") ? "Male" : "Female");
        }
        genderField.setPreferredSize(new Dimension(400, 30));
        genderField.setFont(new Font("Arial", Font.PLAIN, 14));
        genderField.addActionListener(_ -> {
            if (!userdata.get("gender").toString().equals(genderField.getSelectedItem().toString())) {
                saveButtonPersonalInfo.setVisible(true);
                if (!genderLabel.getText().contains("*")) {
                    genderLabel.setText(genderLabel.getText().concat("*"));
                }
            } else {
                genderLabel.setText(genderLabel.getText().replace("*", ""));
            }
        });
        personalInfoPanel.add(genderLabel, "gaptop 10px, span, grow, align center");
        personalInfoPanel.add(genderField, "span,grow, align center");
        String[] roles = {
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
        JLabel roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        roleField = new JComboBox<>();
        for(String roleName : roles){
            roleField.addItem(roleName);
        }
        String role = (String) userdata.get("role");
        if (role != null) {
            roleField.setSelectedItem(role);
        }
        roleField.setPreferredSize(new Dimension(400, 30));
        roleField.setFont(new Font("Arial", Font.PLAIN, 14));
        roleField.addActionListener(_ -> {
            if (!userdata.get("role").toString().equals(Objects.requireNonNull(roleField.getSelectedItem()).toString())) {
                saveButtonPersonalInfo.setVisible(true);
                if (!roleLabel.getText().contains("*")) {
                    roleLabel.setText(roleLabel.getText().concat("*"));
                }
            } else {
                roleLabel.setText(roleLabel.getText().replace("*", ""));
            }
        });
        personalInfoPanel.add(roleLabel, "gaptop 10px, span, grow, align center");
        personalInfoPanel.add(roleField, "span,grow, align center");

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        emailField = new JTextField();
        checkValueChangeTextField((String) userdata.get("email"), emailField);
        emailField.setPreferredSize(new Dimension(400, 30));
        addListenersToFieldsEditMode(emailField, emailLabel, "email", saveButtonPersonalInfo);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        personalInfoPanel.add(emailLabel, "gaptop 10px, span, grow, align center");
        personalInfoPanel.add(emailField, "span,grow, align center");

        JLabel bioLabel = new JLabel("Bio (Max %s)".formatted(BioLIMIT));
        bioLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        bioField = new JTextArea();
        String placeholder = "Write something about you";
        String bioText = (String) userdata.get("bio");
        Border defaultBorder = bioField.getBorder();
        Border paddingBorder = new EmptyBorder(5, 5, 5, 5);
        Border lineBorder = new LineBorder(getColorFromHex("#725555"), 2);

        if(bioText == null || bioText.isBlank()){
            bioField.setText(placeholder);
            bioField.setBorder(new CompoundBorder(lineBorder, paddingBorder));
        } else {
            bioField.setText(bioText);
            bioField.setBorder(defaultBorder);
        }
        bioField.setText((bioText == null || bioText.isBlank()) ? placeholder : bioText);
        bioField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (bioField.getText().equals(placeholder)) {
                    bioField.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (bioField.getText().isBlank()) {
                    bioField.setText(placeholder);
                }
            }
        });
        bioField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (bioField.getDocument().getLength() >= BioLIMIT) {
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A) {
                        return;
                    }
                    e.consume();
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (bioField.getDocument().getLength() >= BioLIMIT) {
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A) {
                        return;
                    }
                    e.consume();
                }
                if(bioField.getText().isBlank()){
                    bioField.setBorder(new CompoundBorder(lineBorder, paddingBorder));
                } else {
                    bioField.setBorder(defaultBorder);
                }
            }
        });
        bioField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changes();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changes();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                changes();
            }
            private void changes(){
                if (bioText != null && !bioText.equals(bioField.getText())){
                    saveButtonPersonalInfo.setVisible(true);
                    bioField.setForeground(Color.WHITE);
                    if (!bioLabel.getText().contains("*")) {
                        bioLabel.setText(bioLabel.getText().concat("*"));
                    }
                } else {
                    if (!bioField.getText().equals(placeholder) && !bioField.getText().isBlank()) {
                        saveButtonPersonalInfo.setVisible(true);
                        bioField.setForeground(Color.WHITE);
                        if (!bioLabel.getText().contains("*")) {
                            bioLabel.setText(bioLabel.getText().concat("*"));
                        }
                    } else {
                        bioField.setForeground(Color.GRAY);
                        bioLabel.setText(bioLabel.getText().replace("*", ""));
                    }
                }
            }
        });
        bioField.setPreferredSize(new Dimension(400, 100));
        bioField.setLineWrap(true);
        bioField.setWrapStyleWord(true);
        bioField.setFont(new Font("Arial", Font.PLAIN, 14));
        personalInfoPanel.add(bioLabel, "gaptop 10px, span, grow, align center");
        personalInfoPanel.add(bioField, "span, align center");

        JLabel dobLabel = new JLabel("Date Of Birth");
        dobLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JFormattedTextField dobField = new JFormattedTextField();
        dobPicker = new DatePicker();
        dobPicker.setEditor(dobField);
        String oldDobDate = (userdata.get("date_of_birth") != null) ? userdata.get("date_of_birth").toString() : "Not";
        if (oldDobDate != null && !oldDobDate.isEmpty() && !oldDobDate.equals("Not")){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateTime = LocalDate.parse(oldDobDate, formatter);
            dobPicker.setSelectedDate(dateTime);
        }
        dobField.setPreferredSize(new Dimension(400, 30));
        dobField.setFont(new Font("Arial", Font.PLAIN, 14));
        dobField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkForChanges();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                checkForChanges();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                checkForChanges();
            }
            private void checkForChanges() {
                if ((oldDobDate != null && !oldDobDate.equals("Not")) ||
                        (dobPicker.getSelectedDate() != null &&
                                !dobPicker.getSelectedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).equals(oldDobDate))) {saveButtonPersonalInfo.setVisible(true);
                }
            }
        });
        personalInfoPanel.add(dobLabel, "gaptop 10px, span, grow, align center");
        personalInfoPanel.add(dobField, "span,grow, align center");

        JLabel accountCreatedLabel = new JLabel("Account Created");
        accountCreatedLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JTextField accountCreatedField = new JTextField();
        String accountCreatedDate = ((userdata.get("account_created")) == null )? "Not Filled": formatDateTime((userdata.get("account_created")).toString(), "yyyy-MM-dd HH:mm:ss.SSSSSS", "MMMM dd, yyyy hh:mm a");
        accountCreatedField.setText(accountCreatedDate);
        accountCreatedField.setEnabled(false);
        accountCreatedField.setPreferredSize(new Dimension(400, 30));
        accountCreatedField.setFont(new Font("Arial", Font.PLAIN, 14));

        personalInfoPanel.add(accountCreatedLabel, "gaptop 10px, span, grow, align center");
        personalInfoPanel.add(accountCreatedField, "span,grow, align center");
        SwingUtilities.invokeLater(()->saveButtonPersonalInfo.setVisible(false));
        personalInfoPanel.add(saveButtonPersonalInfo, "span, grow, align center, gaptop 20px, gapbottom 20px");
        return OuterScrollBar;
    }

    public static String formatDateTime(String originalDateTime, String originalFormat, String newFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(originalFormat);

        try {
            LocalDateTime dateTime = LocalDateTime.parse(originalDateTime, formatter);
            DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern(newFormat);
            return dateTime.format(newFormatter);
        } catch (DateTimeParseException e) {
            try {
                LocalDate date = LocalDate.parse(originalDateTime, formatter);
                DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern(newFormat);
                return date.format(newFormatter);
            } catch (DateTimeParseException ex) {
                return "Invalid Date";
            }
        }
    }

    private void addListenersToFieldsEditMode(JTextField field, JLabel label, String get, JButton button){
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkForChanges();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                checkForChanges();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                checkForChanges();
            }
            private void checkForChanges() {
                if (userdata.get(get)!= null && !userdata.get(get).toString().equals(field.getText())) {
                    button.setVisible(true);
                    if (!label.getText().contains("*")) {
                        label.setText(label.getText().concat("*"));
                    }
                } else if (userdata.get(get) == null && !field.getText().isBlank()) {
                    button.setVisible(true);
                    if (!label.getText().contains("*")) {
                        label.setText(label.getText().concat("*"));
                    }
                }else {
                    label.setText(label.getText().replace("*", ""));
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            JFrame f = new JFrame();
            FlatDarkLaf.setup();
            f.setSize(new Dimension(900, 900));
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(new AccountPage().createAccountPage(f));
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });

    }
}
