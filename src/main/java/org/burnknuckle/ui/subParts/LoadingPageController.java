package org.burnknuckle.ui.subParts;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.control.ProgressBar;

public class LoadingPageController {
    @FXML
    private ProgressBar progressBar;

    Timeline loadingAnimation;

    public void initialize() {
        progressBar.setProgress(-1.0);

        loadingAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(progressBar.progressProperty(), 1.0))
        );
        loadingAnimation.setCycleCount(Timeline.INDEFINITE);
        loadingAnimation.setAutoReverse(false);
        loadingAnimation.play();
    }
}
