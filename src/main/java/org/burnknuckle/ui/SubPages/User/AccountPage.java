package org.burnknuckle.ui.SubPages.User;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.burnknuckle.ui.subParts.PasswordFieldWithToggle;
import org.burnknuckle.utils.Database;
import raven.datetime.component.date.DatePicker;

import javax.swing.*;
import javax.swing.border.*;
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

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.ui.SignUpPanel.roles;
import static org.burnknuckle.ui.UserDashboardPanel.checkAccountData;
import static org.burnknuckle.ui.UserDashboardPanel.checkStatusOfUser;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;
import static org.burnknuckle.utils.ThemeManager.*;
import static org.burnknuckle.utils.Userdata.getUsername;

// set defualt valuues and sign up role not select warning
public class AccountPage {
    private final String username = getUsername();
    private CardLayout cardLayout;
    private JPanel mainContent;
    public static JTabbedPane accountTabs = null;
    private JFrame frame;
    private Map<String, Object> userdata;
    private JPanel accountPanel;

    private final int BioLIMIT = 300;
    private final int avaLIMIT = 100;
    private final int phyLIMIT = 100;

    private JButton saveButtonPersonalInfo;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private PasswordFieldWithToggle passwordField;
    private JComboBox<String> genderField;
    private JComboBox<String> roleField;
    private JTextField emailField;
    private JTextArea bioField;
    private DatePicker dobPicker;
    private JTextField phoneNumberField;

    private JTextField countryField;
    private JTextField stateField;
    private JTextField zipCodeField;
    private JTextField roadField;
    private JTextField cityField;
    private JTextField emergencyContactField;
    private JButton saveButtonContactInfo;
    private JTextArea availabilityField;

    private JTextArea physicalField;
    private JTextArea allergicField;
    private JComboBox<String> preferredLocationsField;
    private JComboBox<String> preferredWorkField;
    private JComboBox<String> bloodGroupField;
    private JButton saveButtonHealthInfo;

    private JButton saveButtonSkillInfo;
    private JRadioButton rbtnYes, rbtnNo;
    private JTextArea priorField;
    private JTextArea skillsField;
    private JTextArea langField;
    private JComboBox<String> backgroundField;

    private JButton saveButtonTeamtInfo;

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

    private void runCheck(){
        if(checkAccountData(accountTabs)){
            if(!checkStatusOfUser()){
                cardLayout.show(mainContent, "Volunteer Registration");
            }
        }

    }
    public JPanel createAccountPage(JFrame frame, CardLayout cardLayout, JPanel mainContent) {
        this.frame = frame;
        this.cardLayout = cardLayout;
        this.mainContent = mainContent;
        try {
            Database db = Database.getInstance();
            db.getConnection();
            userdata = db.getUsernameDetails(username);
        } catch (Exception e) {
            logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
        }

        JPanel backgroundPanel = new JPanel(new GridBagLayout());

        accountPanel = new JPanel(new BorderLayout());
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

        accountPanel.setPreferredSize(new Dimension((int) (frame.getWidth() * 0.70), (int) (frame.getHeight() * 0.70)));
        accountPanel.setBackground(getColorFromHex(ADPThemeData.get("accountPanelBg")));
        addThemeChangeListener(_->{
            accountPanel.setBackground(getColorFromHex(ADPThemeData.get("accountPanelBg")));
            accountPanel.revalidate();
            accountPanel.repaint();
            frame.revalidate();
            frame.repaint();

        });
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

        accountTabs = new JTabbedPane();
        accountTabs.addTab("Personal Info", personalInfoPanel());
        accountTabs.addTab("Contact Info", contactInfoPanel());
        accountTabs.addTab("Health and Medical Info", healthMedicalInfoPanel());
        accountTabs.addTab("Skills and Experience", skillsExperienceInfoPanel());
        accountTabs.addChangeListener(_->{
            accountPanel.revalidate();
            accountPanel.repaint();
            frame.revalidate();
            frame.repaint();
        });
        String status = userdata.get("privilege").toString();
        switch (status) {
            case "admin", "co-admin", "vol"-> accountTabs.addTab("Team Info", TeamInfoPanel());
        }
        accountPanel.add(accountTabs, BorderLayout.CENTER);
        
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
        String phone_number = phoneNumberField.getText();

        Map<String, Object> data = new HashMap<>();
        if (!firstName.equals("Not Filled")){
            data.put("first_name", firstName);
        }
        if (!lastName.equals("Not Filled")){
            data.put("last_name", lastName);
        }
        if (!phone_number.equals("Not Filled")){
            data.put("phone_number", phone_number);
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
            runCheck();
        } catch (Exception e){
            logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
        }
    }

    private void saveChangesHealthInfo(){
        String preferred_volunteering_location = Objects.requireNonNull(preferredLocationsField.getSelectedItem()).toString();
        String preferred_volunteering_work = Objects.requireNonNull(preferredWorkField.getSelectedItem()).toString();
        String physical_limitations = physicalField.getText();
        String blood_group = Objects.requireNonNull(bloodGroupField.getSelectedItem()).toString();
        String allergies = allergicField.getText();
        Map<String, Object> data = new HashMap<>();

        if (!allergies.isBlank()){
            data.put("allergies", allergies);
        }
        if (!physical_limitations.isBlank()){
            data.put("physical_limitations", physical_limitations);
        }
        data.put("preferred_volunteering_work", preferred_volunteering_work);
        data.put("preferred_volunteering_location", preferred_volunteering_location);
        data.put("blood_group", blood_group);

        try {
            Database db = Database.getInstance();
            db.getConnection();
            db.updateData12(0,username, data);
            System.out.println("Updated Health Info");
            saveButtonHealthInfo.setVisible(false);
            runCheck();
        } catch (Exception e){
            logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
        }
    }

    private void saveChangesContactInfo(){
        String country = countryField.getText();
        String state = stateField.getText();
        String zipCode = zipCodeField.getText();
        String city = cityField.getText();
        String road = roadField.getText();
        String emergencyContact = emergencyContactField.getText();
        String availability = availabilityField.getText();
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
        if (!checkEmptyFields(availability)){
            data.put("availability", availability);
        }
        try {
            Database db = Database.getInstance();
            db.getConnection();
            db.updateData12(0,username, data);
            System.out.println("Updated Contact Info");
            saveButtonContactInfo.setVisible(false);
            runCheck();
        } catch (Exception e){
            logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
        }
    }

    private void saveChangesSkillsInfo(){
        String willingness = rbtnYes.isSelected() ? "Yes" : rbtnNo.isSelected() ? "No" : "Not specified";
        String languages_spoken = langField.getText();
        String prior_experiences = priorField.getText();
        String skills = skillsField.getText();
        String professional_background = Objects.requireNonNull(backgroundField.getSelectedItem()).toString();
        Map<String, Object> data = new HashMap<>();
        data.put("willingness", willingness);

        if (!languages_spoken.isBlank()){
            data.put("languages_spoken", languages_spoken);
        }
        if (!prior_experiences.isBlank()){
            data.put("prior_experiences", prior_experiences);
        }
        if (!languages_spoken.isBlank()){
            data.put("languages_spoken", languages_spoken);
        }
        if (!skills.isBlank()){
            data.put("skills", skills);
        }
        data.put("professional_background", professional_background);
        try {
            Database db = Database.getInstance();
            db.getConnection();
            db.updateData12(0,username, data);
            System.out.println("Updated Health Info");
            saveButtonHealthInfo.setVisible(false);
            runCheck();
        } catch (Exception e){
            logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
        }
    }

    private boolean checkEmptyFields(String field){
        return field.equals("Not Filled");
    }

    private JLabel LabelCreator(String title){
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    private JTextField LabelTextFieldCreator(JPanel panel, String title, String get, JButton button, boolean setEnable){
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField field = new JTextField();
        checkValueChangeTextField((String) userdata.get(get), field);
        field.setPreferredSize(new Dimension(400, 30));
        field.setEnabled(setEnable);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        addListenersToFieldsEditMode(field, label, get, button);
        panel.add(label, "gaptop 10px, span, grow, align center");
        panel.add(field, "span,grow, align center");
    return field;
    }

    private JScrollPane skillsExperienceInfoPanel(){
        JScrollPane OuterScrollBar = new JScrollPane();
        JPanel skillsExperienceInfoPanel = new JPanel(new MigLayout("wrap 2", "push[][]push", ""));
        OuterScrollBar.setViewportView(skillsExperienceInfoPanel);
        saveButtonSkillInfo = new JButton("Save");
        saveButtonSkillInfo.setVisible(false);
        saveButtonSkillInfo.addActionListener(_ -> saveChangesSkillsInfo());

        JLabel lblWillingness = new JLabel("Willingness to Travel:");
        lblWillingness.setFont(new Font("Inter", Font.PLAIN, 16));

        rbtnYes = new JRadioButton("Yes");
        rbtnNo = new JRadioButton("No");
        ButtonGroup travelGroup = new ButtonGroup();
        travelGroup.add(rbtnYes);
        travelGroup.add(rbtnNo);
        JPanel travelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbtnNo.setFont(new Font("Inter", Font.PLAIN, 16));
        rbtnYes.setFont(new Font("Inter", Font.PLAIN, 16));

        travelPanel.add(rbtnYes);
        travelPanel.add(rbtnNo);
        skillsExperienceInfoPanel.add(lblWillingness, "gaptop 10px, span, grow, align center");
        skillsExperienceInfoPanel.add(travelPanel, "gaptop 10px, span, grow, align center");

        JLabel priorExLabel = LabelCreator("Prior Experiences (Max: %s)".formatted(BioLIMIT));
        priorExLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        skillsExperienceInfoPanel.add(priorExLabel, "gaptop 10px, span, grow, align center");
        priorField = TextAreaCreator(skillsExperienceInfoPanel, priorExLabel, "are you a volunteer?", "prior_experiences", saveButtonSkillInfo, BioLIMIT);

        JLabel skillsLabel = LabelCreator("Skills (Max: %s)".formatted(BioLIMIT));
        skillsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        skillsExperienceInfoPanel.add(skillsLabel, "gaptop 10px, span, grow, align center");
        skillsField = TextAreaCreator(skillsExperienceInfoPanel, skillsLabel, "Write your skills here", "skills", saveButtonSkillInfo, BioLIMIT);

        JLabel langLabel = LabelCreator("Languages Spoken (Max: %s)".formatted(BioLIMIT));
        langLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        skillsExperienceInfoPanel.add(langLabel, "gaptop 10px, span, grow, align center");
        langField = TextAreaCreator(skillsExperienceInfoPanel, langLabel, "Languages you can speak? ", "prior_experiences", saveButtonSkillInfo, BioLIMIT);

        JLabel lblProfessionalBackground = new JLabel("Professional Background:");
        lblProfessionalBackground.setFont(new Font("Inter", Font.PLAIN, 16));

        backgroundField = new JComboBox<>(new String[]{"Medical", "Engineering", "IT", "Construction", "Other"});
        backgroundField.setFont(new Font("Inter", Font.PLAIN, 16));
        String background = (String) userdata.get("professional_background");
        if (background != null) {
            backgroundField.setSelectedItem(background);
        }
        backgroundField.addActionListener(_ -> {
            if (background != null && !background.equals(Objects.requireNonNull(backgroundField.getSelectedItem()).toString())) {
                saveButtonSkillInfo.setVisible(true);
                if (!lblProfessionalBackground.getText().contains("*")) {
                    lblProfessionalBackground.setText(lblProfessionalBackground.getText().concat("*"));
                }
            } else {
                lblProfessionalBackground.setText(lblProfessionalBackground.getText().replace("*", ""));
            }
        });
        skillsExperienceInfoPanel.add(lblProfessionalBackground,"gaptop 10px, span, grow, align center");
        skillsExperienceInfoPanel.add(backgroundField, "gaptop 10px, span, grow, align center");

        skillsExperienceInfoPanel.add(saveButtonSkillInfo, "span, grow, align center, gaptop 20px, gapbottom 20px");
        accountPanel.revalidate();
        accountPanel.repaint();
        return OuterScrollBar;
    }

    private JScrollPane contactInfoPanel(){
        JScrollPane OuterScrollBar = new JScrollPane();
        JPanel contactInfoPanel = new JPanel(new MigLayout("wrap 2", "push[][]push", ""));
        OuterScrollBar.setViewportView(contactInfoPanel);
        saveButtonContactInfo = new JButton("Save");
        saveButtonContactInfo.setVisible(false);
        saveButtonContactInfo.addActionListener(_ -> saveChangesContactInfo());

        countryField = LabelTextFieldCreator(contactInfoPanel,"Country", "country", saveButtonContactInfo, true);
        stateField = LabelTextFieldCreator(contactInfoPanel,"State", "state", saveButtonContactInfo, true);
        zipCodeField = LabelTextFieldCreator(contactInfoPanel,"Zip Code", "zip_code", saveButtonContactInfo, true);
        cityField = LabelTextFieldCreator(contactInfoPanel,"City", "city", saveButtonContactInfo, true);
        roadField = LabelTextFieldCreator(contactInfoPanel,"Road", "road", saveButtonContactInfo, true);
        emergencyContactField = LabelTextFieldCreator(contactInfoPanel,"Emergency Contact", "emergency_contact", saveButtonContactInfo, true);
        JLabel avLabel = LabelCreator("Availability (Max: %s)".formatted(avaLIMIT));
        avLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        contactInfoPanel.add(avLabel, "gaptop 10px, span, grow, align center");
        availabilityField = TextAreaCreator(contactInfoPanel, avLabel, "When are you free? preferred time?", "availability", saveButtonContactInfo, avaLIMIT);
        contactInfoPanel.add(saveButtonContactInfo, "span, grow, align center, gaptop 20px, gapbottom 20px");
        accountPanel.revalidate();
        accountPanel.repaint();
        return OuterScrollBar;
    }

    private JScrollPane TeamInfoPanel() {
        JScrollPane outerScrollBar = new JScrollPane();
        JPanel teamInfoPanel = new JPanel(new MigLayout("wrap 2", "push[][]push", ""));
        outerScrollBar.setViewportView(teamInfoPanel);

        saveButtonTeamtInfo = new JButton("Save");
        saveButtonTeamtInfo.setVisible(false);
        saveButtonTeamtInfo.addActionListener(_ -> saveChangesContactInfo());

        teamInfoPanel.add(saveButtonTeamtInfo, "span, grow, align center, gaptop 20px, gapbottom 20px");

        try {
            Database db = Database.getInstance();
            db.getConnection();

            java.util.List<Map<String, Object>> teams = db.getTeamsForUser(username);
            if (teams != null && !teams.isEmpty()) {
                for (Map<String, Object> team : teams) {
                    JPanel teamPanel = new JPanel(new MigLayout("wrap 2", "push[][]push", "[]10[]10[]"));
                    teamPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1),
                            "Team: " + team.getOrDefault("team_name", "N/A"),
                            TitledBorder.LEFT, TitledBorder.TOP,
                            new Font("inter", Font.BOLD, 16), getColorFromHex(ADPThemeData.get("text"))));
//                    addThemeChangeListener(_->getColorFromHex(ADPThemeData.get("text")));
                    String[] labels = {
                            "Organization Affiliation", "Leader", "Co-Leader", "Phone Number", "Email Address",
                            "Team Address", "Team Type", "Primary Expertise", "Secondary Expertise",
                            "Equipment Resources", "Max Deployment Duration", "Preferred Location",
                            "Previous Operations", "Success Stories", "References", "Training Attended"
                    };
                    String[] keys = {
                            "organization_affiliation", "leader_username", "co_leader_username", "phone_number", "email_address",
                            "team_address", "team_type", "primary_expertise", "secondary_expertise",
                            "equipment_resources", "max_deployment_duration", "preferred_location",
                            "previous_operations", "success_stories", "references_text", "training_attended"
                    };
                    Font labelFont = new Font("inter", Font.BOLD, 14);
                    Font valueFont = new Font("inter", Font.PLAIN, 14);
                    for (int i = 0; i < labels.length; i++) {
                        String value = String.valueOf(team.getOrDefault(keys[i], "N/A"));
                        JLabel TeamLabel = new JLabel(labels[i] + ": ");
                        TeamLabel.setFont(labelFont);
                        TeamLabel.setForeground(getColorFromHex(ADPThemeData.get("text")));
                        JTextArea valueTextArea = new JTextArea(value);
                        valueTextArea.setFont(valueFont);
                        valueTextArea.setForeground(getColorFromHex(ADPThemeData.get("text")));
                        valueTextArea.setLineWrap(true);
                        valueTextArea.setWrapStyleWord(true);
                        valueTextArea.setEditable(false);
                        valueTextArea.setBackground(null);
                        valueTextArea.setBorder(null);
                        valueTextArea.setPreferredSize(new Dimension(300, 40));
                        teamPanel.add(TeamLabel, "align left, gapright 10, gapleft 10");
                        teamPanel.add(valueTextArea, "grow,gaptop 13, wrap");
                        addThemeChangeListener((_)->{
                            TeamLabel.setForeground(getColorFromHex(ADPThemeData.get("text")));
                            valueTextArea.setForeground(getColorFromHex(ADPThemeData.get("text")));

                        });
                    }
                    teamInfoPanel.add(teamPanel, "span, grow, wrap, gaptop 20, gapbottom 20");
                }
            } else {
                teamInfoPanel.add(new JLabel("No teams found."), "span, grow, align center, gaptop 50");
            }
        } catch (Exception e) {
            logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
            teamInfoPanel.add(new JLabel("Error loading team data: " + e.getMessage()), "span, grow, align center");
        }
        accountPanel.revalidate();
        accountPanel.repaint();
        return outerScrollBar;
    }

    private JScrollPane healthMedicalInfoPanel(){
        JScrollPane OuterScrollBar = new JScrollPane();
        JPanel healthInfoPanel = new JPanel(new MigLayout("wrap 2", "push[][]push", ""));
        OuterScrollBar.setViewportView(healthInfoPanel);
        saveButtonHealthInfo = new JButton("Save");
        saveButtonHealthInfo.setVisible(false);
        saveButtonHealthInfo.addActionListener(_ -> saveChangesHealthInfo());

        JLabel phyLabel = LabelCreator("Physical Limitations (Max: %s)".formatted(avaLIMIT));
        phyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        healthInfoPanel.add(phyLabel, "gaptop 10px, span, grow, align center");
        physicalField = TextAreaCreator(healthInfoPanel, phyLabel, "Have Any physical problems?", "physical_limitations", saveButtonHealthInfo, phyLIMIT);

        JLabel allLabel = LabelCreator("Allergies or Medical Conditions (Max: %s)".formatted(avaLIMIT));
        allLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        healthInfoPanel.add(allLabel, "gaptop 10px, span, grow, align center");
        allergicField = TextAreaCreator(healthInfoPanel, allLabel, "Have Any allergic  problems?", "allergies", saveButtonHealthInfo, phyLIMIT);

        JLabel lblBloodGroup = new JLabel("Blood Group");
        lblBloodGroup.setFont(new Font("Inter", Font.PLAIN, 16));

        bloodGroupField = new JComboBox<>(new String[]{"Select","A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        bloodGroupField.setFont(new Font("Inter", Font.PLAIN, 16));
        String bloodGroup = (String) userdata.get("blood_group");
        if (bloodGroup != null) {
            bloodGroupField.setSelectedItem(bloodGroup);
        }
        bloodGroupField.addActionListener(_ -> {
            if (bloodGroup != null && Objects.equals(bloodGroupField.getSelectedItem(), "Select") || !Objects.equals(bloodGroup, Objects.requireNonNull(bloodGroupField.getSelectedItem()).toString())) {
                saveButtonHealthInfo.setVisible(true);
                if (!lblBloodGroup.getText().contains("*")) {
                    lblBloodGroup.setText(lblBloodGroup.getText().concat("*"));
                }
            } else {
                lblBloodGroup.setText(lblBloodGroup.getText().replace("*", ""));
            }
        });
        healthInfoPanel.add(lblBloodGroup, "gaptop 10px, span, grow, align center");
        healthInfoPanel.add(bloodGroupField, "gaptop 10px, span, grow, align center");

        JLabel lblPreferredLocation = new JLabel("Preferred Volunteering Location:");
        lblPreferredLocation.setFont(new Font("Inter", Font.PLAIN, 16));

        preferredLocationsField = new JComboBox<>(new String[]{"Select", "Local", "State", "National"});
        preferredLocationsField.setFont(new Font("Inter", Font.PLAIN, 16));
        String preferredLocation = (String) userdata.get("preferred_volunteering_location");
        if (preferredLocation != null) {
            preferredLocationsField.setSelectedItem(preferredLocation);
        }
        preferredLocationsField.addActionListener(_ -> {
            if (preferredLocation != null && Objects.equals(preferredLocationsField.getSelectedItem(), "Select") || !Objects.equals(preferredLocation, Objects.requireNonNull(preferredLocationsField.getSelectedItem()).toString())) {
                saveButtonHealthInfo.setVisible(true);
                if (!lblPreferredLocation.getText().contains("*")) {
                    lblPreferredLocation.setText(lblPreferredLocation.getText().concat("*"));
                }
            } else {
                lblPreferredLocation.setText(lblPreferredLocation.getText().replace("*", ""));
            }
        });
        healthInfoPanel.add(lblPreferredLocation, "gaptop 10px, span, grow, align center");
        healthInfoPanel.add(preferredLocationsField, "gaptop 10px, span, grow, align center");

        JLabel lblVolunteeringWork = new JLabel("Preferred Volunteering Work");
        lblVolunteeringWork.setFont(new Font("Inter", Font.PLAIN, 16));
        preferredWorkField = new JComboBox<>(new String[]{
                "Select",
                "Rescue Operations",
                "Medical Assistance",
                "Food Distribution",
                "Shelter Support",
                "Environmental Conservation",
                "Education and Tutoring",
                "Community Outreach",
                "Disaster Relief and Crisis Management",
                "Elderly Care Support",
                "Child Welfare and Development",
                "Sports and Recreation",
                "Animal Welfare",
                "Technology Support",
                "Fundraising and Event Management",
                "Health and Hygiene Education",
                "Cultural Preservation"
        });
        preferredWorkField.setFont(new Font("Inter", Font.PLAIN, 16));
        String preferredWork = (String) userdata.get("preferred_volunteering_work");
        if (preferredWork != null) {
            preferredWorkField.setSelectedItem(preferredWork);
        }
        preferredWorkField.addActionListener(_ -> {
            if (preferredWork != null && Objects.equals(preferredWorkField.getSelectedItem(), "Select") || !Objects.equals(preferredWork, Objects.requireNonNull(preferredWorkField.getSelectedItem()).toString())) {
                saveButtonHealthInfo.setVisible(true);
                if (!lblVolunteeringWork.getText().contains("*")) {
                    lblVolunteeringWork.setText(lblVolunteeringWork.getText().concat("*"));
                }
            } else {
                lblVolunteeringWork.setText(lblVolunteeringWork.getText().replace("*", ""));
            }
        });
        healthInfoPanel.add(lblVolunteeringWork, "gaptop 10px, span, grow, align center");
        healthInfoPanel.add(preferredWorkField, "gaptop 10px, span, grow, align center");

        healthInfoPanel.add(saveButtonHealthInfo, "span, grow, align center, gaptop 20px, gapbottom 20px");
        accountPanel.revalidate();
        accountPanel.repaint();
        return OuterScrollBar;
    }

    private JTextArea TextAreaCreator(JPanel panel, JLabel label, String placeholder, String get, JButton button, int limit){
        JTextArea fieldArea = new JTextArea();
        String originalText = (String) userdata.get(get);
        Border defaultBorder = fieldArea.getBorder();
        Border paddingBorder = new EmptyBorder(5, 5, 5, 5);
        Border lineBorder = new LineBorder(getColorFromHex("#725555"), 2);

        if(originalText == null || originalText.isBlank()){
            fieldArea.setText(placeholder);
            fieldArea.setBorder(new CompoundBorder(lineBorder, paddingBorder));
        } else {
            fieldArea.setText(originalText);
            fieldArea.setBorder(defaultBorder);
        }
        fieldArea.setText((originalText == null || originalText.isBlank()) ? placeholder : originalText);
        fieldArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (fieldArea.getText().equals(placeholder)) {
                    fieldArea.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (fieldArea.getText().isBlank()) {
                    fieldArea.setText(placeholder);
                }
            }
        });
        fieldArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (fieldArea.getDocument().getLength() >= limit) {
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A) {
                        return;
                    }
                    e.consume();
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (fieldArea.getDocument().getLength() >= limit) {
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A) {
                        return;
                    }
                    e.consume();
                }
                if(fieldArea.getText().isBlank()){
                    fieldArea.setBorder(new CompoundBorder(lineBorder, paddingBorder));
                } else {
                    fieldArea.setBorder(defaultBorder);
                }
            }
        });
        fieldArea.getDocument().addDocumentListener(new DocumentListener() {
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
                if (originalText != null && !originalText.equals(fieldArea.getText())){
                    button.setVisible(true);
                    fieldArea.setForeground(Color.WHITE);
                    if (!label.getText().contains("*")) {
                        label.setText(label.getText().concat("*"));
                    }
                } else {
                    if (!fieldArea.getText().equals(placeholder) && !fieldArea.getText().isBlank()) {
                        button.setVisible(true);
                        fieldArea.setForeground(Color.WHITE);
                        if (!label.getText().contains("*")) {
                            label.setText(label.getText().concat("*"));
                        }
                    } else {
                        fieldArea.setForeground(Color.GRAY);
                        label.setText(label.getText().replace("*", ""));
                    }
                }
            }
        });
        fieldArea.setPreferredSize(new Dimension(400, 100));
        fieldArea.setLineWrap(true);
        fieldArea.setWrapStyleWord(true);
        fieldArea.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(fieldArea, "span, align center");
        return fieldArea;

    }
    
    private JScrollPane personalInfoPanel(){
        JScrollPane OuterScrollBar = new JScrollPane();
        JPanel personalInfoPanel = new JPanel(new MigLayout("wrap 2", "push[][]push", ""));
        saveButtonPersonalInfo = new JButton("Save");
        saveButtonPersonalInfo.addActionListener(_ -> saveChangesPersonalInfo());

        JLabel accountPrivilegeLabel = LabelCreator("Account Privilege");
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

        firstNameField = LabelTextFieldCreator(personalInfoPanel, "First Name", "first_name",saveButtonPersonalInfo, true);
        lastNameField = LabelTextFieldCreator(personalInfoPanel, "Last Name", "last_name",saveButtonPersonalInfo, true);

        LabelTextFieldCreator(personalInfoPanel, "Username", "username",saveButtonPersonalInfo, false);

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
            if (gender != null && !gender.equals(genderField.getSelectedItem().toString())) {
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
        roleField.setRenderer(new DefaultListCellRenderer() {
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
        roleField.setFont(new Font("Arial", Font.PLAIN, 14));
        roleField.addActionListener(_ -> {
            if (role != null && !role.equals(Objects.requireNonNull(roleField.getSelectedItem()).toString())) {
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
        emailField = LabelTextFieldCreator(personalInfoPanel, "Email", "email",saveButtonPersonalInfo, true);

        JLabel bioLabel = LabelCreator("Bio (Max: %s)".formatted(BioLIMIT));
        bioLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        personalInfoPanel.add(bioLabel, "gaptop 10px, span, grow, align center");
        bioField = TextAreaCreator(personalInfoPanel, bioLabel, "Write something about you", "bio", saveButtonPersonalInfo, BioLIMIT);
        personalInfoPanel.add(saveButtonPersonalInfo, "span, grow, align center, gaptop 20px, gapbottom 20px");


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
        String accountCreatedDate = ((userdata.get("account_created")) == null )? "Not Filled": formatDateTime((userdata.get("account_created")).toString(), "yyyy-MM-dd HH:mm:ss.SSS", "MMMM dd, yyyy hh:mm a");
        accountCreatedField.setText(accountCreatedDate);
        accountCreatedField.setEnabled(false);
        accountCreatedField.setPreferredSize(new Dimension(400, 30));
        accountCreatedField.setFont(new Font("Arial", Font.PLAIN, 14));

        personalInfoPanel.add(accountCreatedLabel, "gaptop 10px, span, grow, align center");
        personalInfoPanel.add(accountCreatedField, "span,grow, align center");

        phoneNumberField = LabelTextFieldCreator(personalInfoPanel,"Phone number", "phone_number", saveButtonPersonalInfo, true);

        personalInfoPanel.add(saveButtonPersonalInfo, "span, grow, align center, gaptop 20px, gapbottom 20px");
        SwingUtilities.invokeLater(()-> {
            saveButtonPersonalInfo.setVisible(false);
            OuterScrollBar.setViewportView(personalInfoPanel);
        });
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
}
