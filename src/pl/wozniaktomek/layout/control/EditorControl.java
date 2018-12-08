package pl.wozniaktomek.layout.control;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import pl.wozniaktomek.ThesisApp;
import pl.wozniaktomek.service.DataEditorService;
import pl.wozniaktomek.util.EditorChart;
import pl.wozniaktomek.service.DataTransferService;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class EditorControl implements Initializable {
    @FXML private Button buttonGenerate;
    @FXML private Button buttonShuffle;
    @FXML private Button buttonClear;
    @FXML private Button buttonSave;
    @FXML private Button buttonRead;

    @FXML private HBox titleContainer;
    @FXML private HBox chartContainer;

    @FXML private Spinner<Integer> classAmountSpinner;
    @FXML private ChoiceBox<Integer> classChoiceBox;

    @FXML private TextFlow textSummary;
    private Text textSummaryContent;

    private EditorChart editorChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeClassSpinner();
        initializeClassChoiceBox();
        initializeSizeListener();
        initializeSummaryListener();
        initializeButtonActions();
        initializeWidget();
    }

    private void initializeClassSpinner() {
        classAmountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 4));
        classAmountSpinner.setEditable(true);

        classAmountSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            refreshClassChoiceBox(newValue);

            if (newValue < oldValue) {
                editorChart.clearChart();
            }
        }));
    }

    private void initializeClassChoiceBox() {
        for (int i = 1; i <= 4; i++) {
            classChoiceBox.getItems().add(i);
        }

        classChoiceBox.getSelectionModel().select(0);
        classChoiceBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> editorChart.setClassNumber(newValue)));
    }

    private void refreshClassChoiceBox(Integer classAmount) {
        Integer selectedItem = classChoiceBox.getSelectionModel().getSelectedItem();
        if (selectedItem > classAmount) {
            selectedItem = classAmount;
        }

        classChoiceBox.getItems().clear();
        for (int i = 1; i <= classAmount; i++) {
            classChoiceBox.getItems().add(i);
        }

        classChoiceBox.getSelectionModel().select(selectedItem);
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            titleContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth() - 82);
            editorChart.setChartSize(ThesisApp.windowControl.getContentPane().getWidth() - 282, ThesisApp.windowControl.getContentPane().getHeight() - 98);
        };

        ThesisApp.windowControl.getContentPane().widthProperty().addListener(stageSizeListener);
        ThesisApp.windowControl.getContentPane().heightProperty().addListener(stageSizeListener);
    }

    private void initializeSummaryListener() {
        textSummaryContent = new Text();

        textSummaryContent.textProperty().addListener((observable, oldValue, newValue) -> {
            textSummary.getChildren().clear();
            textSummaryContent = new Text(newValue);
            textSummaryContent.getStyleClass().add("text-paragraph");

            textSummary.getChildren().add(textSummaryContent);
        });
    }

    private void initializeButtonActions() {
        buttonGenerate.setOnAction(event -> {
            editorChart.setObjects(new DataEditorService().generateObjects());
            editorChart.refreshWidget();
        });

        buttonShuffle.setOnAction(event -> {
            editorChart.setObjects(new DataEditorService().shuffleObjects(editorChart.getObjects()));
            editorChart.refreshWidget();
        });

        buttonClear.setOnAction(event -> editorChart.clearChart());

        buttonSave.setOnAction(event -> {
            new DataTransferService().saveToFile(editorChart.getObjects());
            editorChart.refreshWidget();
        });

        buttonRead.setOnAction(event -> {
            HashMap<Integer, ArrayList<Point2D>> objects = new DataTransferService().parseListToMap(new DataTransferService().readFromFile());
            if (objects != null) {
                editorChart.setObjects(objects);
                editorChart.refreshWidget();
            }
        });
    }

    private void initializeWidget() {
        editorChart = new EditorChart();
        editorChart.setTextSummary(textSummaryContent);
        chartContainer.getChildren().add(editorChart.getChart());
        editorChart.refreshWidget();
    }
}
