package org.burnknuckle.ui.subParts;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.burnknuckle.model.ThemeManager.ADPThemeData;
import static org.burnknuckle.model.ThemeManager.getColorFromHex;

public class AdminUsersLabel extends JPanel {
    public static Icon arrowDownIcon = new FlatSVGIcon(Objects.requireNonNull(AdminUsersLabel.class.getClassLoader().getResource("Common/triangle_down.svg")));
    public static Icon arrowUpIcon = new FlatSVGIcon(Objects.requireNonNull(AdminUsersLabel.class.getClassLoader().getResource("Common/triangle_up.svg")));
    public static Icon female = new FlatSVGIcon(Objects.requireNonNull(AdminUsersLabel.class.getClassLoader().getResource("icons/female.svg")));
    public static Icon male = new FlatSVGIcon(Objects.requireNonNull(AdminUsersLabel.class.getClassLoader().getResource("icons/male.svg")));

    private static JLabel createTitleLabel(String title, boolean[] check){
        JLabel main = new JLabel();
        main.setText(title);
        main.setHorizontalTextPosition(SwingConstants.LEFT);
        main.setFont(new Font("Inter", Font.PLAIN, 16));
        if (!title.equals("GENDER")){
            main.setIcon(arrowDownIcon);
            main.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (check[0]){
                        main.setIcon(arrowDownIcon);
                        check[0] = false;
                    } else {
                        main.setIcon(arrowUpIcon);
                        check[0] = true;
                    }
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    main.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    main.setCursor(Cursor.getDefaultCursor());
                }
            });
        }
        else {
            main.setIcon(male);
            main.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (check[0]){
                        main.setIcon(male);
                        check[0] = false;
                    } else {
                        main.setIcon(female);
                        check[0] = true;
                    }
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    main.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    main.setCursor(Cursor.getDefaultCursor());
                }
            });
        }
        return main;
    }

    public static JPanel setTitleBar(ArrayList<String> TitlesData){
        TitlesData.replaceAll(String::toUpperCase);
        JPanel ColumnTitles = new JPanel();
        ColumnTitles.setLayout(new GridBagLayout());
        ColumnTitles.setBackground(getColorFromHex(ADPThemeData.get("background")));

        GridBagConstraints CTGbc = new GridBagConstraints();
        CTGbc.gridy = 0;
        CTGbc.insets = new Insets(13, 10, 13, 10);
        CTGbc.fill = GridBagConstraints.HORIZONTAL;
        CTGbc.weighty = 1.0;

        CTGbc.gridx= 0;
        CTGbc.weightx = 0.05;
        JCheckBox selectAllCheckBox = new JCheckBox();
        selectAllCheckBox.setIcon(new FlatSVGIcon(Objects.requireNonNull(AdminUsersLabel.class.getClassLoader().getResource("icons/checkBoxE.svg"))));
        selectAllCheckBox.setSelectedIcon(new FlatSVGIcon(Objects.requireNonNull(AdminUsersLabel.class.getClassLoader().getResource("icons/disabledBox.svg"))));
        selectAllCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                selectAllCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                selectAllCheckBox.setCursor(Cursor.getDefaultCursor());
            }
        });
        ColumnTitles.add(selectAllCheckBox,CTGbc);

        final boolean[] firstArrowIcon = {false};
        CTGbc.gridx= 1;
        CTGbc.weightx = 0.25;
        JLabel firstTile = createTitleLabel(TitlesData.get(0), firstArrowIcon);
        ColumnTitles.add(firstTile,CTGbc);

        CTGbc.gridx= 2;
        CTGbc.weightx = 0.25;
        final boolean[] secArrowIcon = {false};
        JLabel secTile = createTitleLabel(TitlesData.get(1), secArrowIcon);

        ColumnTitles.add(secTile,CTGbc);

        CTGbc.gridx= 3;
        CTGbc.weightx = 0.25;
        final boolean[] thirdArrowIcon = {false};
        JLabel thirdTile = createTitleLabel(TitlesData.get(2), thirdArrowIcon);
        ColumnTitles.add(thirdTile,CTGbc);

        CTGbc.gridx = 4;
        CTGbc.weightx = 0.25;
        final boolean[] fourthArrowIcon = {false};
        JLabel fourthTitle = createTitleLabel(TitlesData.get(3), fourthArrowIcon);
        ColumnTitles.add(fourthTitle, CTGbc);

        CTGbc.gridx = 5;
        CTGbc.weightx = 0.25;
        final boolean[] fifthArrowIcon = {false};
        JLabel fifthTitle = createTitleLabel(TitlesData.get(4), fifthArrowIcon);
        ColumnTitles.add(fifthTitle, CTGbc);
        return ColumnTitles;
    }

    public static JPanel userFrames(List<Map<String, Object>> data, ArrayList<String> TitlesData) {
        JPanel userPanel = new JPanel();

        userPanel.setLayout(new GridBagLayout());
        userPanel.setBackground(getColorFromHex(ADPThemeData.get("background")));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(13, 10, 30, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.05;
        int row = 0;
        for (Map<String, Object> member : data) {
            int col = 0;
            gbc.gridy = row;
            gbc.gridx = col++;
            JCheckBox selectAllCheckBox = new JCheckBox();
            selectAllCheckBox.setIcon(new FlatSVGIcon(Objects.requireNonNull(AdminUsersLabel.class.getClassLoader().getResource("icons/checkBoxE.svg"))));
            selectAllCheckBox.setSelectedIcon(new FlatSVGIcon(Objects.requireNonNull(AdminUsersLabel.class.getClassLoader().getResource("icons/disabledBox.svg"))));
            selectAllCheckBox.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    selectAllCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    selectAllCheckBox.setCursor(Cursor.getDefaultCursor());
                }
            });
            userPanel.add(selectAllCheckBox, gbc);

            for (String title : TitlesData) {
                addUserColumn(userPanel, gbc, member, title.toLowerCase(), row);
                col++;
            }
            row++;
        }

        return userPanel;
    }

    private static void addUserColumn(JPanel userPanel, GridBagConstraints gbc, Map<String, Object> member, String title, int row) {
        gbc.gridy = row;
        gbc.gridx++;
        String temp = (String) member.get(title);
        String text;
        if (temp != null && !title.equals("sno")) {
            text = temp;
        } else if (title.equals("sno")) {
            text = String.valueOf(row + 1);
        } else {
            text = "None";
        }
        boolean[] check = {false};
        gbc.weightx = 1.0;
        JLabel userLabel = createUserLabel(text, check);
        userPanel.add(userLabel, gbc);
    }


    private static JLabel createUserLabel(String text, boolean[] check) {
        JLabel label = new JLabel(text);
        label.setHorizontalTextPosition(SwingConstants.LEFT);
        label.setFont(new Font("Inter", Font.PLAIN, 14));
        label.setForeground(getColorFromHex(ADPThemeData.get("text")));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                check[0] = !check[0];
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setCursor(Cursor.getDefaultCursor());
            }
        });

        return label;
    }

}
