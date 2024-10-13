package org.burnknuckle.ui.SubPages.User;

import com.formdev.flatlaf.FlatDarkLaf;
import net.miginfocom.swing.MigLayout;
import org.burnknuckle.ui.subParts.LoginBgPanel;
import org.burnknuckle.utils.Database;
import raven.datetime.component.date.DatePicker;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.burnknuckle.utils.Resources.*;
import static org.burnknuckle.utils.ThemeManager.getColorFromHex;
import static org.burnknuckle.utils.Userdata.getUsername;

public class TeamCreationPage {
    private JComboBox<String> cmbTeamType;
    private static JButton addMemberButton;
    private final String username = getUsername();
//    private String username = "tanvik123";
    private static JPanel teamMembersUsernamesPanel = null;
    private static ArrayList<MemberPanel> memberPanels = new ArrayList<>();

    private DatePicker startPicker;
    private DatePicker endPicker;
    private JTextField txtTeamName, txtOrganizationAffiliation,txtCoLeaderName
            , txtLeaderName, txtPhoneNumber, txtEmailAddress, txtTeamAddress;
    private JTextField txtPrimaryExpertise, txtSecondaryExpertise;
    private JTextArea txtEquipmentResources;

    private JTextField txtPreferredLocation, txtMaxDeploymentDuration;

    private JTextArea txtPreviousOperations, txtSuccessStories, txtReferences;

    private JTextArea txtTrainingAttended;

    private JCheckBox chkBackgroundCheckConsent, chkGuidelinesAgreement, chkLiabilityWaiver, chkMediaReleaseConsent;

    private void submitTeamInfo() {
        String teamName = txtTeamName.getText().trim();
        String organizationAffiliation = txtOrganizationAffiliation.getText().trim();
        String leaderName = txtLeaderName.getText().trim();
        String coLeaderName = txtCoLeaderName.getText().trim();
        String phoneNumber = txtPhoneNumber.getText().trim();
        String emailAddress = txtEmailAddress.getText().trim();
        String teamAddress = txtTeamAddress.getText().trim();
        String teamType = (cmbTeamType.getSelectedItem() != null) ? cmbTeamType.getSelectedItem().toString().trim() : "";

        String primaryExpertise = txtPrimaryExpertise.getText().trim();
        String secondaryExpertise = txtSecondaryExpertise.getText().trim();
        String equipmentResources = txtEquipmentResources.getText().trim();
        String preferredLocation = txtPreferredLocation.getText().trim();
        String previousOperations = txtPreviousOperations.getText().trim();
        String successStories = txtSuccessStories.getText().trim();
        String references = txtReferences.getText().trim();
        String trainingAttended = txtTrainingAttended.getText().trim();
        int maxDeploymentDuration = 0;
        try {
            maxDeploymentDuration = Integer.parseInt(txtMaxDeploymentDuration.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Max Deployment Duration must be a valid number!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (teamName.isEmpty()) {
            showError("Team Name");
            return;
        }
        if (organizationAffiliation.isEmpty()) {
            showError("Organization Affiliation");
            return;
        }
        if (leaderName.isEmpty()) {
            showError("Leader Name");
            return;
        }
        if (phoneNumber.isEmpty()) {
            showError("Phone Number");
            return;
        }
        if (emailAddress.isEmpty()) {
            showError("Email Address");
            return;
        }
        if (teamAddress.isEmpty()) {
            showError("Team Address");
            return;
        }
        if (teamType.isEmpty()) {
            showError("Team Type");
            return;
        }
        LocalDate availabilityStartDate = startPicker.getSelectedDate();
        LocalDate availabilityEndDate = endPicker.getSelectedDate();
        Map<String, Object> data = new HashMap<>();
        data.put("team_registered", new Timestamp(System.currentTimeMillis()));
        if (availabilityStartDate != null) {
            data.put("availability_start", Date.valueOf(availabilityStartDate));
        }
        if (availabilityEndDate != null) {
            data.put("availability_end", Date.valueOf(availabilityEndDate));
        }
        StringBuilder users = new StringBuilder();
        if (memberPanels != null) {
            for (MemberPanel memberPanel : memberPanels) {
                String user = memberPanel.txtMemberUsername.getText().trim();
                if (!user.isBlank()) {
                    users.append(user).append(" ");
                }
            }
        }
        data.put("team_name", teamName);
        data.put("organization_affiliation", organizationAffiliation);
        data.put("leader_username", leaderName);
        data.put("co_leader_username", coLeaderName);
        data.put("phone_number", phoneNumber);
        data.put("email_address", emailAddress);
        data.put("team_address", teamAddress);
        data.put("members", users.toString().trim());
        data.put("team_type", teamType);
        data.put("primary_expertise", primaryExpertise);
        data.put("secondary_expertise", secondaryExpertise);
        data.put("equipment_resources", equipmentResources);
        data.put("preferred_location", preferredLocation);
        data.put("max_deployment_duration", maxDeploymentDuration);
        data.put("previous_operations", previousOperations);
        data.put("success_stories", successStories);
        data.put("references_text", references);
        data.put("training_attended", trainingAttended);
        data.put("guidelines_agreement", chkGuidelinesAgreement.isSelected());
        data.put("background_check_consent", chkBackgroundCheckConsent.isSelected());
        data.put("liability_waiver", chkLiabilityWaiver.isSelected());
        data.put("media_release_consent", chkMediaReleaseConsent.isSelected());
        try {
            Database db = Database.getInstance();
            db.getConnection();
            if (db.checkForDuplicateTeams(data)) {
                JOptionPane.showMessageDialog(null, "Team Information failed! Team already exists.", "Validation Error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            db.insertData(3, data);
            JOptionPane.showMessageDialog(null, "Team Information successfully submitted!", "Success", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Team Information submitted successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error submitting team information: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private void showError(String fieldName) {
        JOptionPane.showMessageDialog(null, fieldName + " is required and cannot be empty!", "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    private JTextField FieldsCreator(JPanel centerForm, String title, JTextField field){
        field = new JTextField(20);
        JLabel teamName = new JLabel(title);
        teamName.setFont(new Font("Inter", Font.PLAIN, 15));
        field.setFont(new Font("Inter", Font.PLAIN, 15));
        centerForm.add(teamName, "gapright 10");
        centerForm.add(field);
        return field;
    }
    public JPanel createTeamCreationPage() {
        JPanel inner = new JPanel(new BorderLayout());
        LoginBgPanel OuterBgPanel = new LoginBgPanel("Common/formPagesBg.jpg");
        OuterBgPanel.setLayout(new BorderLayout());
        inner.setOpaque(false);
        JScrollPane scrollablePanel = new JScrollPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape roundedRectangle = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5, 5);
                g2d.setColor(new Color(0, 0, 0, 94));
                g2d.fill(roundedRectangle);
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(0, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.draw(roundedRectangle);
            }
        };
        JPanel centerForm = new JPanel(new MigLayout("wrap 2", "[400][fill, grow]"));
        centerForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Team Creation Form");
        title.setFont(new Font("Inter", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topBar.add(title);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        topBar.add(separator);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        JSeparator separator1 = new JSeparator(JSeparator.HORIZONTAL);
        centerForm.add(separator1, "gaptop 10, span 2, grow, gapbottom 10");

        JLabel teamInfoTitle = new JLabel("Team Information");
        teamInfoTitle.setFont(new Font("Inter", Font.BOLD, 20));
        centerForm.add(teamInfoTitle, "span, grow, wrap");

        txtTeamName = FieldsCreator(centerForm, "Team Name", txtTeamName);

        txtOrganizationAffiliation = FieldsCreator(centerForm, "Organization Affiliation", txtOrganizationAffiliation);

        txtLeaderName = FieldsCreator(centerForm, "Team Leader's Username", txtLeaderName);
        txtLeaderName.setText(username);
        txtLeaderName.setEnabled(false);

        txtCoLeaderName = FieldsCreator(centerForm, "Team CoLeader's Username", txtCoLeaderName);

        JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
        centerForm.add(separator2, "span, grow, gaptop 10px, gapbottom 10px");

        JLabel ContactInfoLabel = new JLabel("Team Contact Info");
        ContactInfoLabel.setFont(new Font("Inter", Font.BOLD, 20));
        centerForm.add(ContactInfoLabel, "span, grow, wrap");

        txtPhoneNumber = FieldsCreator(centerForm, "Phone Number", txtPhoneNumber);
        txtPhoneNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && txtPhoneNumber.getText().length() < 15) {
                    e.consume();
                }
            }
        });
        txtEmailAddress = FieldsCreator(centerForm, "Gmail", txtEmailAddress);

        JSeparator separator3 = new JSeparator(SwingConstants.HORIZONTAL);
        centerForm.add(separator3, "span, grow, gaptop 10px, gapbottom 10px");

        txtTeamAddress = FieldsCreator(centerForm, "Team Address (Street, City, State, Zip)", txtTeamAddress);

        String[] teamTypes = {"Medical", "Search and Rescue", "Logistics", "Psychological Support"};
        cmbTeamType = new JComboBox<>(teamTypes);
        cmbTeamType.setFont(new Font("Inter", Font.PLAIN, 15));

        JLabel teamTpLabel = new JLabel("Type of Team:");
        teamTpLabel.setFont(new Font("Inter", Font.PLAIN, 16));

        centerForm.add(teamTpLabel);
        centerForm.add(cmbTeamType);

        JSeparator separator4 = new JSeparator(SwingConstants.HORIZONTAL);
        centerForm.add(separator4, "span, grow, gaptop 10px, gapbottom 10px");

        JLabel teamMembersInfoLabel = new JLabel("Team Members' Info");
        teamMembersInfoLabel.setFont(new Font("Inter", Font.BOLD, 20));
        centerForm.add(teamMembersInfoLabel, "span, grow, wrap");

        teamMembersUsernamesPanel = new JPanel();
        teamMembersUsernamesPanel.setLayout(new BoxLayout(teamMembersUsernamesPanel, BoxLayout.Y_AXIS));
        teamMembersUsernamesPanel.setBorder(new TitledBorder("Members List"));
        teamMembersUsernamesPanel.setPreferredSize(new Dimension(centerForm.getWidth(), 50));

        addMemberButton = new JButton("Add Member");
        addMemberButton.addActionListener(e -> addMember(teamMembersUsernamesPanel));
        centerForm.add(addMemberButton, "span, grow");

        centerForm.add(teamMembersUsernamesPanel, "span, grow, wrap");

        // Team’s Area of Expertise
        JSeparator separator5 = new JSeparator(SwingConstants.HORIZONTAL);
        centerForm.add(separator5, "span, grow, gaptop 10px, gapbottom 10px");

        JLabel ExpertiseInfoLabel = new JLabel("Team's Area of Expertise");
        ExpertiseInfoLabel.setFont(new Font("Inter", Font.BOLD, 20));
        centerForm.add(ExpertiseInfoLabel, "span, grow, wrap");

        txtPrimaryExpertise = FieldsCreator(centerForm, "Primary Expertise", txtPrimaryExpertise);
        txtSecondaryExpertise = FieldsCreator(centerForm, "Secondary Expertise", txtSecondaryExpertise);

        txtEquipmentResources = new JTextArea(3, 20);
        txtEquipmentResources.setFont(new Font("Inter", Font.PLAIN, 16));
        JLabel EquipmentInfoLabel = new JLabel("Equipment and Resources Available");
        EquipmentInfoLabel.setFont(new Font("Inter", Font.PLAIN, 15));
        txtEquipmentResources.setLineWrap(true);
        centerForm.add(EquipmentInfoLabel);
        centerForm.add(new JScrollPane(txtEquipmentResources));

        // Availability and Deployment Preferences
        JSeparator separator6 = new JSeparator(SwingConstants.HORIZONTAL);
        centerForm.add(separator6, "span, grow, gaptop 10px, gapbottom 10px");

        JLabel AvailabilityInfoLabel = new JLabel("Availability and Deployment Preferences");
        AvailabilityInfoLabel.setFont(new Font("Inter", Font.BOLD, 20));
        centerForm.add(AvailabilityInfoLabel, "span, grow, wrap");

        JPanel availabilityPanel = new JPanel(new MigLayout("fill", "[][]", "[]"));
        JFormattedTextField txtAvailabilityStart = new JFormattedTextField();
        JFormattedTextField txtAvailabilityEnd = new JFormattedTextField();

        startPicker = new DatePicker();
        startPicker.setEditor(txtAvailabilityStart);

        endPicker = new DatePicker();
        endPicker.setEditor(txtAvailabilityEnd);
        availabilityPanel.add(txtAvailabilityStart, "grow");
        availabilityPanel.add(txtAvailabilityEnd, "grow");

        JLabel AvailabilityLabel = new JLabel("Availability Dates (Start - End)");
        AvailabilityLabel.setFont(new Font("Inter", Font.PLAIN, 15));
        centerForm.add(AvailabilityLabel);
        centerForm.add(availabilityPanel, "span, grow, wrap");

        txtPreferredLocation = new JTextField(20);

        JLabel PreferredLabel = new JLabel("Preferred Deployment Location");
        PreferredLabel.setFont(new Font("Inter", Font.PLAIN, 15));
        centerForm.add(PreferredLabel);
        centerForm.add(txtPreferredLocation,"span, grow, wrap");

        txtMaxDeploymentDuration = new JTextField(10);
        JLabel MaxDeLabel = new JLabel("Max Deployment Duration");
        MaxDeLabel.setFont(new Font("Inter", Font.PLAIN, 15));
        centerForm.add(MaxDeLabel);
        txtMaxDeploymentDuration.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
        centerForm.add(txtMaxDeploymentDuration,"span, grow, wrap");

        JSeparator separator7 = new JSeparator(SwingConstants.HORIZONTAL);
        centerForm.add(separator7, "span, grow, gaptop 10px, gapbottom 10px");

        JLabel PastInfoLabel = new JLabel("Team's Past Experience");
        PastInfoLabel.setFont(new Font("Inter", Font.BOLD, 20));
        centerForm.add(PastInfoLabel, "span, grow, wrap");

        txtPreviousOperations = new JTextArea(3, 20);
        JLabel PreviousLabel = new JLabel("Previous Disaster Relief Operations");
        PreviousLabel.setFont(new Font("Inter", Font.PLAIN, 15));
        centerForm.add(PreviousLabel);
        centerForm.add(new JScrollPane(txtPreviousOperations), "span, grow, wrap");

        txtSuccessStories = new JTextArea(2, 20);
        JLabel SuccessLabel = new JLabel("Success Stories");
        SuccessLabel.setFont(new Font("Inter", Font.PLAIN, 15));
        centerForm.add(SuccessLabel);
        centerForm.add(new JScrollPane(txtSuccessStories), "span, grow, wrap");

        txtReferences = new JTextArea(2, 20);
        JLabel feedLabel = new JLabel("Feedback/References");
        feedLabel.setFont(new Font("Inter", Font.PLAIN, 15));
        centerForm.add(feedLabel);
        centerForm.add(new JScrollPane(txtReferences), "span, grow, wrap");

        txtTrainingAttended = new JTextArea(2, 20);
        JLabel trainLabel = new JLabel("Training Attended");
        trainLabel.setFont(new Font("Inter", Font.PLAIN, 15));
        centerForm.add(trainLabel);
        centerForm.add(new JScrollPane(txtTrainingAttended), "span, grow, wrap");

        // Legal and Consent
        JSeparator separator8 = new JSeparator(SwingConstants.HORIZONTAL);
        centerForm.add(separator8, "span, grow, gaptop 10px, gapbottom 10px");

        JLabel preparednessInfoLabel = new JLabel("Legal and Consent");
        preparednessInfoLabel.setFont(new Font("Inter", Font.BOLD, 20));
        centerForm.add(preparednessInfoLabel, "span, grow, wrap");

        chkBackgroundCheckConsent = new JCheckBox("Consent for Background Check");
        chkBackgroundCheckConsent.setFont(new Font("Inter", Font.PLAIN, 15));
        centerForm.add(chkBackgroundCheckConsent, "span, grow");
        chkGuidelinesAgreement = new JCheckBox("Agree to Follow Guidelines");
        chkGuidelinesAgreement.setFont(new Font("Inter", Font.PLAIN, 15));
        centerForm.add(chkGuidelinesAgreement, "span, grow");
        chkLiabilityWaiver = new JCheckBox("Liability Waiver");
        chkLiabilityWaiver.setFont(new Font("Inter", Font.PLAIN, 15));
        centerForm.add(chkLiabilityWaiver, "span, grow");
        chkMediaReleaseConsent = new JCheckBox("Consent for Media Release");
        chkMediaReleaseConsent.setFont(new Font("Inter", Font.PLAIN, 15));
        centerForm.add(chkMediaReleaseConsent, "span, grow");

        JSeparator separator9 = new JSeparator(SwingConstants.HORIZONTAL);
        centerForm.add(separator9, "span, grow, gaptop 10px, gapbottom 10px");

        JButton submitButton = new JButton("Submit Team Info");
        submitButton.setFont(new Font("Inter", Font.BOLD, 16));
        submitButton.setPreferredSize(new Dimension(200, 40));
        submitButton.addActionListener(e -> submitTeamInfo());
        centerForm.add(submitButton, "span, align center, gapbottom 30px");

        inner.add(topBar, BorderLayout.NORTH);
        inner.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        wrapper.add(centerForm);

        scrollablePanel.setViewportView(wrapper);
        scrollablePanel.setOpaque(false);

        OuterBgPanel.add(inner, BorderLayout.NORTH);
        scrollablePanel.getViewport().setOpaque(false);

        OuterBgPanel.add(scrollablePanel, BorderLayout.CENTER);
        return OuterBgPanel;
    }
    private static boolean checkMemberExist(JPanel currentPanel) {
        if (!memberPanels.isEmpty()) {
            MemberPanel target = null;
            for (MemberPanel x : memberPanels) {
                if (x.panel.equals(currentPanel)) {
                    target = x;
                    break;
                }
            }
            if (target != null) {
                boolean isDuplicate = false;
                for (MemberPanel existingMember : memberPanels) {
                    if (!existingMember.panel.equals(currentPanel) &&
                            target.txtMemberUsername.getText().equals(existingMember.txtMemberUsername.getText())) {

                        JDialog dialog = new JDialog((Frame) null, "Duplicate Username", true);
                        dialog.setLayout(new BorderLayout());
                        JLabel text = new JLabel("Username already exists!");
                        text.setFont(new Font("Inter", Font.PLAIN, 15));
                        dialog.add(text, BorderLayout.CENTER);

                        JButton okButton = new JButton("OK");
                        okButton.addActionListener(e -> dialog.dispose());
                        dialog.add(okButton, BorderLayout.SOUTH);

                        dialog.pack();
                        dialog.setSize(new Dimension(200, 150));
                        dialog.setLocationRelativeTo(null);
                        dialog.setVisible(true);

                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    target.txtMemberUsername.setEnabled(false);
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private void addMember(JPanel formPanel) {
        addMemberButton.setEnabled(false);
        int memberNumber = memberPanels.size() + 1;
        MemberPanel memberPanel = new MemberPanel();
        formPanel.add(memberPanel.createMemberPanel(memberNumber), "span, grow, wrap");
        memberPanels.add(memberPanel);
        formPanel.revalidate();
        formPanel.repaint();
        formPanel.getParent().revalidate();
        formPanel.getParent().repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Team Creation Page");
        FlatDarkLaf.setup();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 1000);
        TeamCreationPage teamCreationPage = new TeamCreationPage();
        frame.add(teamCreationPage.createTeamCreationPage());
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    private static void updateMemberNumbers() {
        int number = 1;  // Start numbering from 1
        for (MemberPanel panel : memberPanels) {
            panel.updateMemberNumber(number);  // Update each panel's number
            number++;
        }
    }
    private static class MemberPanel {
        private JTextField txtMemberUsername;
        private JPanel panel;
        private JButton DeleteButton;
        private JButton EditButton;
        private JButton SaveButton;
        public void updateMemberNumber(int newNumber) {
            panel.setBorder(BorderFactory.createTitledBorder("Member " + newNumber));
        }
        public JPanel createMemberPanel(int no) {
            JPanel memberPanel = new JPanel(new MigLayout("wrap 5", "[150][fill, grow][][][]"));
            memberPanel.setBorder(new TitledBorder("Member %s".formatted(no)));
            memberPanel.setFont(new Font("Inter", Font.PLAIN, 15));

            JLabel usernameLabel = new JLabel("Username:");
            usernameLabel.setFont(new Font("Inter", Font.PLAIN, 15));

            txtMemberUsername = new JTextField(20);
            Border defaultBorder = txtMemberUsername.getBorder();
            txtMemberUsername.setFont(new Font("Inter", Font.PLAIN, 15));

            Border lineBorder = new LineBorder(getColorFromHex("#9cfe98"), 2);
            Border paddingBorder = new EmptyBorder(5, 5, 5, 5);

            memberPanel.add(usernameLabel);
            memberPanel.add(txtMemberUsername);

            DeleteButton = new JButton();
            DeleteButton.setToolTipText("Delete");
            DeleteButton.setIcon(deleteButtonIcon);
            DeleteButton.setEnabled(false);
            DeleteButton.setPreferredSize(new Dimension(20,20));
            memberPanel.add(DeleteButton);
            DeleteButton.addActionListener(_ -> {
                for (MemberPanel x : memberPanels) {
                    if (x.panel.equals(memberPanel)) {
                        memberPanels.remove(x);
                        break;
                    }
                }
                updateMemberNumbers();
                teamMembersUsernamesPanel.remove(memberPanel);
                teamMembersUsernamesPanel.revalidate();
                teamMembersUsernamesPanel.repaint();
            });

            EditButton = new JButton();
            EditButton.setToolTipText("Edit");
            EditButton.setIcon(editButtonIcon);
            EditButton.setEnabled(false);
            EditButton.setPreferredSize(new Dimension(20,20));
            EditButton.addActionListener(_ -> {
                for (MemberPanel x : memberPanels) {
                    if (x.panel.equals(memberPanel)) {
                        x.SaveButton.setEnabled(true);
                        x.txtMemberUsername.setEnabled(true);
                        break;
                    }
                }
                teamMembersUsernamesPanel.revalidate();
                teamMembersUsernamesPanel.repaint();
            });
            memberPanel.add(EditButton);

            SaveButton = new JButton();
            SaveButton.setToolTipText("Save");
            SaveButton.setIcon(saveButtonIcon);
            SaveButton.addActionListener(_->{
                if(!checkMemberExist(memberPanel) && !txtMemberUsername.getText().equals(getUsername())){
                    SaveButton.setEnabled(false);
                    txtMemberUsername.setEnabled(false);
                    addMemberButton.setEnabled(true);
                    EditButton.setEnabled(true);
                    DeleteButton.setEnabled(true);
                } else {
                    txtMemberUsername.setEnabled(true);

                }
            });
            SaveButton.setPreferredSize(new Dimension(20,20));
            memberPanel.add(SaveButton);

            txtMemberUsername.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    try{
                        Database db = Database.getInstance();
                        db.getConnection();
                        if(db.isUser(txtMemberUsername.getText())){
                            txtMemberUsername.setBorder(new CompoundBorder(lineBorder, paddingBorder));
                            addMemberButton.setEnabled(true);
                            SaveButton.setEnabled(true);

                        } else {
                            if(!txtMemberUsername.getBorder().equals(defaultBorder)){
                                txtMemberUsername.setBorder(defaultBorder);
                                addMemberButton.setEnabled(false);
                                SaveButton.setEnabled(false);

                            }
                        }
                    } catch (Exception el){
                        el.printStackTrace();
                    }
                }
            });
            this.panel = memberPanel;
            return memberPanel;
        }

    }

}
