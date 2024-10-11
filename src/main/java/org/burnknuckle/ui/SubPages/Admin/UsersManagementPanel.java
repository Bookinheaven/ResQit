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

public class UsersManagementPanel {
    private String[] defaultColumnNamesAU;
    private Map<String, String> columnMappingAU;
    private String[] selectedColumnsAU;
    private List<Map<String, Object>> dataAU;
    private DefaultTableModel adminsTableModel;
    private DefaultTableModel usersTableModel;

    public JPanel createUsersCoAdminsPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        outer.setBackground(Color.red);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        columnMappingAU = new LinkedHashMap<>(); // HashMap is not saving in ordered manner
        columnMappingAU.put("Username", "username");
        columnMappingAU.put("Password", "password");
        columnMappingAU.put("Privilege", "privilege");
        columnMappingAU.put("Email", "email");
        columnMappingAU.put("Gender", "gender");
        columnMappingAU.put("Role", "role");
        columnMappingAU.put("First Name", "first_name");
        columnMappingAU.put("Last Name", "last_name");
        columnMappingAU.put("Phone Number", "phone_number");
        columnMappingAU.put("DOB", "date_of_birth");
        columnMappingAU.put("Account Created", "account_created");
        columnMappingAU.put("Last Login", "last_login");
        columnMappingAU.put("Status", "is_active");
        columnMappingAU.put("DP", "profile_picture_url");
        columnMappingAU.put("bio", "bio");
        columnMappingAU.put("Failed logins", "failed_login_attempts");
        columnMappingAU.put("Password Last Updated", "password_last_updated");


        defaultColumnNamesAU = new String[]{"Sno", "Username", "Email", "Role", "Last Login", "Status", "Account Created"};
        selectedColumnsAU = defaultColumnNamesAU;
        usersTableModel = new DefaultTableModel(defaultColumnNamesAU, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                String name = getColumnName(column);
                if (name.equals("Sno") || name.equals("Username") || name.equals("Last Login") || name.equals("Account Created") || name.equals("Status")){
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };
        adminsTableModel = new DefaultTableModel(defaultColumnNamesAU, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                String name = getColumnName(column);
                if (name.equals("Sno") || name.equals("Username") || name.equals("Last Login") || name.equals("Account Created") || name.equals("Status")){
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };

        dataAU = refreshDataAU(adminsTableModel, usersTableModel, columnMappingAU, defaultColumnNamesAU);
        JTable adminTable = new JTable(adminsTableModel);
        JTable usersTable = new JTable(usersTableModel);

        adminTable.setEnabled(false);
        usersTable.setEnabled(false);

        JScrollPane adminsScrollPane = new JScrollPane(adminTable);
        JScrollPane usersScrollPane = new JScrollPane(usersTable);

        JLabel adminLabel = new JLabel("Admins", SwingConstants.LEFT);
        adminLabel.setFont(new Font("Inter", Font.BOLD, 25));

        JLabel userLabel = new JLabel("Users", SwingConstants.LEFT);
        userLabel.setFont(new Font("Inter", Font.BOLD, 25));

        ImageIcon editButtonIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("AdminDashboardPanel/edit.svg")));
        ImageIcon saveButtonIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("AdminDashboardPanel/save.svg")));
        ImageIcon deleteButtonIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("AdminDashboardPanel/delete.svg")));

        JPanel adminsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton adminEditButton = new JButton();
        adminEditButton.setToolTipText("Edit");
        adminEditButton.setIcon(editButtonIcon);
        adminEditButton.setPreferredSize(new Dimension(40,40));
        adminsPanel.add(adminEditButton);

        JButton adminSaveButton = new JButton();
        adminSaveButton.setToolTipText("Save");
        adminSaveButton.setIcon(saveButtonIcon);
        adminSaveButton.setEnabled(false);
        adminSaveButton.setPreferredSize(new Dimension(40,40));
        adminsPanel.add(adminSaveButton);

        JButton adminDeleteButton = new JButton();
        adminDeleteButton.setToolTipText("Delete");
        adminDeleteButton.setIcon(deleteButtonIcon);
        adminDeleteButton.setEnabled(false);
        adminDeleteButton.setPreferredSize(new Dimension(40,40));
        adminsPanel.add(adminDeleteButton);

        JPanel usersPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton usersEditButton = new JButton();
        usersEditButton.setToolTipText("Edit");
        usersEditButton.setIcon(editButtonIcon);
        usersEditButton.setPreferredSize(new Dimension(40,40));
        usersPanel.add(usersEditButton);

        JButton usersSaveButton = new JButton();
        usersSaveButton.setToolTipText("Save");
        usersSaveButton.setIcon(saveButtonIcon);
        usersSaveButton.setEnabled(false);
        usersSaveButton.setPreferredSize(new Dimension(40,40));
        usersPanel.add(usersSaveButton);

        JButton usersDeleteButton = new JButton();
        usersDeleteButton.setToolTipText("Delete");
        usersDeleteButton.setIcon(deleteButtonIcon);
        usersDeleteButton.setEnabled(false);
        usersDeleteButton.setPreferredSize(new Dimension(40,40));
        usersPanel.add(usersDeleteButton);

        addTableButtonListenersAU(adminTable, adminsTableModel, adminLabel , adminEditButton, adminSaveButton, adminDeleteButton, columnMappingAU, dataAU);
        addTableButtonListenersAU(usersTable, usersTableModel, userLabel, usersEditButton, usersSaveButton, usersDeleteButton, columnMappingAU, dataAU);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton();
        refreshButton.setToolTipText("Refresh");
        refreshButton.setPreferredSize(new Dimension(40,40));
        ImageIcon refreshIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Common/refresh-cw.svg")));
        refreshButton.setIcon(refreshIcon);
        refreshButton.addActionListener(_ -> dataAU = refreshDataAU(adminsTableModel, usersTableModel, columnMappingAU, selectedColumnsAU));
        JButton columnSelectionButton = new JButton();
        ImageIcon filterIcon = new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Common/filter.svg")));
        columnSelectionButton.setToolTipText("Column Filter");
        columnSelectionButton.setPreferredSize(new Dimension(40,40));
        columnSelectionButton.setIcon(filterIcon);
        columnSelectionButton.addActionListener(_ -> {
            ColumnFilterDialog dialog = new ColumnFilterDialog(null, selectedColumnsAU, columnMappingAU);
            dialog.setVisible(true);
            selectedColumnsAU = dialog.selectedColumns;
            if(selectedColumnsAU == null){
                selectedColumnsAU = defaultColumnNamesAU;
            }
            updateTableModelsAU(selectedColumnsAU);
        });
        topBar.add(columnSelectionButton);
        topBar.add(refreshButton);

        gbc.weighty = 0.1;
        gbc.gridx = 2; gbc.gridy = 0; panel.add(topBar, gbc);

        gbc.gridwidth = 3;
        gbc.weighty = 0;
        gbc.gridx = 0; gbc.gridy = 1; panel.add(adminLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(userLabel, gbc);

        gbc.gridwidth = 3;
        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 2; panel.add(adminsScrollPane, gbc);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(usersScrollPane, gbc);

        gbc.gridwidth = 1;
        gbc.weighty = 0;
        gbc.gridx = 2; gbc.gridy = 1; panel.add(adminsPanel, gbc);

        gbc.gridwidth = 1;
        gbc.weighty = 0;
        gbc.gridx = 2; gbc.gridy = 3; panel.add(usersPanel, gbc);

        outer.add(panel, BorderLayout.CENTER);
        return outer;
    }

    private void updateTableModelsAU(String[] selectedColumns) {
        adminsTableModel.setColumnCount(0);
        usersTableModel.setColumnCount(0);
        adminsTableModel.setColumnIdentifiers(selectedColumns);
        usersTableModel.setColumnIdentifiers(selectedColumns);
        dataAU = refreshDataAU(adminsTableModel, usersTableModel, columnMappingAU, selectedColumns);
    }

    private java.util.List<Map<String, Object>> refreshDataAU(DefaultTableModel adminsTableModel, DefaultTableModel usersTableModel, Map<String, String> columnMapping, String[] selectedColumns) {
        adminsTableModel.setRowCount(0);
        usersTableModel.setRowCount(0);
        java.util.List<Map<String, Object>> data = null;
        try {
            Database db = Database.getInstance();
            db.getConnection();
            data = db.getAllData(0, "");
        } catch (SQLException e) {
            logger.error("Error in AdminDashboardPanel.java: |SQLException while refreshing data| %s \n".formatted(getStackTraceAsString(e)));
        }
        if (selectedColumns == null){
            selectedColumns = defaultColumnNamesAU;
        }
        if (data != null) {
            int snoAdmins = 1;
            int snoUsers = 1;
            for (Map<String, Object> item : data) {
                java.util.List<String> row = new ArrayList<>();
                for (String selectedColumn : selectedColumns) {
                    String current = columnMapping.get(selectedColumn);
                    row.add(String.valueOf(item.get(current)));
                }
                String status = String.valueOf(item.get("privilege"));
                switch (status) {
                    case "co-admin":
                        row.removeFirst();
                        row.addFirst(String.valueOf(snoAdmins));
                        adminsTableModel.addRow(row.toArray());
                        snoAdmins+=1;
                        break;
                    case "user":
                        row.removeFirst();
                        row.addFirst(String.valueOf(snoUsers));
                        usersTableModel.addRow(row.toArray());
                        snoUsers+=1;
                        break;
                }
            }
        }
        return data;
    }

    private void addTableButtonListenersAU(JTable table, DefaultTableModel tableModel, JLabel tableLabel ,JButton editButton, JButton saveButton, JButton deleteButton, Map<String, String> columnMapping, List<Map<String, Object>> changeData) {
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
                Database.getInstance().updateData(0,rowData);
                JOptionPane.showMessageDialog(
                        null,
                        "Data updated successfully for row: " + selectedRow,
                        "Update Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );
                logger.info("Data updated successfully for row: {}", selectedRow);
                dataAU = refreshDataAU(adminsTableModel, usersTableModel, columnMappingAU, selectedColumnsAU);

            } else {
                logger.error("Invalid selected row: {}", selectedRow);
            }
            table.setEnabled(false);
            deleteButton.setEnabled(false);
        });

        deleteButton.addActionListener(_ -> {
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
                dataAU = refreshDataAU(adminsTableModel, usersTableModel, columnMappingAU, selectedColumnsAU);
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
