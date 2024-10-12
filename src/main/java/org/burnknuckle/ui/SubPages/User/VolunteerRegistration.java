package org.burnknuckle.ui.SubPages.User;

import com.formdev.flatlaf.FlatDarkLaf;
import net.miginfocom.swing.MigLayout;
import org.burnknuckle.ui.subParts.LoginBgPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

public class VolunteerRegistration {
    private JTextField txtFirstName, txtMiddleName, txtLastName, txtContactNo, txtEmergencyNo, txtAvailability,
            txtPhysicalLimits, txtZipCode, txtState, txtRoad, txtCity, txtCountry;
    private JTextArea txtSkills, txtLanguagesSpoken, txtPriorExperiences, txtAllergies;
    private JComboBox<String> cmbGender, txtPreferredVolunteeringWork ,cmbPreferredLocation, cmbProfessionalBackground, cmbBloodGroup;
    private JRadioButton rbtnYes, rbtnNo;
    private JFormattedTextField txtDob;

    private void clickSubmit(){
        Map<String, Object> data = new HashMap<>();
        data.put("First Name", txtFirstName.getText().trim());
        data.put("Middle Name", txtMiddleName.getText().trim());
        data.put("Last Name", txtLastName.getText().trim());
        data.put("Date of Birth", txtDob.getText().trim());
        data.put("Gender", cmbGender.getSelectedItem());
        data.put("Contact Number", txtContactNo.getText().trim());
        data.put("Zip Code", txtZipCode.getText().trim());
        data.put("State", txtState.getText().trim());
        data.put("Road", txtRoad.getText().trim());
        data.put("City", txtCity.getText().trim());
        data.put("Country", txtCountry.getText().trim());
        data.put("Emergency Contact", txtEmergencyNo.getText().trim());
        data.put("Availability", txtAvailability.getText().trim());
        data.put("Preferred Volunteering Location", cmbPreferredLocation.getSelectedItem());
        data.put("Professional Background", cmbProfessionalBackground.getSelectedItem());
        data.put("Skills", txtSkills.getText().trim());
        data.put("Languages Spoken", txtLanguagesSpoken.getText().trim());
        data.put("Prior Experiences", txtPriorExperiences.getText().trim());
        data.put("Preferred Volunteering Work", txtPreferredVolunteeringWork.getSelectedItem());
        data.put("Physical Limitations", txtPhysicalLimits.getText().trim());
        data.put("Blood Group", cmbBloodGroup.getSelectedItem());
        data.put("Allergies", txtAllergies.getText().trim());

        String willingnessToTravel = rbtnYes.isSelected() ? "Yes" : rbtnNo.isSelected() ? "No" : "Not specified";
        data.put("Willingness to Travel", willingnessToTravel);

        String errorMessage = validateData(data);
        if (!errorMessage.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please correct the following errors:\n" + errorMessage, "Form Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        data.forEach((key, value) -> System.out.println(key + ": " + value));
        JOptionPane.showMessageDialog(null, "Form submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private String validateData(Map<String, Object> data) {
        StringBuilder errorMessage = new StringBuilder();
        if (data.get("First Name").toString().isEmpty()) {
            errorMessage.append("- First Name is required.\n");
        }
        if (data.get("Last Name").toString().isEmpty()) {
            errorMessage.append("- Last Name is required.\n");
        }
        if (data.get("Contact Number").toString().isEmpty()) {
            errorMessage.append("- Contact Number is required.\n");
        }
        if (data.get("Middle Name").toString().isEmpty()) {
            errorMessage.append("- Middle Name is required.\n");
        }
        if (data.get("Availability").toString().isEmpty()) {
            errorMessage.append("- Availability is required.\n");
        }
        if (data.get("Zip Code").toString().isEmpty()) {
            errorMessage.append("- Zip Code is required.\n");
        }
        if (data.get("State").toString().isEmpty()) {
            errorMessage.append("- State is required.\n");
        }
        if (data.get("Road").toString().isEmpty()) {
            errorMessage.append("- Road is required.\n");
        }
        if (data.get("City").toString().isEmpty()) {
            errorMessage.append("- City is required.\n");
        }
        if (data.get("Country").toString().isEmpty()) {
            errorMessage.append("- Country is required.\n");
        }

        if (data.get("Willingness to Travel").toString().isEmpty()) {
            errorMessage.append("- Willingness to Travel is required.\n");
        }

        if (data.get("Languages Spoken").toString().isEmpty()) {
            errorMessage.append("- Languages Spoken is required.\n");
        }

        if (data.get("Emergency Contact").toString().isEmpty()) {
            errorMessage.append("- Emergency Contact is required.\n");
        }
        if (data.get("Professional Background").toString().isEmpty()) {
            errorMessage.append("- Emergency Contact is required.\n");
        }

        data.put("Preferred Volunteering Work", data.get("Preferred Volunteering Work").toString().isEmpty() ? "None" : data.get("Preferred Volunteering Work"));
        data.put("Physical Limitations", data.get("Physical Limitations").toString().isEmpty() ? "None" : data.get("Physical Limitations"));

        data.put("Skills", data.get("Skills").toString().isEmpty() ? "None" : data.get("Skills"));
        data.put("Prior Experiences", data.get("Prior Experiences").toString().isEmpty() ? "None" : data.get("Prior Experiences"));
        data.put("Allergies", data.get("Allergies").toString().isEmpty() ? "None" : data.get("Allergies"));

        data.put("Emergency Contact", data.get("Emergency Contact").toString().isEmpty() ? "None" : data.get("Emergency Contact"));
        data.put("Professional Background", data.get("Professional Background").toString().isEmpty() ? "None" : data.get("Professional Background"));

        if (!errorMessage.isEmpty()) {
            return errorMessage.toString();
        }
        return "";
    }

    public JPanel createVolunteerRegistrationSubPage() {
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
        // Title Label
        JLabel title = new JLabel("Volunteer Registration");
        title.setFont(new Font("Inter", Font.BOLD, 32));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Top Bar Panel
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        topBar.add(title);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        topBar.add(separator);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        wrapper.setOpaque(false);
        JPanel centerForm = new JPanel(new MigLayout("wrap 2", "", ""));
        centerForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JSeparator separator6 = new JSeparator(JSeparator.HORIZONTAL);
        centerForm.add(separator6, "span 2, grow, gapbottom 10");

        JLabel contactInfoTitle = new JLabel("Contact Information");
        contactInfoTitle.setFont(new Font("Inter", Font.BOLD, 20));
        contactInfoTitle.setForeground(new Color(30, 144, 255)); // Blue header color
        centerForm.add(contactInfoTitle, "span, gapbottom 10, gaptop 10, align center");

        JLabel lblContactNo = new JLabel("Contact Number:");
        lblContactNo.setFont(new Font("Inter", Font.PLAIN, 16));

        txtContactNo = new JTextField(15);
        txtContactNo.setFont(new Font("Inter", Font.PLAIN, 16));
        txtContactNo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });
        centerForm.add(lblContactNo, "gapright 10");
        centerForm.add(txtContactNo);

        JLabel lblZipCode = new JLabel("Zip Code:");
        lblZipCode.setFont(new Font("Inter", Font.PLAIN, 16));
        txtZipCode = new JTextField(10);
        txtZipCode.setFont(new Font("Inter", Font.PLAIN, 16));
        txtZipCode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!Character.isDigit(e.getKeyChar()) || txtZipCode.getText().length() > 7) {
                    e.consume();
                }
            }
        });
        JLabel lblState = new JLabel("State:");
        lblState.setFont(new Font("Inter", Font.PLAIN, 16));
        txtState = new JTextField(15);
        txtState.setFont(new Font("Inter", Font.PLAIN, 16));

        JLabel lblRoad = new JLabel("Road:");
        lblRoad.setFont(new Font("Inter", Font.PLAIN, 16));
        txtRoad = new JTextField(20);
        txtRoad.setFont(new Font("Inter", Font.PLAIN, 16));

        JLabel lblCity = new JLabel("City:");
        lblCity.setFont(new Font("Inter", Font.PLAIN, 16));
        txtCity = new JTextField(20);
        txtCity.setFont(new Font("Inter", Font.BOLD, 16));

        JLabel lblCountry = new JLabel("Country:");
        lblCountry.setFont(new Font("Inter", Font.PLAIN, 16));
        txtCountry = new JTextField(20);
        txtCountry.setFont(new Font("Inter", Font.PLAIN, 16));

        centerForm.add(lblCountry, "gapright 10");
        centerForm.add(txtCountry, "span");

        centerForm.add(lblState, "gapright 10");
        centerForm.add(txtState, "span");

        centerForm.add(lblZipCode, "gapright 10");
        centerForm.add(txtZipCode, "span");

        centerForm.add(lblCity, "gapright 10");
        centerForm.add(txtCity, "span");

        centerForm.add(lblRoad, "gapright 10");
        centerForm.add(txtRoad, "span");

        JLabel lblEmergencyNo = new JLabel("Emergency Contact:");
        lblEmergencyNo.setFont(new Font("Inter", Font.PLAIN, 16));
        txtEmergencyNo = new JTextField(15);
        txtEmergencyNo.setFont(new Font("Inter", Font.PLAIN, 16));

        centerForm.add(lblEmergencyNo, "gapright 10");
        centerForm.add(txtEmergencyNo);

        JLabel lblAvailability = new JLabel("Availability:");
        lblAvailability.setFont(new Font("Inter", Font.PLAIN, 16));

        txtAvailability = new JTextField(20);
        txtAvailability.setFont(new Font("Inter", Font.PLAIN, 16));

        centerForm.add(lblAvailability, "gapright 10");
        centerForm.add(txtAvailability);

        JSeparator separator4 = new JSeparator(JSeparator.HORIZONTAL);
        centerForm.add(separator4, "gaptop 20, span 2, grow, gapbottom 10");

        JLabel travelInfoTitle = new JLabel("Travel Information");
        travelInfoTitle.setFont(new Font("Inter", Font.BOLD, 20));
        travelInfoTitle.setForeground(new Color(30, 144, 255)); // Blue header color
        centerForm.add(travelInfoTitle, "span, gapbottom 10, gaptop 10, align center");

        JLabel lblWillingness = new JLabel("Willingness to Travel:");
        lblWillingness.setFont(new Font("Inter", Font.PLAIN, 16));

        rbtnYes = new JRadioButton("Yes");
        rbtnNo = new JRadioButton("No");
        ButtonGroup travelGroup = new ButtonGroup();
        travelGroup.add(rbtnYes);
        travelGroup.add(rbtnNo);
        JPanel travelPanel = new JPanel();
        rbtnNo.setFont(new Font("Inter", Font.PLAIN, 16));
        rbtnYes.setFont(new Font("Inter", Font.PLAIN, 16));

        travelPanel.add(rbtnYes);
        travelPanel.add(rbtnNo);
        centerForm.add(lblWillingness, "gapright 10");
        centerForm.add(travelPanel, "span");

        JLabel lblPreferredLocation = new JLabel("Preferred Volunteering Location:");
        lblPreferredLocation.setFont(new Font("Inter", Font.PLAIN, 16));

        cmbPreferredLocation = new JComboBox<>(new String[]{"Local", "State", "National"});
        cmbPreferredLocation.setFont(new Font("Inter", Font.PLAIN, 16));

        centerForm.add(lblPreferredLocation, "gapright 10");
        centerForm.add(cmbPreferredLocation);

        JSeparator separator3 = new JSeparator(JSeparator.HORIZONTAL);
        centerForm.add(separator3, "gaptop 20, span 2, grow, gapbottom 10");

        JLabel professionalInfoTitle = new JLabel("Professional Background");
        professionalInfoTitle.setFont(new Font("Inter", Font.BOLD, 20));
        professionalInfoTitle.setForeground(new Color(30, 144, 255)); // Blue header color
        centerForm.add(professionalInfoTitle, "span, gapbottom 10, gaptop 10, align center");

        JLabel lblProfessionalBackground = new JLabel("Professional Background:");
        lblProfessionalBackground.setFont(new Font("Inter", Font.PLAIN, 16));

        cmbProfessionalBackground = new JComboBox<>(new String[]{"Medical", "Engineering", "IT", "Construction", "Other"});
        cmbProfessionalBackground.setFont(new Font("Inter", Font.PLAIN, 16));

        centerForm.add(lblProfessionalBackground, "gapright 10");
        centerForm.add(cmbProfessionalBackground);

        JLabel lblSkills = new JLabel("Skills:");
        lblSkills.setFont(new Font("Inter", Font.PLAIN, 16));
        txtSkills = new JTextArea(3, 20);
        txtSkills.setFont(new Font("Inter", Font.PLAIN, 16));

        centerForm.add(lblSkills, "gapright 10");
        centerForm.add(new JScrollPane(txtSkills), "span");

        JLabel lblLanguagesSpoken = new JLabel("Languages Spoken:");
        lblLanguagesSpoken.setFont(new Font("Inter", Font.PLAIN, 16));

        txtLanguagesSpoken = new JTextArea(2, 20);
        txtLanguagesSpoken.setFont(new Font("Inter", Font.PLAIN, 16));

        centerForm.add(lblLanguagesSpoken, "gapright 10");
        centerForm.add(new JScrollPane(txtLanguagesSpoken), "span");

        JLabel lblPriorExperiences = new JLabel("Prior Experiences:");
        lblPriorExperiences.setFont(new Font("Inter", Font.PLAIN, 16));

        txtPriorExperiences = new JTextArea(3, 20);
        txtPriorExperiences.setFont(new Font("Inter", Font.PLAIN, 16));
        centerForm.add(lblPriorExperiences, "gapright 10");
        centerForm.add(new JScrollPane(txtPriorExperiences), "span");

        JLabel lblVolunteeringWork = new JLabel("Preferred Volunteering Work:");
        lblVolunteeringWork.setFont(new Font("Inter", Font.PLAIN, 16));
        txtPreferredVolunteeringWork = new JComboBox<>(new String[]{
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
        txtPreferredVolunteeringWork.setFont(new Font("Inter", Font.PLAIN, 16));
        centerForm.add(lblVolunteeringWork, "gapright 10");
        centerForm.add(txtPreferredVolunteeringWork);

        JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);
        centerForm.add(separator2, "gaptop 20, span 2, grow, gapbottom 10");

        JLabel medicalInfoTitle = new JLabel("Medical Information");
        medicalInfoTitle.setFont(new Font("Inter", Font.BOLD, 20));
        medicalInfoTitle.setForeground(new Color(30, 144, 255)); // Blue header color
        centerForm.add(medicalInfoTitle, "span, gapbottom 10, gaptop 10, align center");

        JLabel lblPhysicalLimits = new JLabel("Physical Limitations:");
        lblPhysicalLimits.setFont(new Font("Inter", Font.PLAIN, 16));
        txtPhysicalLimits = new JTextField(20);
        txtPhysicalLimits.setFont(new Font("Inter", Font.PLAIN, 16));

        centerForm.add(lblPhysicalLimits, "gapright 10");
        centerForm.add(txtPhysicalLimits);

        JLabel lblBloodGroup = new JLabel("Blood Group:");
        lblBloodGroup.setFont(new Font("Inter", Font.PLAIN, 16));

        cmbBloodGroup = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        cmbBloodGroup.setFont(new Font("Inter", Font.PLAIN, 16));
        centerForm.add(lblBloodGroup, "gapright 10");
        centerForm.add(cmbBloodGroup);

        JLabel lblAllergies = new JLabel("Allergies or Medical Conditions:");
        lblAllergies.setFont(new Font("Inter", Font.PLAIN, 16));

        txtAllergies = new JTextArea(3, 20);
        txtAllergies.setFont(new Font("Inter", Font.PLAIN, 16));
        centerForm.add(lblAllergies, "gapright 10");
        centerForm.add(new JScrollPane(txtAllergies), "span, growx");

        JSeparator separator1 = new JSeparator(JSeparator.HORIZONTAL);
        centerForm.add(separator1, "gaptop 40, span 2, grow, gapbottom 30");

        JButton submitButton = new JButton();
        submitButton.setText("Submit");
        submitButton.setPreferredSize(new Dimension(300,50));
        submitButton.setFont(new Font("Inter", Font.PLAIN, 16));
        submitButton.addActionListener(_ -> clickSubmit());
        centerForm.add(submitButton, "span 2, align center");

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
    public static void main(String[] args) {
        JFrame f = new JFrame();
        FlatDarkLaf.setup();
        f.setSize(new Dimension(900,900));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new VolunteerRegistration().createVolunteerRegistrationSubPage());
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
