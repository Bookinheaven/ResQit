package org.burnknuckle.ui.SubPages.Admin;

import javax.swing.*;

import static org.burnknuckle.utils.ThemeManager.ADPThemeData;
import static org.burnknuckle.utils.ThemeManager.getColorFromHex;

public class ResourcesStatus {
    public JPanel createDeliveryStatusPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(getColorFromHex(ADPThemeData.get("background")));
        panel.add(new JLabel("Delivery Status Content Here"));
        return panel;
    }

}
