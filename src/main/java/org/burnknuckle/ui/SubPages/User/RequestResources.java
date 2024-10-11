package org.burnknuckle.ui.SubPages.User;

import com.formdev.flatlaf.FlatDarkLaf;
import net.miginfocom.swing.MigLayout;
import org.burnknuckle.ui.subParts.LoginBgPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

public class RequestResources {
    private JTextField txtRequesterName, txtContactNumber, txtRequiredResources, txtLocation, txtEstimatedPeople;
    private JComboBox<String> cmbDisasterType;
    private JTextArea txtAdditionalInfo;

    public JPanel createRequestResourcesSubPage() {
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
        JPanel centerForm = new JPanel(new MigLayout("wrap 2", "", ""));
        centerForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Resource Request Form");
        title.setFont(new Font("Inter", Font.BOLD, 32));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topBar.add(title);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        topBar.add(separator);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        // Requester Information
        JLabel lblRequesterName = new JLabel("Requester Name:");
        lblRequesterName.setFont(new Font("Inter", Font.BOLD, 16));
        txtRequesterName = new JTextField(20);
        txtRequesterName.setFont(new Font("Inter", Font.PLAIN, 16));
        centerForm.add(lblRequesterName);
        centerForm.add(txtRequesterName);

        JLabel lblContactNumber = new JLabel("Contact Number:");
        lblContactNumber.setFont(new Font("Inter", Font.BOLD, 16));
        txtContactNumber = new JTextField(15);
        txtContactNumber.setFont(new Font("Inter", Font.PLAIN, 16));
        centerForm.add(lblContactNumber);
        centerForm.add(txtContactNumber);

        // Disaster Type (Combo Box)
        JLabel lblDisasterType = new JLabel("Disaster Type:");
        lblDisasterType.setFont(new Font("Inter", Font.BOLD, 16));
        String[] disasters = {
                "Flood",
                "Earthquake",
                "Hurricane",
                "Tornado",
                "Wildfire",
                "Drought",
                "Landslide",
                "Tsunami",
                "Volcanic Eruption",
                "Pandemic"
        };
        cmbDisasterType = new JComboBox<>(disasters);
        cmbDisasterType.setFont(new Font("Inter", Font.PLAIN, 16));
        centerForm.add(lblDisasterType);
        centerForm.add(cmbDisasterType);

        // Required Resources
        JLabel lblRequiredResources = new JLabel("Required Resources:");
        lblRequiredResources.setFont(new Font("Inter", Font.BOLD, 16));
        txtRequiredResources = new JTextField(20);
        txtRequiredResources.setFont(new Font("Inter", Font.PLAIN, 16));
        centerForm.add(lblRequiredResources);
        centerForm.add(txtRequiredResources);

        // Location
        JLabel lblLocation = new JLabel("Location:");
        lblLocation.setFont(new Font("Inter", Font.BOLD, 16));
        txtLocation = new JTextField(20);
        txtLocation.setFont(new Font("Inter", Font.PLAIN, 16));
        centerForm.add(lblLocation);
        centerForm.add(txtLocation);

        // Estimated Number of People Affected
        JLabel lblEstimatedPeople = new JLabel("Estimated People Affected:");
        lblEstimatedPeople.setFont(new Font("Inter", Font.BOLD, 16));
        txtEstimatedPeople = new JTextField(10);
        txtEstimatedPeople.setFont(new Font("Inter", Font.PLAIN, 16));
        centerForm.add(lblEstimatedPeople);
        centerForm.add(txtEstimatedPeople);

        // Additional Information
        JLabel lblAdditionalInfo = new JLabel("Additional Information:");
        lblAdditionalInfo.setFont(new Font("Inter", Font.BOLD, 16));
        txtAdditionalInfo = new JTextArea(5, 20);
        txtAdditionalInfo.setFont(new Font("Inter", Font.PLAIN, 16));
        centerForm.add(lblAdditionalInfo);
        centerForm.add(new JScrollPane(txtAdditionalInfo), "span");

        // Submit Button
        JButton submitButton = new JButton("Submit Request");
        submitButton.setFont(new Font("Inter", Font.BOLD, 16));
        submitButton.setPreferredSize(new Dimension(200, 40));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickSubmit();
            }
        });
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

        private void clickSubmit() {
        Map<String, Object> data = new HashMap<>();
        data.put("Requester Name", txtRequesterName.getText().trim());
        data.put("Contact Number", txtContactNumber.getText().trim());
        data.put("Disaster Type", cmbDisasterType.getSelectedItem());
        data.put("Required Resources", txtRequiredResources.getText().trim());
        data.put("Location", txtLocation.getText().trim());
        data.put("Estimated People Affected", txtEstimatedPeople.getText().trim());
        data.put("Additional Information", txtAdditionalInfo.getText().trim());

        String errorMessage = validateData(data);
        if (!errorMessage.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please correct the following errors:\n" + errorMessage, "Form Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        data.forEach((key, value) -> System.out.println(key + ": " + value));

        JOptionPane.showMessageDialog(null, "Request submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private String validateData(Map<String, Object> data) {
        StringBuilder errorMessage = new StringBuilder();

        if (data.get("Requester Name").toString().isEmpty()) {
            errorMessage.append("- Requester Name is required.\n");
        }
        if (data.get("Contact Number").toString().isEmpty()) {
            errorMessage.append("- Contact Number is required.\n");
        }
        if (data.get("Required Resources").toString().isEmpty()) {
            errorMessage.append("- Required Resources is required.\n");
        }
        if (data.get("Location").toString().isEmpty()) {
            errorMessage.append("- Location is required.\n");
        }
        if (data.get("Estimated People Affected").toString().isEmpty()) {
            errorMessage.append("- Estimated People Affected is required.\n");
        }

        if (!errorMessage.isEmpty()) {
            return errorMessage.toString();
        }
        return "";
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Resource Request Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FlatDarkLaf.setup();
        frame.setSize(600, 600);

        RequestResources requestPage = new RequestResources();
        frame.add(requestPage.createRequestResourcesSubPage());
        frame.setVisible(true);
    }

}
