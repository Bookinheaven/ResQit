package org.burnknuckle.ui.SubPages.User;

import org.burnknuckle.ui.subParts.RequestPagePanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;

public class RequestDisaster {
    private JPanel disasterRequestPanel;
    private JPanel mainContent;

    public JPanel createDisasterSubPage(JPanel mainContent) {
        this.mainContent = mainContent;
        if (disasterRequestPanel == null) {
            disasterRequestPanel = new JPanel();
            disasterRequestPanel.setLayout(new BorderLayout());
            try {
                JPanel requestPanel = loadRequestPage();
                disasterRequestPanel.add(requestPanel, BorderLayout.CENTER);
            } catch (IOException e) {
                logger.error("Error in RequestDisaster.createDisasterSubPage: %s".formatted(getStackTraceAsString(e)));
            }
        }
        mainContent.setLayout(new BorderLayout());
        mainContent.add(disasterRequestPanel, BorderLayout.CENTER);
        return disasterRequestPanel;
    }

    private JPanel loadRequestPage() throws IOException {
        return new RequestPagePanel(mainContent);
    }
}
