package org.burnknuckle.ui.SubPages.User;

import javax.swing.*;
import java.awt.*;

public class AccountPage {
    public JPanel AccountPage(JFrame frame) {
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        JPanel accountPanel = new JPanel();
        accountPanel.setPreferredSize(new Dimension(frame.getWidth() / 2, frame.getHeight() / 2));
        accountPanel.setBackground(Color.GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        backgroundPanel.add(accountPanel, gbc);
        return backgroundPanel;
    }

}
