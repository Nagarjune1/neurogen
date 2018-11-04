package pl.wozniaktomek.layout.widget;

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

    private HBox buttonContainer;
    private HBox statusContainer;

    private Button buttonLoadData;
    private Text textDataStatus;

    private ArrayList<NeuralObject> learningData;

    public LoadDataWidget(NeuralControl neuralControl, NeuralNetwork neuralNetwork) {
        this.neuralControl = neuralControl;
        this.neuralNetwork = neuralNetwork;
        createPrimaryWidget("Dane wejściowe");
        initialize();
    }

    private void initialize() {
        initializeContainers();
        initializeButtons();
        initializeStatuses();
        initializeButtonActions();
    }

    private void initializeContainers() {
        HBox mainContainer = layoutService.getHBox(0d, 0d, 16d);

        buttonContainer = layoutService.getHBox(0d, 0d, 0d);
        statusContainer = layoutService.getHBox(0d, 4d, 8d);
        mainContainer.getChildren().addAll(buttonContainer, statusContainer);

        contentContainer.getChildren().add(mainContainer);
    }

    private void initializeButtons() {
        buttonLoadData = layoutService.getButton("Wczytaj dane uczące", null, 32d, null);
        buttonContainer.getChildren().add(buttonLoadData);
    }

    private void initializeStatuses() {
        statusContainer.getChildren().add(layoutService.getText("STATUS:", LayoutService.TextStyle.HEADING));
        textDataStatus = layoutService.getText("Nie wczytano danych...", LayoutService.TextStyle.STATUS);
        statusContainer.getChildren().add(textDataStatus);
    }

    private void initializeButtonActions() {
        buttonLoadData.setOnAction(event -> {
            learningData = new DataTransferService().readFromFile();
            validateLearningData();
        });
    }

    private void validateLearningData() {
        if (neuralNetwork.loadLearningData(learningData)) {
            textDataStatus.setText("Dane poprawne");
            setButtonStyle(true);
        } else {
            textDataStatus.setText("Dane nieprawidłowe");
            setButtonStyle(false);
        }

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
