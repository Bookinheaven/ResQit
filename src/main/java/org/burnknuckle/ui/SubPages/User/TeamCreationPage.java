package org.burnknuckle.ui.SubPages.User;

import com.formdev.flatlaf.FlatDarkLaf;
import net.miginfocom.swing.MigLayout;
import org.burnknuckle.ui.subParts.LoginBgPanel;
import org.burnknuckle.utils.Database;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import static org.burnknuckle.utils.Resources.*;
import static org.burnknuckle.utils.ThemeManager.getColorFromHex;
import static org.burnknuckle.utils.Userdata.getUsername;

public class TeamCreationPage {

    // Team Information Fields
    private JTextField txtTeamName, txtOrganizationAffiliation,txtCoLeaderName, txtLeaderName, txtPhoneNumber, txtEmailAddress, txtTeamAddress, txtNumberOfMembers;
    private JComboBox<String> cmbTeamType;
    private static JButton addMemberButton;
    private String username = getUsername();
//    private String username = "tanvik123";
    // Team Members Information Fields
    private static JPanel teamMembersUsernamesPanel = null;
    private static ArrayList<MemberPanel> memberPanels = new ArrayList<>();

    // Expertise Fields
    private JTextField txtPrimaryExpertise, txtSecondaryExpertise;
    private JTextArea txtEquipmentResources;

    // Availability Fields
    private JTextField txtAvailabilityStart, txtAvailabilityEnd;
    private JComboBox<String> cmbTravelWillingness;
    private JTextField txtPreferredLocation, txtMaxDeploymentDuration;

    // Experience Fields
    private JTextArea txtPreviousOperations, txtSuccessStories, txtReferences;

    // Preparedness Fields
    private JTextArea txtTrainingAttended, txtEmergencyProtocols, txtHealthSafetyMeasures;
    private JTextField txtInsuranceInfo;

    // Legal and Consent Fields
    private JCheckBox chkBackgroundCheckConsent, chkGuidelinesAgreement, chkLiabilityWaiver, chkMediaReleaseConsent;

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
        JPanel centerForm = new JPanel(new MigLayout("wrap 2", "[150][fill, grow]"));
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
        teamInfoTitle.setForeground(new Color(30, 144, 255));
        centerForm.add(teamInfoTitle, "span, align center, gapbottom 20px");

        txtTeamName = FieldsCreator(centerForm, "Team Name", txtTeamName);

        FieldsCreator(centerForm, "Organization Affiliation", txtOrganizationAffiliation);

        txtLeaderName = FieldsCreator(centerForm, "Team Leader's Username", txtLeaderName);
        txtLeaderName.setText(username);
        txtLeaderName.setEnabled(false);

        txtCoLeaderName = FieldsCreator(centerForm, "Team CoLeader's Username", txtCoLeaderName);

        JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
        centerForm.add(separator2, "span, grow, gaptop 10px, gapbottom 10px");

        JPanel contactInfoPanel = new JPanel(new MigLayout("fill", "[][]", "[]"));
        txtPhoneNumber = new JTextField(20);
        txtEmailAddress = new JTextField(20);

        JLabel lblPhoneNumbers = new JLabel("Phone Number:");
        lblPhoneNumbers.setFont(new Font("Inter", Font.PLAIN, 16));
        contactInfoPanel.add(lblPhoneNumbers);
        contactInfoPanel.add(txtPhoneNumber, "span, grow");

        JLabel lblGmail = new JLabel("Gmail:");
        lblGmail.setFont(new Font("Inter", Font.PLAIN, 16));
        contactInfoPanel.add(lblGmail);
        contactInfoPanel.add(txtEmailAddress, "span, grow");

        JLabel lblContactInfo = new JLabel("Team Contact Info");
        lblContactInfo.setFont(new Font("Inter", Font.PLAIN, 16));
        centerForm.add(lblContactInfo);
        centerForm.add(contactInfoPanel);

        JSeparator separator3 = new JSeparator(SwingConstants.HORIZONTAL);
        centerForm.add(separator3, "span, grow, gaptop 10px, gapbottom 10px");

        txtTeamAddress = FieldsCreator(centerForm, "Team Address (Street, City, State, Zip)", txtTeamAddress);

//        FieldsCreator(centerForm, "Number of Members", txtNumberOfMembers);

        String[] teamTypes = {"Medical", "Search and Rescue", "Logistics", "Psychological Support"};
        cmbTeamType = new JComboBox<>(teamTypes);
        JLabel teamTpLabel = new JLabel("Type of Team:");
        teamTpLabel.setFont(new Font("Inter", Font.PLAIN, 16));

        centerForm.add(teamTpLabel);
        centerForm.add(cmbTeamType);

        JSeparator separator4 = new JSeparator(SwingConstants.HORIZONTAL);
        centerForm.add(separator4, "span, grow, gaptop 10px, gapbottom 10px");

        JLabel teamMembersInfoLabel = new JLabel("Team Members' Info");
        teamMembersInfoLabel.setFont(new Font("Inter", Font.PLAIN, 20));
        centerForm.add(teamMembersInfoLabel, "span, grow, wrap");

        teamMembersUsernamesPanel = new JPanel();
        teamMembersUsernamesPanel.setLayout(new BoxLayout(teamMembersUsernamesPanel, BoxLayout.Y_AXIS));
        teamMembersUsernamesPanel.setBackground(Color.CYAN);

        addMemberButton = new JButton("Add Member");
        addMemberButton.addActionListener(e -> addMember(teamMembersUsernamesPanel));
        centerForm.add(addMemberButton, "span, grow");

        centerForm.add(teamMembersUsernamesPanel, "span, grow, wrap");

        // Team’s Area of Expertise
        centerForm.add(new JLabel("Team's Area of Expertise"), "span, grow, wrap");

        txtPrimaryExpertise = new JTextField(20);
        centerForm.add(new JLabel("Primary Expertise:"));
        centerForm.add(txtPrimaryExpertise);

        txtSecondaryExpertise = new JTextField(20);
        centerForm.add(new JLabel("Secondary Expertise:"));
        centerForm.add(txtSecondaryExpertise);

        txtEquipmentResources = new JTextArea(3, 20);
        centerForm.add(new JLabel("Equipment and Resources Available:"));
        centerForm.add(new JScrollPane(txtEquipmentResources));

        // Availability and Deployment Preferences
        centerForm.add(new JLabel("Availability and Deployment Preferences"), "span, grow, wrap");

        JPanel availabilityPanel = new JPanel(new MigLayout("fill", "[][]", "[]"));
        txtAvailabilityStart = new JTextField(20);
        txtAvailabilityEnd = new JTextField(20);
        availabilityPanel.add(txtAvailabilityStart);
        availabilityPanel.add(txtAvailabilityEnd);

        centerForm.add(new JLabel("Availability Dates (Start - End):"));
        centerForm.add(availabilityPanel);

        String[] travelOptions = {"Local", "National", "International"};
        cmbTravelWillingness = new JComboBox<>(travelOptions);
        centerForm.add(new JLabel("Willingness to Travel:"));
        centerForm.add(cmbTravelWillingness);

        txtPreferredLocation = new JTextField(20);
        centerForm.add(new JLabel("Preferred Deployment Location:"));
        centerForm.add(txtPreferredLocation);

        txtMaxDeploymentDuration = new JTextField(10);
        centerForm.add(new JLabel("Max Deployment Duration:"));
        centerForm.add(txtMaxDeploymentDuration);

        // Team’s Past Experience
        centerForm.add(new JLabel("Team's Past Experience"), "span, grow, wrap");

        txtPreviousOperations = new JTextArea(3, 20);
        centerForm.add(new JLabel("Previous Disaster Relief Operations:"));
        centerForm.add(new JScrollPane(txtPreviousOperations));

        txtSuccessStories = new JTextArea(2, 20);
        centerForm.add(new JLabel("Success Stories:"));
        centerForm.add(new JScrollPane(txtSuccessStories));

        txtReferences = new JTextArea(2, 20);
        centerForm.add(new JLabel("Feedback/References:"));
        centerForm.add(new JScrollPane(txtReferences));

        // Team's Preparedness
        centerForm.add(new JLabel("Team's Preparedness"), "span, grow, wrap");

        txtTrainingAttended = new JTextArea(2, 20);
        centerForm.add(new JLabel("Training Attended:"));
        centerForm.add(new JScrollPane(txtTrainingAttended));

        txtEmergencyProtocols = new JTextArea(2, 20);
        centerForm.add(new JLabel("Emergency Protocols/SOPs:"));
        centerForm.add(new JScrollPane(txtEmergencyProtocols));

        txtHealthSafetyMeasures = new JTextArea(2, 20);
        centerForm.add(new JLabel("Health and Safety Measures:"));
        centerForm.add(new JScrollPane(txtHealthSafetyMeasures));

        txtInsuranceInfo = new JTextField(20);
        centerForm.add(new JLabel("Insurance Info:"));
        centerForm.add(txtInsuranceInfo);

        // Legal and Consent
        centerForm.add(new JLabel("Legal and Consent"), "span, grow, wrap");

        chkBackgroundCheckConsent = new JCheckBox("Consent for Background Check");
        centerForm.add(chkBackgroundCheckConsent, "span, grow");

        chkGuidelinesAgreement = new JCheckBox("Agree to Follow Guidelines");
        centerForm.add(chkGuidelinesAgreement, "span, grow");

        chkLiabilityWaiver = new JCheckBox("Liability Waiver");
        centerForm.add(chkLiabilityWaiver, "span, grow");

        chkMediaReleaseConsent = new JCheckBox("Consent for Media Release");
        centerForm.add(chkMediaReleaseConsent, "span, grow");

        // Additional Information
        centerForm.add(new JLabel("Additional Information"), "span, grow, wrap");
        JTextArea txtAdditionalInfo = new JTextArea(3, 20);
        centerForm.add(new JScrollPane(txtAdditionalInfo), "span, grow");

        // Submit Button
        JButton submitButton = new JButton("Submit Team Info");
        submitButton.setPreferredSize(new Dimension(200, 40));
        submitButton.addActionListener(e -> submitTeamInfo());
        centerForm.add(submitButton, "span, align center");


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

    private void submitTeamInfo() {
        System.out.println("Team Information submitted!");
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
                System.out.println(!txtMemberUsername.getText().equals(getUsername()));
                if(!checkMemberExist(memberPanel) && !txtMemberUsername.getText().equals(getUsername())){
                    SaveButton.setEnabled(false);
                    txtMemberUsername.setEnabled(false);
                    addMemberButton.setEnabled(true);
                    EditButton.setEnabled(true);
                    DeleteButton.setEnabled(true);
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
