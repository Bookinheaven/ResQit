package org.burnknuckle.controllers.User;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.burnknuckle.utils.Database;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import static org.burnknuckle.controllers.Main.logger;
import static org.burnknuckle.javafx.model.UserDashboardPanel.currentUser;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;

public class disasterPageController {
    @FXML
    private ComboBox<String> disasterTypeCombo;

    @FXML
    private ComboBox<String> scaleCombo;

    @FXML
    private ComboBox<String> disasterScaleLevel;

    @FXML
    private ComboBox<String> severityLevelCombo;

    @FXML
    private Label scaleValueNo;

    @FXML
    private Slider scaleSlider;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextArea locationTextArea;

    @FXML
    private TextArea ImpactTextArea;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button submitButton;

    @FXML
    private Label warningLabel;

    @FXML
    private TextField disasterName;

    public void submitData() {
        Map<String, Object> formData = new HashMap<>();
        formData.put("disasterName", disasterName.getText());
        formData.put("disasterType", disasterTypeCombo.getValue());
        formData.put("scaleMeter", scaleCombo.getValue() +" | "+scaleSlider.getValue());
        formData.put("scale", disasterScaleLevel.getValue());
        formData.put("severity", severityLevelCombo.getValue());
        formData.put("responseStatus", "requested");
        formData.put("description", descriptionTextArea.getText());
        formData.put("location", locationTextArea.getText());
        formData.put("impactAssessment", ImpactTextArea.getText());
        formData.put("startDate", startDatePicker.getValue());
        formData.put("endDate", endDatePicker.getValue());
        formData.put("userUploaded", currentUser);


        try {
            Database db = Database.getInstance();
            db.getConnection();
            logger.info(db.checkForDuplicateEntries(formData));
            if (db.checkForDuplicateEntries(formData)) {
                warningLabel.setVisible(true);
                Timer timer = new Timer(3000, _ -> {
                    warningLabel.setVisible(false);
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                db.insertData(2, formData);
                warningLabel.setVisible(false);
            }
        } catch (Exception e){
            logger.error("Error in disasterPageController.java: |Exception in submitData| %s \n".formatted(getStackTraceAsString(e)));
        }
    }
    private ObservableList<String> relatedScales;
    private boolean isFormValid() {
        return disasterTypeCombo.getValue() != null &&
                scaleCombo.getValue() != null &&
                severityLevelCombo.getValue() != null &&
                !descriptionTextArea.getText().isEmpty() &&
                !locationTextArea.getText().isEmpty() &&
                !ImpactTextArea.getText().isEmpty() &&
                disasterScaleLevel.getValue() != null &&
                startDatePicker.getValue() != null &&
                disasterName.getText() != null;
    }

    @FXML
    private void initialize() {
        submitButton.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> !isFormValid(),
                        disasterTypeCombo.valueProperty(),
                        scaleCombo.valueProperty(),
                        severityLevelCombo.valueProperty(),
                        descriptionTextArea.textProperty(),
                        locationTextArea.textProperty(),
                        ImpactTextArea.textProperty(),
                        disasterScaleLevel.valueProperty(),
                        startDatePicker.valueProperty(),
                        disasterName.textProperty())
        );
        ObservableList<String> disasterTypes = FXCollections.observableArrayList(
                "Earthquake", "Flood", "Hurricane", "Tornado", "Wildfire");
        ObservableList<String> disasterScales = FXCollections.observableArrayList(
                "local", "regional", "national"
        );
        disasterTypeCombo.setItems(disasterTypes);
        disasterScaleLevel.setItems(disasterScales);
        scaleCombo.setItems(FXCollections.observableArrayList("Select a disaster type first"));
        severityLevelCombo.setItems(FXCollections.observableArrayList("Low", "Moderate", "High", "Critical"));
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
}