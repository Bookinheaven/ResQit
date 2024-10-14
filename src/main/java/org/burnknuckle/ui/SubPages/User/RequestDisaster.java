package org.burnknuckle.ui.SubPages.User;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.burnknuckle.utils.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;
import static org.burnknuckle.utils.ThemeManager.currentTheme;

public class RequestDisaster {
    public JFXPanel createDisasterSubPage(JPanel dashSpace) {
        JFXPanel disasterRequestPanel = new JFXPanel();
        dashSpace.setLayout(new BorderLayout());
        dashSpace.add(disasterRequestPanel, BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/user/requestPage.fxml")));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                setCSSTheme(currentTheme, scene);
                disasterRequestPanel.setScene(scene);

                ThemeManager.addThemeChangeListener(theme -> Platform.runLater(() -> setCSSTheme(theme, scene)));

            } catch (IOException e) {
                logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
            }
        });
        return disasterRequestPanel;
    }

    private void setCSSTheme(String theme, Scene scene) {
        if (theme.equals("dark")) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fxml/user/requestPage-dark.css")).toExternalForm());
        } else {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fxml/user/requestPage-light.css")).toExternalForm());
        }
    }
}
