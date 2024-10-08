package org.burnknuckle.ui.SubPages.Admin;

import javax.swing.*;
import java.awt.*;

import static org.burnknuckle.utils.ThemeManager.ADPThemeData;
import static org.burnknuckle.utils.ThemeManager.getColorFromHex;

public class DashboardPanel {
    public JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(getColorFromHex(ADPThemeData.get("background")));

        JLabel headerLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        panel.add(headerLabel, BorderLayout.NORTH);


        return panel;
    }

}
