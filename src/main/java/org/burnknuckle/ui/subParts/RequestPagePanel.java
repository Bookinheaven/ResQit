package org.burnknuckle.ui.subParts;

import net.miginfocom.swing.MigLayout;
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
import java.util.HashMap;
import java.util.Map;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;
import static org.burnknuckle.utils.ThemeManager.getColorFromHex;
import static org.burnknuckle.utils.Userdata.getUsername;

public class RequestPagePanel extends JPanel {
    private JComboBox<String> disasterTypeCombo, scaleCombo, disasterScaleLevel, severityLevelCombo;
    private JLabel scaleValueNo, warningLabel;
    private JSlider scaleSlider;
    private JTextArea descriptionTextArea, locationTextArea, impactTextArea;
    private DatePicker startDatePicker, endDatePicker;
    private JTextField disasterName;
    private JButton submitButton;
    private JFormattedTextField endDateField, startDateField;

    private JTextArea TextAreaCreator(String placeholder, int limit){
        JTextArea fieldArea = new JTextArea();
        Border defaultBorder = fieldArea.getBorder();
        Border paddingBorder = new EmptyBorder(5, 5, 5, 5);
        Border lineBorder = new LineBorder(getColorFromHex("#725555"), 2);
        fieldArea.setText(placeholder);
        fieldArea.setBorder(new CompoundBorder(lineBorder, paddingBorder));
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
                if (!fieldArea.getText().equals(placeholder) && !fieldArea.getText().isBlank()) {
                        fieldArea.setForeground(Color.WHITE);
                    } else {
                        fieldArea.setForeground(Color.GRAY);
                    }
            }
        });
        fieldArea.setPreferredSize(new Dimension(400, 100));
        fieldArea.setLineWrap(true);
        fieldArea.setWrapStyleWord(true);
        fieldArea.setFont(new Font("Arial", Font.PLAIN, 14));
        return fieldArea;

    }

    public RequestPagePanel(JPanel mainContent) {
        setLayout(new MigLayout("", "push[center,grow]push", ""));
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Disaster Request Form", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        initializeComponents();
        layoutComponents(formPanel, gbc);
        initializeListeners();
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        centerPanel.add(formPanel);
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, "grow");
        mainContent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                int width = mainContent.getWidth();
                int height = mainContent.getHeight();
                formPanel.setSize(new Dimension(width /2, height));
                revalidate();
            }
        });
    }

    private void initializeComponents() {
        disasterTypeCombo = new JComboBox<>(new String[]{"Earthquake", "Flood", "Hurricane", "Tornado", "Wildfire"});
        scaleCombo = new JComboBox<>();
        disasterScaleLevel = new JComboBox<>(new String[]{"local", "regional", "national"});
        severityLevelCombo = new JComboBox<>(new String[]{"Low", "Moderate", "High", "Critical"});
        scaleValueNo = new JLabel("0.0");
        warningLabel = new JLabel();
        warningLabel.setForeground(Color.RED);
        scaleSlider = new JSlider(0, 10);
        descriptionTextArea = TextAreaCreator("Enter the Description.", 400);
        locationTextArea = TextAreaCreator("Enter the location.", 400);
        impactTextArea = TextAreaCreator("Enter the impact.", 400);
        startDateField = new JFormattedTextField();
        startDatePicker = new DatePicker();
        startDatePicker.setEditor(startDateField);
        endDateField = new JFormattedTextField();
        endDatePicker = new DatePicker();
        endDatePicker.setEditor(endDateField);
        disasterName = new JTextField(20);
        submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(0x4CAF50));
        submitButton.setForeground(Color.WHITE);
        submitButton.setPreferredSize(new Dimension(150,50));
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setFocusPainted(false);
    }

    private void layoutComponents(JPanel mainPanel, GridBagConstraints gbc) {
        Font titleFont = new Font("Arial", Font.BOLD, 20);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        mainPanel.add(new JLabel("Disaster Name") {{ setFont(titleFont); }}, gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        mainPanel.add(disasterName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        mainPanel.add(new JLabel("Disaster Type") {{ setFont(titleFont); }}, gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        mainPanel.add(disasterTypeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2;
        mainPanel.add(new JLabel("Scale") {{ setFont(titleFont); }}, gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        mainPanel.add(scaleCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.2;
        mainPanel.add(new JLabel("Severity Level") {{ setFont(titleFont); }}, gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        mainPanel.add(severityLevelCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.2;
        mainPanel.add(new JLabel("Scale Value") {{ setFont(titleFont); }}, gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        mainPanel.add(scaleValueNo, gbc);
        gbc.gridy++;
        gbc.gridx = 1; gbc.weightx = 0.8;
        mainPanel.add(scaleSlider, gbc);

        gbc.gridy++;
        gbc.ipady = 5;
        mainPanel.add(new JLabel(), gbc);
        gbc.ipady = 0;

        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0.2;
        mainPanel.add(new JLabel("Description") {{ setFont(titleFont); }}, gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        mainPanel.add(new JScrollPane(descriptionTextArea), gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0.2;
        mainPanel.add(new JLabel("Location") {{ setFont(titleFont); }}, gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        mainPanel.add(new JScrollPane(locationTextArea), gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0.2;
        mainPanel.add(new JLabel("Impact Assessment") {{ setFont(titleFont); }}, gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        mainPanel.add(new JScrollPane(impactTextArea), gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0.2;
        mainPanel.add(new JLabel("Start Date") {{ setFont(titleFont); }}, gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        mainPanel.add(startDateField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0.2;
        mainPanel.add(new JLabel("End Date") {{ setFont(titleFont); }}, gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        mainPanel.add(endDateField, gbc);

        gbc.gridx = 1; gbc.gridy++; gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
                //        gbc.gridwidth = 1;
        mainPanel.add(submitButton, gbc);

        gbc.gridx = 1;gbc.gridy++; gbc.weightx = 0.8;
        mainPanel.add(warningLabel, gbc);
    }

    private void initializeListeners() {
        disasterTypeCombo.addActionListener(e -> disasterTypeComboSelect());
        scaleSlider.addChangeListener(e -> {
            int value = scaleSlider.getValue();
            scaleValueNo.setText(String.format("%.1f", (float) value));
        });
        submitButton.addActionListener(e -> submitData());
    }

    public void submitData() {
        if (validateFields()) {
            Map<String, Object> formData = collectFormData();
            try {
                Database db = Database.getInstance();
                db.getConnection();
                if (db.checkForDuplicateDisaster(formData)) {
                    showWarning("Duplicate disaster request detected!");
                } else {
                    db.insertData(2, formData);
                    JOptionPane.showMessageDialog(null, "Upload Request %s".formatted(disasterName.getText()), "Success", JOptionPane.INFORMATION_MESSAGE);
                    warningLabel.setVisible(false);
                    clearFields();
                }
            } catch (Exception e) {
                logger.error("Error in RequestPagePanel.submitData: %s".formatted(getStackTraceAsString(e)));
            }
        }
    }

    private boolean validateFields() {
        if (disasterName.getText().trim().isEmpty()) {
            showWarning("Disaster Name cannot be empty.");
            return false;
        }
        if (descriptionTextArea.getText().trim().isEmpty()) {
            showWarning("Description cannot be empty.");
            return false;
        }
        if (locationTextArea.getText().trim().isEmpty()) {
            showWarning("Location cannot be empty.");
            return false;
        }
        if (impactTextArea.getText().trim().isEmpty()) {
            showWarning("Impact Assessment cannot be empty.");
            return false;
        }
        return true;
    }

    private void clearFields() {
        disasterName.setText("");
        disasterTypeCombo.setSelectedIndex(0);
        scaleCombo.setSelectedIndex(0);
        severityLevelCombo.setSelectedIndex(0);
        scaleValueNo.setText("0.0");
        scaleSlider.setValue(0);
        descriptionTextArea.setText("");
        locationTextArea.setText("");
        impactTextArea.setText("");
        startDatePicker.clearSelectedDate();
        endDatePicker.clearSelectedDate();
    }

    private Map<String, Object> collectFormData() {
        Map<String, Object> formData = new HashMap<>();
        formData.put("disastername", disasterName.getText());
        formData.put("disastertype", disasterTypeCombo.getSelectedItem());
        formData.put("scalemeter", scaleCombo.getSelectedItem() + " | " + scaleSlider.getValue());
        formData.put("scale", disasterScaleLevel.getSelectedItem());
        formData.put("severity", severityLevelCombo.getSelectedItem());
        formData.put("description", descriptionTextArea.getText());
        formData.put("responsestatus", "requested");
        formData.put("location", locationTextArea.getText());
        formData.put("impactassessment", impactTextArea.getText());
        formData.put("startdate", startDatePicker.getSelectedDate());
        formData.put("enddate", endDatePicker.getSelectedDate());
        formData.put("useruploaded", getUsername());
        return formData;
    }

    private void showWarning(String message) {
//        warningLabel.setText(message);
        JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
//        warningLabel.setVisible(true);
    }

    private void disasterTypeComboSelect() {
        String selectedItem = (String) disasterTypeCombo.getSelectedItem();
        switch (selectedItem) {
            case "Earthquake":
                scaleCombo.setModel(new DefaultComboBoxModel<>(new String[]{"Richter Scale", "Moment Magnitude Scale", "Modified Mercalli Intensity Scale"}));
                break;
            case "Flood":
                scaleCombo.setModel(new DefaultComboBoxModel<>(new String[]{"Flood Stage", "Inundation Level", "Streamflow Gauge Scale"}));
                break;
            case "Hurricane":
                scaleCombo.setModel(new DefaultComboBoxModel<>(new String[]{"Saffir-Simpson Scale", "Beaufort Wind Scale", "Accumulated Cyclone Energy (ACE)"}));
                break;
            case "Tornado":
                scaleCombo.setModel(new DefaultComboBoxModel<>(new String[]{"Enhanced Fujita Scale", "TORRO Scale", "Damage Path Scale"}));
                break;
            case "Wildfire":
                scaleCombo.setModel(new DefaultComboBoxModel<>(new String[]{"Fire Danger Index (FDI)", "Burn Area Index (BAI)", "Canadian Fire Weather Index (FWI)"}));
                break;
            default:
                scaleCombo.setModel(new DefaultComboBoxModel<>(new String[]{"Unknown Scale"}));
                break;
        }
            scaleCombo.setSelectedIndex(0);
            updateSliderLimits();
        }

        private void updateSliderLimits() {
            String selectedScale = (String) scaleCombo.getSelectedItem();
            switch (selectedScale) {
                case "Richter Scale":
                case "Moment Magnitude Scale":
                    scaleSlider.setMinimum(0);
                    scaleSlider.setMaximum(95);
                    break;
                case "Modified Mercalli Intensity Scale":
                    scaleSlider.setMinimum(1);
                    scaleSlider.setMaximum(12);
                    break;
                case "Flood Stage":
                case "Fire Danger Index (FDI)":
                    scaleSlider.setMinimum(0);
                    scaleSlider.setMaximum(100);
                    break;
                case "Saffir-Simpson Scale":
                    scaleSlider.setMinimum(1);
                    scaleSlider.setMaximum(5);
                    break;
                case "Beaufort Wind Scale":
                    scaleSlider.setMinimum(0);
                    scaleSlider.setMaximum(12);
                    break;
                case "Enhanced Fujita Scale":
                    scaleSlider.setMinimum(0);
                    scaleSlider.setMaximum(5);
                    break;
                case "TORRO Scale":
                    scaleSlider.setMinimum(0);
                    scaleSlider.setMaximum(11);
                    break;
                case "Burn Area Index (BAI)":
                    scaleSlider.setMinimum(0);
                    scaleSlider.setMaximum(200);
                    break;
                default: // "Inundation Level"
                    scaleSlider.setMinimum(0);
                    scaleSlider.setMaximum(10);
                    break;
            }
        }
    }