package org.burnknuckle.ui.subParts;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton addButton, removeButton;

    public CustomTable() {
        setLayout(new BorderLayout());

        // Create a table model with sample column names
        String[] columnNames = {"ID", "Name", "Age"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // Create buttons for adding and removing rows
        addButton = new JButton("Add Row");
        removeButton = new JButton("Remove Selected Row");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Adding sample data
                Object[] newRow = {tableModel.getRowCount() + 1, "John Doe", 25};
                tableModel.addRow(newRow);
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow);
                }
            }
        });

        // Add components to the panel
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Table Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.add(new CustomTable());
        frame.setVisible(true);
    }
}
