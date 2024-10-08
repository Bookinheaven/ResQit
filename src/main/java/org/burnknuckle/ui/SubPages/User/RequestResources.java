package org.burnknuckle.ui.SubPages.User;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;

public class RequestResources {
    public JFXPanel createRequestResourcesSubPage(JPanel dashSpace) {
        JFXPanel requestPanel = new JFXPanel();
        dashSpace.setLayout(new BorderLayout());
        dashSpace.add(requestPanel, BorderLayout.CENTER);
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/user/resourcesRequest.fxml")));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
//                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fxml/user/style.css")).toExternalForm());
                requestPanel.setScene(scene);
            } catch (IOException e) {
                logger.error("Error in UserDashboardPanel.java:[createRequestResourcesSubPage] [IOException]: %s".formatted(getStackTraceAsString(e)));
            }
        });
        return requestPanel;
    }

}
