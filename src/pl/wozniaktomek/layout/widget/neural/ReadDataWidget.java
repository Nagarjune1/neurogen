package pl.wozniaktomek.layout.widget.neural;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.layout.widget.Widget;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.service.data.DataService;

import java.util.ArrayList;
import java.util.HashMap;

public class ReadDataWidget extends Widget {
    private NeuralNetwork neuralNetwork;

    private Button buttonLoadLearningData;
    private Button buttonLoadTestingData;

    private Text textLearningDataStatus;
    private Text textTestingDataStatus;

    private HashMap<Integer, ArrayList<Point2D>> dataLearning;
    private HashMap<Integer, ArrayList<Point2D>> dataTesting;

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
            dataLearning = new DataService().readFromFile();
            checkStatus(DataType.LEARNING);
        });

        buttonLoadTestingData.setOnAction(event -> {
            dataTesting = new DataService().readFromFile();
            checkStatus(DataType.TESTING);
        });
    }

    private void checkStatus(DataType dataType) {
        if (dataType.equals(DataType.LEARNING)) {
            if (setStatus(dataLearning, textLearningDataStatus)) {
                // neuralNetwork.addLearningData(dataLearning);
            }
        } else {
            if (setStatus(dataTesting, textTestingDataStatus)) {
                // neuralNetwork.addTestingData(dataTesting);
            }
        }
    }

    private boolean setStatus(HashMap<Integer, ArrayList<Point2D>> objects, Text textStatus) {
        if (objects != null) {
            textStatus.setText("DANE POPRAWNE");
            textStatus.getStyleClass().add("action-status-success");
            textStatus.getStyleClass().remove("action-status-failure");
            return true;
        } else {
            textStatus.setText("DANE NIEPOPRAWNE");
            textStatus.getStyleClass().add("action-status-failure");
            textStatus.getStyleClass().remove("action-status-success");
            return false;
        }
    }

    private enum DataType {LEARNING, TESTING}
}
