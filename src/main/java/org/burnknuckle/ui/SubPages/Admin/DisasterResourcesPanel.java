package org.burnknuckle.ui.SubPages.Admin;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.burnknuckle.ui.subParts.ColumnFilterDialog;
import org.burnknuckle.utils.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.ui.AdminDashboardPanel.showDateTimePicker;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;

public class DisasterResourcesPanel {
    private List<Map<String, Object>> dataD;
    private DefaultTableModel resolvedTableModel ;
    private DefaultTableModel requestedTableModel;
    private DefaultTableModel ongoingTableModel;
    private Map<String, String> columnMappingD;
    private String[] selectedColumnsD;
    private String[] defaultColumnNamesD;

    public JPanel createDisasterResourcesPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        outer.setBackground(Color.red);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        columnMappingD = new LinkedHashMap<>(); // HashMap is not saving in ordered manner
        columnMappingD.put("ID", "id");
        columnMappingD.put("Name", "disastername");
        columnMappingD.put("Type", "disastertype");
        columnMappingD.put("Scale", "scale");
        columnMappingD.put("Severity", "severity");
        columnMappingD.put("Location", "location");
        columnMappingD.put("Start Date", "startdate");
        columnMappingD.put("End Date", "enddate");
        columnMappingD.put("Status", "responsestatus");
        columnMappingD.put("Uploaded User", "useruploaded");
        columnMappingD.put("Entry date", "dateOfentry");
        columnMappingD.put("Impact Assessment", "impactassessment");
        columnMappingD.put("Last Updated", "lastdpdated");
        columnMappingD.put("Description", "description");
        columnMappingD.put("Meter", "scalemeter");

        defaultColumnNamesD = new String[]{"ID", "Name", "Type", "Scale", "Severity", "Location", "Start Date", "End Date", "Status"};
        selectedColumnsD = defaultColumnNamesD;
        resolvedTableModel = new DefaultTableModel(defaultColumnNamesD, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                String name = getColumnName(column);
                if (name.equals("Start Date") || name.equals("End Date")){
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };
        requestedTableModel = new DefaultTableModel(defaultColumnNamesD, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                String name = getColumnName(column);
                if (name.equals("Start Date") || name.equals("End Date")){
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };
        ongoingTableModel = new DefaultTableModel(defaultColumnNamesD, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                String name = getColumnName(column);
                if (name.equals("Start Date") || name.equals("End Date")){
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };

        dataD = refreshData(resolvedTableModel, requestedTableModel, ongoingTableModel, columnMappingD, defaultColumnNamesD);

        JTable resolvedTable = new JTable(resolvedTableModel);
        JTable requestedTable = new JTable(requestedTableModel);
        JTable ongoingTable = new JTable(ongoingTableModel);

        resolvedTable.setEnabled(false);
        requestedTable.setEnabled(false);
        ongoingTable.setEnabled(false);

        JScrollPane resolvedScrollPane = new JScrollPane(resolvedTable);
        JScrollPane requestedScrollPane = new JScrollPane(requestedTable);
        JScrollPane ongoingScrollPane = new JScrollPane(ongoingTable);

        JLabel resolvedLabel = new JLabel("Resolved Disasters", SwingConstants.LEFT);
        resolvedLabel.setFont(new Font("Inter", Font.BOLD, 25));

        JLabel requestedLabel = new JLabel("Requested Disasters", SwingConstants.LEFT);
        requestedLabel.setFont(new Font("Inter", Font.BOLD, 25));

        JLabel ongoingLabel = new JLabel("Ongoing Disasters", SwingConstants.LEFT);
        ongoingLabel.setFont(new Font("Inter", Font.BOLD, 25));

        ImageIcon editButtonIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("AdminDashboardPanel/edit.svg")));
        ImageIcon saveButtonIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("AdminDashboardPanel/save.svg")));
        ImageIcon deleteButtonIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("AdminDashboardPanel/delete.svg")));

        JPanel resolvedPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton resolvedEditButton = new JButton();
        resolvedEditButton.setToolTipText("Edit");
        resolvedEditButton.setIcon(editButtonIcon);
        resolvedEditButton.setPreferredSize(new Dimension(40,40));
        resolvedPanel.add(resolvedEditButton);

        JButton resolvedSaveButton = new JButton();
        resolvedSaveButton.setToolTipText("Save");
        resolvedSaveButton.setIcon(saveButtonIcon);
        resolvedSaveButton.setEnabled(false);
        resolvedSaveButton.setPreferredSize(new Dimension(40,40));
        resolvedPanel.add(resolvedSaveButton);

        JButton resolvedDeleteButton = new JButton();
        resolvedDeleteButton.setToolTipText("Delete");
        resolvedDeleteButton.setIcon(deleteButtonIcon);
        resolvedDeleteButton.setEnabled(false);
        resolvedDeleteButton.setPreferredSize(new Dimension(40,40));
        resolvedPanel.add(resolvedDeleteButton);

        JPanel requestedPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton requestedEditButton = new JButton();
        requestedEditButton.setToolTipText("Edit");
        requestedEditButton.setIcon(editButtonIcon);
        requestedEditButton.setPreferredSize(new Dimension(40,40));
        requestedPanel.add(requestedEditButton);

        JButton requestedSaveButton = new JButton();
        requestedSaveButton.setToolTipText("Save");
        requestedSaveButton.setIcon(saveButtonIcon);
        requestedSaveButton.setEnabled(false);
        requestedSaveButton.setPreferredSize(new Dimension(40,40));
        requestedPanel.add(requestedSaveButton);

        JButton requestedDeleteButton = new JButton();
        requestedDeleteButton.setToolTipText("Delete");
        requestedDeleteButton.setIcon(deleteButtonIcon);
        requestedDeleteButton.setEnabled(false);
        requestedDeleteButton.setPreferredSize(new Dimension(40,40));
        requestedPanel.add(requestedDeleteButton);

        JPanel onGoingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ongoingEditButton = new JButton();
        ongoingEditButton.setToolTipText("Edit");
        ongoingEditButton.setIcon(editButtonIcon);
        ongoingEditButton.setPreferredSize(new Dimension(40,40));
        onGoingPanel.add(ongoingEditButton);

        JButton ongoingSaveButton = new JButton();
        ongoingSaveButton.setToolTipText("Save");
        ongoingSaveButton.setIcon(saveButtonIcon);
        ongoingSaveButton.setEnabled(false);
        ongoingSaveButton.setPreferredSize(new Dimension(40,40));
        onGoingPanel.add(ongoingSaveButton);

        JButton ongoingDeleteButton = new JButton();
        ongoingDeleteButton.setToolTipText("Delete");
        ongoingDeleteButton.setIcon(deleteButtonIcon);
        ongoingDeleteButton.setEnabled(false);
        ongoingDeleteButton.setPreferredSize(new Dimension(40,40));
        onGoingPanel.add(ongoingDeleteButton);

        addTableButtonListeners(resolvedTable, resolvedTableModel, resolvedLabel , resolvedEditButton, resolvedSaveButton, resolvedDeleteButton, columnMappingD, dataD);
        addTableButtonListeners(requestedTable, requestedTableModel, requestedLabel, requestedEditButton, requestedSaveButton, requestedDeleteButton, columnMappingD, dataD);
        addTableButtonListeners(ongoingTable, ongoingTableModel, ongoingLabel, ongoingEditButton, ongoingSaveButton, ongoingDeleteButton, columnMappingD, dataD);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton();
        refreshButton.setToolTipText("Refresh");
        refreshButton.setPreferredSize(new Dimension(40,40));
        ImageIcon refreshIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Common/refresh-cw.svg")));
        refreshButton.setIcon(refreshIcon);
        refreshButton.addActionListener(_ -> dataD = refreshData(resolvedTableModel, requestedTableModel, ongoingTableModel, columnMappingD, selectedColumnsD));
        JButton columnSelectionButton = new JButton();
        ImageIcon filterIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Common/filter.svg")));
        columnSelectionButton.setToolTipText("Column Filter");
        columnSelectionButton.setPreferredSize(new Dimension(40,40));
        columnSelectionButton.setIcon(filterIcon);
        columnSelectionButton.addActionListener(_ -> {
            ColumnFilterDialog dialog = new ColumnFilterDialog(null, selectedColumnsD, columnMappingD);
            dialog.setVisible(true);
            selectedColumnsD = dialog.selectedColumns;
            if(selectedColumnsD == null){
                selectedColumnsD = defaultColumnNamesD;
            }
            updateTableModels(selectedColumnsD);
        });
        topBar.add(columnSelectionButton);
        topBar.add(refreshButton);

        gbc.weighty = 0.1;
        gbc.gridx = 2; gbc.gridy = 0; panel.add(topBar, gbc);

        gbc.gridwidth = 3;
        gbc.weighty = 0;
        gbc.gridx = 0; gbc.gridy = 1; panel.add(resolvedLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(requestedLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 7; panel.add(ongoingLabel, gbc);

        gbc.gridwidth = 3;
        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 2; panel.add(resolvedScrollPane, gbc);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(requestedScrollPane, gbc);
        gbc.gridx = 0; gbc.gridy = 8; panel.add(ongoingScrollPane, gbc);

        gbc.gridwidth = 1;
        gbc.weighty = 0;
        gbc.gridx = 2; gbc.gridy = 1; panel.add(resolvedPanel, gbc);

        gbc.gridwidth = 1;
        gbc.weighty = 0;
        gbc.gridx = 2; gbc.gridy = 3; panel.add(requestedPanel, gbc);

        gbc.gridwidth = 1;
        gbc.weighty = 0;
        gbc.gridx = 2; gbc.gridy = 7; panel.add(onGoingPanel, gbc);

        outer.add(panel, BorderLayout.CENTER);
        return outer;
    }

    private void updateTableModels(String[] selectedColumns) {
        resolvedTableModel.setColumnCount(0);
        requestedTableModel.setColumnCount(0);
        ongoingTableModel.setColumnCount(0);
        resolvedTableModel.setColumnIdentifiers(selectedColumns);
        requestedTableModel.setColumnIdentifiers(selectedColumns);
        ongoingTableModel.setColumnIdentifiers(selectedColumns);
        dataD = refreshData(resolvedTableModel, requestedTableModel, ongoingTableModel, columnMappingD, selectedColumns);
    }

    private java.util.List<Map<String, Object>> refreshData(DefaultTableModel resolvedTableModel, DefaultTableModel requestedTableModel, DefaultTableModel ongoingTableModel, Map<String, String> columnMapping, String[] selectedColumns) {
        resolvedTableModel.setRowCount(0);
        requestedTableModel.setRowCount(0);
        ongoingTableModel.setRowCount(0);

        java.util.List<Map<String, Object>> data = null;
        try {
            Database db = Database.getInstance();
            db.getConnection();
            data = db.getAllData(2, "");
        } catch (SQLException e) {
            logger.error("Error in AdminDashboardPanel.java: |SQLException while refreshing data| %s \n".formatted(getStackTraceAsString(e)));
        }
        if (selectedColumns == null){
            selectedColumns = defaultColumnNamesD;
        }
        if (data != null) {
            for (Map<String, Object> item : data) {
                java.util.List<String> row = new ArrayList<>();
                for (String selectedColumn : selectedColumns) {
                    String current = columnMapping.get(selectedColumn);
                    row.add(String.valueOf(item.get(current)));
                }
                String status = String.valueOf(item.get("responsestatus"));
                switch (status) {
                    case "resolved":
                        resolvedTableModel.addRow(row.toArray());
                        break;
                    case "requested":
                        requestedTableModel.addRow(row.toArray());
                        break;
                    case "ongoing":
                        ongoingTableModel.addRow(row.toArray());
                        break;
                }
            }
        }
        return data;
    }

    private void addTableButtonListeners(JTable table, DefaultTableModel tableModel, JLabel tableLabel ,JButton editButton, JButton saveButton, JButton deleteButton, Map<String, String> columnMapping, List<Map<String, Object>> changeData) {
        editButton.addActionListener(_ -> {
            table.setEnabled(!table.isEnabled());
            if (table.isEnabled()) {
                boolean set = tableLabel.getText().contains(" (Editing)*");
                if(!set){
                    tableLabel.setText(tableLabel.getText().concat(" (Editing)*"));
                }
            } else {
                tableLabel.setText(tableLabel.getText().replace(" (Editing)*", ""));
            }
        });
        table.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (table.isEnabled()){
                    deleteButton.setEnabled(true);
                    saveButton.setEnabled(true);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (!table.isEnabled()){
                    deleteButton.setEnabled(false);
                    saveButton.setEnabled(false);
                }
            }
        });
        saveButton.addActionListener(_ -> {
            saveButton.setEnabled(false);
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < changeData.size()) {
                Map<String, Object> rowData = changeData.get(selectedRow);
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    String columnName = tableModel.getColumnName(i);
                    String dbColumnName = columnMapping.get(columnName);
                    if (dbColumnName != null) {
                        Object value = table.getValueAt(selectedRow, i);
                        rowData.put(dbColumnName, (value != null) ? value.toString() : null);
                    }
                }
                Database.getInstance().updateData(2,rowData);
                JOptionPane.showMessageDialog(
                        null,
                        "Data updated successfully for row: " + selectedRow,
                        "Update Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );
                logger.info("[AdminDashboardPanel.java] [addTableButtonListeners]:Data updated successfully for row: {}", selectedRow);
                dataD = refreshData(resolvedTableModel, requestedTableModel, ongoingTableModel, columnMapping, selectedColumnsD);

            } else {
                logger.error("Error in [AdminDashboardPanel.java] [addTableButtonListeners]: Invalid selected row: {}", selectedRow);
            }
            table.setEnabled(false);
            deleteButton.setEnabled(false);
            saveButton.setEnabled(false);
        });

        deleteButton.addActionListener(_ -> {
            logger.info("Row ");

            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Object idValue = table.getValueAt(selectedRow, 0);
                tableModel.removeRow(selectedRow);
                Database.getInstance().deleteData(2, "id", idValue);
                logger.info("Row deleted at index: {}, with ID: {}", selectedRow, idValue);
                JOptionPane.showMessageDialog(
                        null,
                        "Data deleted successfully for row: " + selectedRow,
                        "Deleted Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dataD = refreshData(resolvedTableModel, requestedTableModel, ongoingTableModel, columnMapping, selectedColumnsD);
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!table.isEnabled()) return;
                if(e.getClickCount() == 2 && !e.isConsumed()){
                    e.consume();
                    int row = table.rowAtPoint(e.getPoint());
                    int column = table.columnAtPoint(e.getPoint());
                    String columnName = tableModel.getColumnName(column);
                    if (columnName.equals("Start Date") || columnName.equals("End Date")) {
                        showDateTimePicker(table, row, column);
                    }
                }
            }
        });
    }

}
