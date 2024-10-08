package org.burnknuckle.ui.subParts;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BKTable extends JPanel {
    // Given panel
    private final java.util.List<Dimension> sizeArray = new ArrayList<>();
    public BKTable(JPanel main) {

        setLayout(new BorderLayout());
        String[] columnNames = {"Name", "Role", "Group", "Status"};
        JPanel tablePanel = createTablePanel();
        Object[][] rowData = {
                {"Tanvik", "Admin", "hero", "active"},
                {"Dharvik", "Co-Admin", "villain", "active"},
                {"Sung Jinwoo", "Hero", "hero", "active"}, {"Dharvik", "Co-Admin", "villain", "active"},
                {"Sung Jinwoo", "Hero", "hero", "active"}, {"Dharvik", "Co-Admin", "villain", "active"},
                {"Sung Jinwoo", "Hero", "hero", "active"}
        };
        System.out.println(main.getWidth());
        for (String _ : columnNames){
            sizeArray.add(new Dimension(main.getWidth()/columnNames.length, main.getHeight()));
        }
        System.out.println(sizeArray.toString());

//        JScrollPane tableBodyPanel = createTableBodyPanel(rowData, columnNames.length);
        add(tablePanel, BorderLayout.NORTH);
//        add(tableBodyPanel, BorderLayout.CENTER);
    }


    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setPreferredSize(new Dimension(200,200));
        tablePanel.setBackground(Color.GREEN);

        return tablePanel;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatDarkLaf.setup();
            JFrame frame = new JFrame("Custom Table");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);

            frame.setLayout(new BorderLayout());
            JPanel main = new JPanel();
            main.setSize(new Dimension(100,200));
            BKTable table = new BKTable(main);
            frame.add(table, BorderLayout.CENTER);

            frame.setVisible(true);
        });

    }
}
