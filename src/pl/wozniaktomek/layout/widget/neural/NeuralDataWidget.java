package pl.wozniaktomek.layout.widget.neural;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.layout.control.NeuralControl;
import pl.wozniaktomek.layout.widget.Widget;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.util.NeuralObject;
import pl.wozniaktomek.service.DataTransferService;
import pl.wozniaktomek.service.LayoutService;

import java.util.ArrayList;

public class NeuralDataWidget extends Widget {
    private NeuralControl neuralControl;
    private NeuralNetwork neuralNetwork;

    private HBox buttonContainer;
    private HBox statusContainer;

    private Button buttonLoadLearningData;
    private Button buttonLoadTestingData;

    private Text textDataStatus;
    private Boolean learningDataStatus;
    private Boolean testingDataStatus;

    private ArrayList<NeuralObject> objectsLearning;
    private ArrayList<NeuralObject> objectsTesting;

    public NeuralDataWidget(NeuralControl neuralControl, NeuralNetwork neuralNetwork) {
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
        HBox mainContainer = layoutService.getHBox(0d, 0d, 24d);

        buttonContainer = layoutService.getHBox(0d, 0d, 12d);
        mainContainer.getChildren().add(buttonContainer);

        statusContainer = layoutService.getHBox(0d, 4d, 8d);

        contentContainer.getChildren().addAll(mainContainer, statusContainer);
    }

    private void initializeButtons() {
        buttonLoadLearningData = layoutService.getButton("Wczytaj dane uczące", null, 32d, null);
        buttonLoadTestingData = layoutService.getButton("Wczytaj dane testowe", null, 32d, null);
        buttonContainer.getChildren().addAll(buttonLoadLearningData, buttonLoadTestingData);
    }

    private void initializeStatuses() {
        statusContainer.getChildren().add(layoutService.getText("STATUS:", LayoutService.TextStyle.HEADING));
        textDataStatus = layoutService.getText("Nie wczytano wszystkich danych...", LayoutService.TextStyle.STATUS);
        statusContainer.getChildren().add(textDataStatus);
    }

    private void initializeButtonActions() {
        buttonLoadLearningData.setOnAction(event -> {
            objectsLearning = new DataTransferService().readFromFile();
            learningDataStatus = objectsLearning != null;
            validateObjects();
        });

        buttonLoadTestingData.setOnAction(event -> {
            objectsTesting = new DataTransferService().readFromFile();
            testingDataStatus = objectsTesting != null;
            validateObjects();
        });
    }

    private void validateObjects() {
        if (neuralNetwork.setObjects(objectsLearning, objectsTesting)) {
            textDataStatus.setText("Dane poprawne");
        } else {
            textDataStatus.setText("Dane uczące i dane testowe nie są zgodne!");
        }

        if (objectsLearning == null || objectsTesting == null) {
            textDataStatus.setText("Nie wczytano wszystkich danych...");
        }

        neuralControl.refreshSettings();
        neuralControl.refreshTopology();
        validateStyle();
    }

    private void validateStyle() {
        deleteStyles();
        setStyle(buttonLoadLearningData, learningDataStatus);
        setStyle(buttonLoadTestingData, testingDataStatus);
    }

    private void deleteStyles() {
        buttonLoadLearningData.getStyleClass().remove("button-success");
        buttonLoadLearningData.getStyleClass().remove("button-failure");

        buttonLoadTestingData.getStyleClass().remove("button-success");
        buttonLoadTestingData.getStyleClass().remove("button-failure");
    }

    private void setStyle(Button button, Boolean state) {
        if (state != null) {
            if (state) {
                button.getStyleClass().add("button-success");
            } else {
                button.getStyleClass().add("button-failure");
            }
        }
    }
}
