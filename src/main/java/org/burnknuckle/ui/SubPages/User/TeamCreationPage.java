package org.burnknuckle.ui.SubPages.User;

import com.formdev.flatlaf.FlatDarkLaf;
import net.miginfocom.swing.MigLayout;
import org.burnknuckle.ui.subParts.LoginBgPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;


public class TeamCreationPage {

    // Team Information Fields
    private JTextField txtTeamName, txtOrganizationAffiliation, txtLeaderName, txtPhoneNumber, txtEmailAddress, txtTeamAddress, txtNumberOfMembers;
    private JComboBox<String> cmbTeamType;

    // Team Members Information Fields
    private ArrayList<MemberPanel> memberPanels = new ArrayList<>();

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
        title.setFont(new Font("Inter", Font.BOLD, 32));
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
        centerForm.add(teamInfoTitle, "span, align center");

        txtTeamName = new JTextField(20);
        JLabel teamName = new JLabel("Team Name:");
        teamName.setFont(new Font("Inter", Font.PLAIN, 18));
        txtTeamName.setFont(new Font("Inter", Font.PLAIN, 18));
        centerForm.add(teamName, "gapright 10");
        centerForm.add(txtTeamName);

        txtOrganizationAffiliation = new JTextField(20);
        JLabel orgAff = new JLabel("Organization Affiliation:");
        orgAff.setFont(new Font("Inter", Font.PLAIN, 18));
        centerForm.add(orgAff);
        centerForm.add(txtOrganizationAffiliation);

        txtLeaderName = new JTextField(20);
        JLabel teamLeaderName = new JLabel("Team Leader's Name:");
        teamLeaderName.setFont(new Font("Inter", Font.PLAIN, 18));
        centerForm.add(teamLeaderName, "gapright 10");
        centerForm.add(txtLeaderName);

        JPanel contactInfoPanel = new JPanel(new MigLayout("fill", "[][]", "[]"));
        txtPhoneNumber = new JTextField(20);
        txtEmailAddress = new JTextField(20);

        JLabel lblPhoneNumbers = new JLabel("Phone Number:");
        lblPhoneNumbers.setFont(new Font("Inter", Font.PLAIN, 16));
        contactInfoPanel.add(lblPhoneNumbers);
        contactInfoPanel.add(txtPhoneNumber, "span");

        JLabel lblGmail = new JLabel("Gmail:");
        lblGmail.setFont(new Font("Inter", Font.PLAIN, 16));
        contactInfoPanel.add(lblGmail);
        contactInfoPanel.add(txtEmailAddress, "span");

        JLabel lblContactInfo = new JLabel("Contact Information:");
        lblContactInfo.setFont(new Font("Inter", Font.PLAIN, 18));
        centerForm.add(lblContactInfo);
        centerForm.add(contactInfoPanel);

        txtTeamAddress = new JTextField(20);
        centerForm.add(new JLabel("Team Address (Street, City, State, Zip):"));
        centerForm.add(txtTeamAddress);

        txtNumberOfMembers = new JTextField(5);
        centerForm.add(new JLabel("Number of Members:"));
        centerForm.add(txtNumberOfMembers);

        String[] teamTypes = {"Medical", "Search and Rescue", "Logistics", "Psychological Support"};
        cmbTeamType = new JComboBox<>(teamTypes);
        centerForm.add(new JLabel("Type of Team:"));
        centerForm.add(cmbTeamType);

        centerForm.add(new JLabel("Team Members' Information"), "span, grow, wrap");

        JButton addMemberButton = new JButton("Add Member");
        addMemberButton.addActionListener(e -> addMember(centerForm));
        centerForm.add(addMemberButton, "span, grow");

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

    private void addMember(JPanel formPanel) {
        MemberPanel memberPanel = new MemberPanel();
        formPanel.add(memberPanel.createMemberPanel(), "span, grow");
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

    private static class MemberPanel {
        private JTextField txtMemberName, txtMemberContact, txtMemberEmail, txtMemberRole, txtMemberSkills, txtEmergencyContact, txtHealthInfo;

        public JPanel createMemberPanel() {
            JPanel memberPanel = new JPanel(new MigLayout("wrap 2", "[150][fill, grow]"));

            txtMemberName = new JTextField(20);
            memberPanel.add(new JLabel("Full Name:"));
            memberPanel.add(txtMemberName);

            txtMemberContact = new JTextField(15);
            memberPanel.add(new JLabel("Contact Number:"));
            memberPanel.add(txtMemberContact);

            txtMemberEmail = new JTextField(20);
            memberPanel.add(new JLabel("Email Address:"));
            memberPanel.add(txtMemberEmail);

            txtMemberRole = new JTextField(20);
            memberPanel.add(new JLabel("Role in the Team:"));
            memberPanel.add(txtMemberRole);

            txtMemberSkills = new JTextField(20);
            memberPanel.add(new JLabel("Skills/Certifications:"));
            memberPanel.add(txtMemberSkills);

            txtEmergencyContact = new JTextField(20);
            memberPanel.add(new JLabel("Emergency Contact:"));
            memberPanel.add(txtEmergencyContact);

            txtHealthInfo = new JTextField(20);
            memberPanel.add(new JLabel("Health Information:"));
            memberPanel.add(txtHealthInfo);

            return memberPanel;
        }

    }

}
