package pl.wozniaktomek.layout.widget;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.layout.control.NeuralControl;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.util.NeuralObject;
import pl.wozniaktomek.service.DataTransferService;
import pl.wozniaktomek.service.LayoutService;

import java.util.ArrayList;

public class LoadDataWidget extends Widget {
    private NeuralControl neuralControl;
    private NeuralNetwork neuralNetwork;
    private DataTransferService dataTransferService;

    private HBox buttonContainer;
    private HBox statusContainer;

    private Button buttonLoadData;
    private Text textDataStatus;

    private ArrayList<NeuralObject> learningData;

    public LoadDataWidget(NeuralControl neuralControl, NeuralNetwork neuralNetwork) {
        this.neuralControl = neuralControl;
        this.neuralNetwork = neuralNetwork;
        createWidget("Dane wejściowe");
        initialize();
    }

    /* Initialization */
    private void initialize() {
        initializeContainers();
        initializeButtons();
        initializeStatuses();
        initializeButtonActions();
    }

    private void initializeContainers() {
        HBox localContentContainer = layoutService.getHBox(0d, 0d, 16d);

        buttonContainer = layoutService.getHBox(0d, 0d, 0d);
        buttonContainer.setAlignment(Pos.BASELINE_LEFT);

        statusContainer = layoutService.getHBox(0d, 4d, 6d);
        statusContainer.setAlignment(Pos.CENTER_LEFT);

        localContentContainer.getChildren().addAll(buttonContainer, statusContainer);
        contentContainer.getChildren().add(localContentContainer);
    }

    private void initializeButtons() {
        buttonLoadData = layoutService.getButton("Wczytaj dane uczące", null, 32d, null);
        buttonContainer.getChildren().add(buttonLoadData);
    }

    private void initializeStatuses() {
        statusContainer.getChildren().add(layoutService.getText("STATUS:", LayoutService.TextStyle.HEADING));
        textDataStatus = layoutService.getText("Nie wczytano danych.", LayoutService.TextStyle.HEADING);
        statusContainer.getChildren().add(textDataStatus);
    }

    private void initializeButtonActions() {
        buttonLoadData.setOnAction(event -> {
            dataTransferService = new DataTransferService();
            learningData = dataTransferService.readFromFile();
            validateLearningData();
        });
    }

    /* Actions */
    private void validateLearningData() {
        if (neuralNetwork.loadLearningData(learningData)) {
            setButtonStyle(true);
            neuralControl.maximizeSettingsWidgets();
        } else {
            neuralControl.minimizeSettingsWidgets();
            setButtonStyle(false);
        }

        textDataStatus.setText(dataTransferService.getTransferStatus());

        neuralNetwork.setLearned(false);
        neuralControl.refreshSettings();
        neuralControl.refreshTopology();
    }

    private void setButtonStyle(Boolean state) {
        buttonLoadData.getStyleClass().remove("button-success");
        buttonLoadData.getStyleClass().remove("button-failure");

        if (state != null) {
            if (state) {
                buttonLoadData.getStyleClass().add("button-success");
            } else {
                buttonLoadData.getStyleClass().add("button-failure");
            }
        }
    }
}
