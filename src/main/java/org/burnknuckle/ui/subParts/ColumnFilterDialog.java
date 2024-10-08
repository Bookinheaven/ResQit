package org.burnknuckle.ui.subParts;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ColumnFilterDialog extends JDialog {
    private final String[] columnNames;
    public String[] selectedColumns;

    public ColumnFilterDialog(Frame parent, String[] givenColumnNames, Map<String, String> columnMapping) {
        super(parent, "Columns Filter", true);
        this.columnNames = columnMapping.keySet().toArray(new String[0]);
        Set<String> givenColumnNamesSet = new HashSet<>(Arrays.asList(givenColumnNames));
        JCheckBox[] checkBoxes = new JCheckBox[this.columnNames.length];
        for (int i = 0; i < this.columnNames.length; i++) {
            if (this.columnNames[i].equals("ID")){
                checkBoxes[i] = new JCheckBox(this.columnNames[i], true);
                checkBoxes[i].setEnabled(false);
            } else {
                boolean tick = givenColumnNamesSet.contains(this.columnNames[i]);
                checkBoxes[i] = new JCheckBox(this.columnNames[i], tick);
            }
        }
        JPanel panel = new JPanel(new GridLayout(0, 1));
        for (JCheckBox checkBox : checkBoxes) {
            panel.add(checkBox);
        }
        add(panel, BorderLayout.CENTER);
        JButton okButton = new JButton();
        okButton.setText("OK");
        okButton.addActionListener(_ -> {
            selectedColumns = Arrays.stream(checkBoxes)
                    .filter(JCheckBox::isSelected)
                    .map(JCheckBox::getText)
                    .toArray(String[]::new);
            setVisible(false);
            if (selectedColumns == null || selectedColumns.length == 0){
                selectedColumns = columnNames;
                setOrder(columnMapping);

            }
            if(Arrays.equals(selectedColumns, givenColumnNames)){
                selectedColumns = givenColumnNames;
                setOrder(columnMapping);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> {
            setVisible(false);
            if (selectedColumns == null || selectedColumns.length == 0){
                selectedColumns = givenColumnNames;
                setOrder(columnMapping);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
        setSize(300, 300);
        setLocationRelativeTo(parent);
    }
    private void setOrder(Map<String, String> columnMapping) {
        List<String> priorityOrder = new ArrayList<>(columnMapping.keySet());
        List<String> selectedColumnsList = new ArrayList<>(Arrays.asList(selectedColumns));
        List<String> sortedColumns = new ArrayList<>();
        for (String column : priorityOrder) {
            if (selectedColumnsList.contains(column)) {
                sortedColumns.add(column);
            }
        }
        selectedColumns = sortedColumns.toArray(new String[0]);
    }
}