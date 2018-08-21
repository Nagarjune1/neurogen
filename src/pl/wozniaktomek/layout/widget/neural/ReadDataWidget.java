package pl.wozniaktomek.layout.widget.neural;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.layout.widget.Widget;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.service.data.DataObject;
import pl.wozniaktomek.service.data.DataService;

import java.util.ArrayList;

public class ReadDataWidget extends Widget {
    private NeuralNetwork neuralNetwork;

    private Button buttonLoadLearningData;
    private Button buttonLoadTestingData;

    private Text textLearningDataStatus;
    private Text textTestingDataStatus;

    private ArrayList<DataObject> objectsLearning;
    private ArrayList<DataObject> objectsTesting;

    public ReadDataWidget(NeuralNetwork neuralNetwork, String widgetTitle) {
        this.neuralNetwork = neuralNetwork;
        setTitle(widgetTitle);
        initialize();
    }

    private void initialize() {
        initializeReadDataContainer(DataType.LEARNING);
        initializeReadDataContainer(DataType.TESTING);
        initializeButtonActions();
    }

    private void initializeReadDataContainer(DataType dataType) {
        HBox hBox = new HBox();
        hBox.setSpacing(12d);
        hBox.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setPrefWidth(160d);

        if (dataType.equals(DataType.LEARNING)) {
            button.setText("Wczytaj dane uczące");
        } else {
            button.setText("Wczytaj dane testowe");
        }

        Text text = new Text("nie wybrano pliku...");
        text.getStyleClass().add("action-status");

        if (dataType.equals(DataType.LEARNING)) {
            buttonLoadLearningData = button;
            textLearningDataStatus = text;
        } else {
            buttonLoadTestingData = button;
            textTestingDataStatus = text;
        }

        hBox.getChildren().addAll(button, text);
        contentContainer.getChildren().add(hBox);
    }

    private void initializeButtonActions() {
        buttonLoadLearningData.setOnAction(event -> {
            objectsLearning = new DataService().readFromFile();
            setStatus(objectsLearning, textLearningDataStatus);
            validateObjects();
        });

        buttonLoadTestingData.setOnAction(event -> {
            objectsTesting = new DataService().readFromFile();
            setStatus(objectsTesting, textTestingDataStatus);
            validateObjects();
        });
    }


    private void validateObjects() {
        if (objectsLearning != null && objectsTesting != null) {
            if (checkInputsSize()) {
                neuralNetwork.setObjects(objectsLearning, objectsTesting);
            }
        }
    }

    private boolean checkInputsSize() {
        int inputSize = objectsLearning.get(0).getInputValues().size();

        for (DataObject dataObject : objectsLearning) {
            if (dataObject.getInputValues().size() != inputSize) {
                return false;
            }
        }

        for (DataObject dataObject : objectsTesting) {
            if (dataObject.getInputValues().size() != inputSize) {
                return false;
            }
        }

        return true;
    }

    private void setStatus(ArrayList<DataObject> objects, Text textStatus) {
        if (objects != null) {
            textStatus.setText("DANE POPRAWNE");
            textStatus.getStyleClass().add("action-status-success");
            textStatus.getStyleClass().remove("action-status-failure");
        } else {
            textStatus.setText("DANE NIEPOPRAWNE");
            textStatus.getStyleClass().add("action-status-failure");
            textStatus.getStyleClass().remove("action-status-success");
        }
    }

    private enum DataType {LEARNING, TESTING}
}
