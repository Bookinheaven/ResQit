package org.burnknuckle.controllers.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static org.burnknuckle.controllers.Main.logger;

public class requestPageController {
    @FXML
    private ComboBox<String> disasterTypeCombo;

    @FXML
    private ComboBox<String> scaleCombo;

    @FXML
    private ComboBox<String> severityLevelCombo;

    @FXML
    private Label scaleValueNo;

    @FXML
    private Slider scaleSlider;

    private ObservableList<String> relatedScales;
    @FXML
    private void initialize() {
        ObservableList<String> disasterTypes = FXCollections.observableArrayList(
                "Earthquake", "Flood", "Hurricane", "Tornado", "Wildfire");
        disasterTypeCombo.setItems(disasterTypes);
        scaleCombo.setItems(FXCollections.observableArrayList("Select a disaster type first"));
        severityLevelCombo.setItems(FXCollections.observableArrayList("Low", "Moderate", "High", "Severe"));
        scaleSlider.setMin(0);
        scaleSlider.setMax(10);
        scaleSlider.setValue(5);
        scaleValueNo.setText(String.valueOf(scaleSlider.getValue()));
        scaleSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            scaleValueNo.setText(String.format("%.1f", newValue.doubleValue()));
        });
    }

    @FXML
    private void disasterTypeComboSelect() {
        String selectedItem = disasterTypeCombo.getValue();
        logger.info("Selected Disaster Type: {}", selectedItem);

        relatedScales = switch (selectedItem) {
            case "Earthquake" -> FXCollections.observableArrayList(
                    "Richter Scale", "Moment Magnitude Scale", "Modified Mercalli Intensity Scale");
            case "Flood" -> FXCollections.observableArrayList(
                    "Flood Stage", "Inundation Level", "Streamflow Gauge Scale");
            case "Hurricane" -> FXCollections.observableArrayList(
                    "Saffir-Simpson Scale", "Beaufort Wind Scale", "Accumulated Cyclone Energy (ACE)");
            case "Tornado" -> FXCollections.observableArrayList(
                    "Enhanced Fujita Scale", "TORRO Scale", "Damage Path Scale");
            case "Wildfire" -> FXCollections.observableArrayList(
                    "Fire Danger Index (FDI)", "Burn Area Index (BAI)", "Canadian Fire Weather Index (FWI)");
            default -> FXCollections.observableArrayList("Unknown Scale");
        };

        scaleCombo.setItems(relatedScales);
        scaleCombo.setValue(relatedScales.isEmpty() ? null : relatedScales.get(0));
        logger.info("Scale ComboBox Updated with: {}", relatedScales.toString());
    }

    @FXML
    private void scaleComboSelect() {
        String selectedScale = scaleCombo.getValue();
        if (selectedScale == null) {
            logger.info(relatedScales.toString());
            scaleCombo.setItems(relatedScales);
            scaleCombo.setValue(relatedScales.isEmpty() ? null : relatedScales.get(0));

            selectedScale = scaleCombo.getValue();
        };
        logger.info("Selected Scale: {}", selectedScale);
        switch (selectedScale) {
            case "Richter Scale", "Moment Magnitude Scale" -> {
                scaleSlider.setMin(0);
                scaleSlider.setMax(9.5);
            }
            case "Modified Mercalli Intensity Scale" -> {
                scaleSlider.setMin(1);
                scaleSlider.setMax(12);
            }
            case "Flood Stage", "Fire Danger Index (FDI)" -> {
                scaleSlider.setMin(0);
                scaleSlider.setMax(100);
            }
            case "Saffir-Simpson Scale" -> {
                scaleSlider.setMin(1);
                scaleSlider.setMax(5);
            }
            case "Beaufort Scale" -> {
                scaleSlider.setMin(0);
                scaleSlider.setMax(12);
            }
            case "Enhanced Fujita Scale" -> {
                scaleSlider.setMin(0);
                scaleSlider.setMax(5);
            }
            case "TORRO Scale" -> {
                scaleSlider.setMin(0);
                scaleSlider.setMax(11);
            }
            case "Burn Area Index (BAI)" -> {
                scaleSlider.setMin(0);
                scaleSlider.setMax(200);
            }
            default -> { // "Inundation Level"
                scaleSlider.setMin(0);
                scaleSlider.setMax(10);
            }
        }

        scaleValueNo.setText(String.format("%.1f", scaleSlider.getValue()));
    }

    @FXML
    private void severityLevelComboSelect() {
        String selectedSeverity = severityLevelCombo.getValue();
        logger.info("Selected Severity Level: {}", selectedSeverity);
    }
}
