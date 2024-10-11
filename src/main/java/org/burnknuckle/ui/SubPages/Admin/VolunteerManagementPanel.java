package org.burnknuckle.ui.SubPages.Admin;

import com.formdev.flatlaf.extras.components.FlatTabbedPane;
import com.formdev.flatlaf.ui.FlatTabbedPaneUI;
import org.burnknuckle.utils.Database;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.burnknuckle.ui.subParts.AdminUsersLabel.setTitleBar;
import static org.burnknuckle.ui.subParts.AdminUsersLabel.userFrames;
import static org.burnknuckle.utils.ThemeManager.ADPThemeData;
import static org.burnknuckle.utils.ThemeManager.getColorFromHex;

public class VolunteerManagementPanel {

    public JPanel createVolunteerManagementPanel() {
        Database db = Database.getInstance();
        java.util.List<Map<String, Object>>  coAdminData = db.getAllData(0,"privilege");
        ArrayList<String> userDataDetails = new ArrayList<>(Arrays.asList(
                "Sno", "Username", "Gender", "Role", "Email", "Privilege", "Password",
                "First Name", "Last Name", "Phone Number", "Date of Birth",
                "Account Creation", "Last Login", "Active Status", "Address",
                "Profile Picture URL", "Bio", "Failed Login Attempts",
                "Password Last Updated"
        ));

        ArrayList<Integer> selectedTitles = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        ArrayList<String> selectedUserDataDetails = new ArrayList<>();
        for (Integer index : selectedTitles) {
            selectedUserDataDetails.add(userDataDetails.get(index));
        }
        JPanel pageBackGroundPanel = new JPanel();
        pageBackGroundPanel.setBackground(getColorFromHex(ADPThemeData.get("TabTitleBg")));
        pageBackGroundPanel.setLayout(new BorderLayout());

        JPanel roleManagementInner = new JPanel();
        roleManagementInner.setLayout(new GridBagLayout());
        GridBagConstraints page1Gbc = new GridBagConstraints();
        page1Gbc.gridx = 0;
        page1Gbc.gridy = 0;
        page1Gbc.anchor = GridBagConstraints.NORTHWEST;
        page1Gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Roles");
        title.setFont(new Font("Inter", Font.BOLD, 38));
        roleManagementInner.add(title, page1Gbc);

        JPanel pageInner = new JPanel();
        pageInner.setLayout(new GridBagLayout());

        GridBagConstraints pageInnerGbc = new GridBagConstraints();
        pageInnerGbc.gridx = 0;
        pageInnerGbc.gridy = 0;
        pageInnerGbc.fill = GridBagConstraints.HORIZONTAL;
//        pageInnerGbc.weightx = 1.0;
        pageInnerGbc.anchor = GridBagConstraints.NORTHWEST;
        pageInnerGbc.insets = new Insets(0,0,0,10);

        JPanel columnTitles = setTitleBar(selectedUserDataDetails);
        columnTitles.setBackground(Color.red);
        pageInner.add(columnTitles, pageInnerGbc);

        pageInnerGbc.gridy = 1;
        pageInnerGbc.weightx = 1.0;
        pageInnerGbc.weighty = 1.0;
        pageInnerGbc.fill = GridBagConstraints.BOTH;
        pageInnerGbc.anchor = GridBagConstraints.CENTER;

        JScrollPane userBlock = new JScrollPane(userFrames(coAdminData, selectedUserDataDetails));
        userBlock.setBackground(Color.CYAN);
        pageInner.add(userBlock, pageInnerGbc);

        page1Gbc.gridy = 1;
        page1Gbc.fill = GridBagConstraints.BOTH;
        page1Gbc.weightx = 1.0;
        page1Gbc.weighty = 1.0;
        page1Gbc.insets = new Insets(10, 10, 10, 20);

        roleManagementInner.add(pageInner, page1Gbc);

        JPanel page2 = new JPanel();
        page2.add(new JLabel("This is Tab 2"));
        JPanel page3 = new JPanel();
        page3.add(new JLabel("This is Tab 3"));

        JTabbedPane tabbedPane = new FlatTabbedPane();
        tabbedPane.setUI(new CustomFlatTabbedPaneUI());
        tabbedPane.addTab("Role Management", roleManagementInner);
        tabbedPane.addTab("Users Management", page2);
        tabbedPane.addTab("Request Management", page3);

        pageBackGroundPanel.add(tabbedPane, BorderLayout.CENTER);
        return pageBackGroundPanel;
    }

    static class CustomFlatTabbedPaneUI extends FlatTabbedPaneUI {
        @Override
        protected Insets getTabInsets(int tabPlacement, int tabIndex) {
            return new Insets(20, 20, 20, 20);
        }
        @Override
        protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
            g.setFont(font);
            if (isSelected) {
                g.setColor(getColorFromHex(ADPThemeData.get("TabTitleTextColorSelected")));
            } else {
                g.setColor(getColorFromHex(ADPThemeData.get("TabTitleTextColorNormal")));
            }
            g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
        }
        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            if (isSelected) {
                g.setColor(getColorFromHex(ADPThemeData.get("TabTitleSelected")));
            } else {
                g.setColor(getColorFromHex(ADPThemeData.get("TabTitleBg")));
            }
            g.fillRect(x, y, w, h);
        }
        @Override
        protected void paintTabSelection(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h) {
            g.setColor(getColorFromHex(ADPThemeData.get("TabTitleTextColorSelected")));
            g.fillRect(x, y + h - 3, w, 3);
        }
    }

}
