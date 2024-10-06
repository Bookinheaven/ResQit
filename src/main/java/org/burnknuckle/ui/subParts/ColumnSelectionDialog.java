package org.burnknuckle.ui.subParts;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ColumnSelectionDialog extends JDialog {
    private final Map<String, JCheckBox> columnCheckBoxes = new HashMap<>();
    private final String[] columnNames;
    public String[] selectedColumns;

    public ColumnSelectionDialog(Frame parent, String[] columnNames, Map<String, String> columnMapping) {
        super(parent, "Select Columns", true);

        // Initialize columnNames array with the keys of columnMapping
        this.columnNames = columnMapping.keySet().toArray(new String[0]);

        // Create a list to hold selected checkboxes
        JCheckBox[] checkBoxes = new JCheckBox[this.columnNames.length];
        for (int i = 0; i < this.columnNames.length; i++) {
            checkBoxes[i] = new JCheckBox(this.columnNames[i], true); // Default to selected
        }

        // Create a panel for the checkboxes
        JPanel panel = new JPanel(new GridLayout(0, 1));
        for (JCheckBox checkBox : checkBoxes) {
            panel.add(checkBox);
        }

        // Add the panel to the dialog
        add(panel, BorderLayout.CENTER);

        // Create OK and Cancel buttons
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            // Retrieve selected columns
            selectedColumns = Arrays.stream(checkBoxes)
                    .filter(JCheckBox::isSelected)
                    .map(JCheckBox::getText)
                    .toArray(String[]::new);
            setVisible(false);
            if (selectedColumns == null || selectedColumns.length == 0){
                selectedColumns = columnNames;
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            setVisible(false);
            if (selectedColumns == null || selectedColumns.length == 0){
                selectedColumns = columnNames;
            }
        });

        // Create a button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setSize(300, 300); // Set size for the dialog
        setLocationRelativeTo(parent); // Center the dialog relative to the parent
    }
}