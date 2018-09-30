package pl.wozniaktomek.widget;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.layout.control.NeuralControl;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.NeuralObject;
import pl.wozniaktomek.service.DataTransferService;
import pl.wozniaktomek.widget.Widget;

import java.util.ArrayList;

public class NeuralDataWidget extends Widget {
    private NeuralControl neuralControl;
    private NeuralNetwork neuralNetwork;

    private Button buttonLoadLearningData;
    private Button buttonLoadTestingData;

    private Text textLearningFileStatus;
    private Text textTestingFileStatus;
    private Text textDataStatus;

    private ArrayList<NeuralObject> objectsLearning;
    private ArrayList<NeuralObject> objectsTesting;

    public NeuralDataWidget(NeuralControl neuralControl, NeuralNetwork neuralNetwork, String widgetTitle) {
        this.neuralControl = neuralControl;
        this.neuralNetwork = neuralNetwork;
        setTitle(widgetTitle);
        initialize();
    }

    private void initialize() {
        initializeReadDataContainer(DataType.LEARNING);
        initializeReadDataContainer(DataType.TESTING);
        // initializeDataStatus();
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
        text.getStyleClass().addAll("action-status", "text-color-status");

        if (dataType.equals(DataType.LEARNING)) {
            buttonLoadLearningData = button;
            textLearningFileStatus = text;
        } else {
            buttonLoadTestingData = button;
            textTestingFileStatus = text;
        }

        hBox.getChildren().addAll(button, text);
        contentContainer.getChildren().add(hBox);
    }

    /*
    private void initializeDataStatus() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(0, 0, 0, 2));
        hBox.setAlignment(Pos.CENTER_LEFT);

        textDataStatus = new Text("Nie załadowano wszystkich danych");
        textDataStatus.getStyleClass().add("action-bold-status");

        hBox.getChildren().add(textDataStatus);
        contentContainer.getChildren().add(hBox);
    }
    */

    private void initializeButtonActions() {
        buttonLoadLearningData.setOnAction(event -> {
            objectsLearning = new DataTransferService().readFromFile();
            setFileDataStatus(textLearningFileStatus, objectsLearning);
            validateObjects();
        });

        buttonLoadTestingData.setOnAction(event -> {
            objectsTesting = new DataTransferService().readFromFile();
            setFileDataStatus(textTestingFileStatus, objectsTesting);
            validateObjects();
        });
    }

    private void validateObjects() {
        if (!textLearningFileStatus.getText().equals("nie wybrano pliku...") && !textTestingFileStatus.getText().equals("nie wybrano pliku...")) {
            if (neuralNetwork.setObjects(objectsLearning, objectsTesting)) {
                this.setStyle(WidgetStyle.SUCCESS);
                neuralControl.refreshSettingsWidget();
                neuralControl.refreshTopologyWidget();
            } else {
                this.setStyle(WidgetStyle.FAILURE);
                neuralControl.refreshSettingsWidget();
                neuralControl.refreshTopologyWidget();
            }
        }
    }

    /*
    private void setGeneralDataStatus(DataStatus dataStatus) {
        if (dataStatus.equals(DataStatus.OK)) {
            textDataStatus.setText("Dane POPRAWNE");
            setStatusClass(dataStatus, textDataStatus);
        } else {
            textDataStatus.setText("Dane NIEPOPRAWNE (w celu pomocy naciśnij przycisk w prawym górnym rogu)");
            setStatusClass(dataStatus, textDataStatus);
        }
    }
    */

    private void setFileDataStatus(Text textStatus, ArrayList<NeuralObject> objects) {
        if (objects != null) {
            textStatus.setText("plik poprawny");
            setStatusClass(DataStatus.OK, textStatus);
        } else {
            textStatus.setText("plik niepoprawny");
            setStatusClass(DataStatus.BAD, textStatus);
        }
    }

    private void setStatusClass(DataStatus dataStatus, Text text) {
        if (dataStatus.equals(DataStatus.OK)) {
            text.getStyleClass().remove("text-color-failure");
            text.getStyleClass().add("text-color-success");

        } else {
            text.getStyleClass().remove("text-color-success");
            text.getStyleClass().add("text-color-failure");
        }
    }

    private enum DataType {LEARNING, TESTING}

    private enum DataStatus {OK, BAD}
}
