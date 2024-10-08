package org.burnknuckle.ui.SubPages.User;

import javax.swing.*;
import java.awt.*;

public class HomePage {
    public JPanel createHomePage(JPanel dashSpace) {
        JPanel homePage = new JPanel();
        dashSpace.setLayout(new BorderLayout());
        dashSpace.setBackground(Color.CYAN);
        dashSpace.add(homePage, BorderLayout.CENTER);
        return homePage;
    }
}