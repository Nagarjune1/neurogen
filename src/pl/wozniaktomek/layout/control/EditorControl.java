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
import pl.wozniaktomek.layout.widget.DataEditorWidget;
import pl.wozniaktomek.service.DataService;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class EditorControl implements Initializable {
    @FXML private Button buttonClear;
    @FXML private Button buttonGenerate;
    @FXML private Button buttonSave;
    @FXML private Button buttonRead;
    @FXML private Button buttonHelp;

    @FXML private HBox titleContainer;
    @FXML private HBox chartContainer;

    @FXML private RadioButton dataClassRadioButton1;
    @FXML private RadioButton dataClassRadioButton2;
    @FXML private RadioButton dataClassRadioButton3;
    @FXML private RadioButton dataClassRadioButton4;
    @FXML private RadioButton dataClassRadioButton5;
    @FXML private ToggleGroup dataClassToggleGroup;

    @FXML private TextFlow textSummary;
    private Text textSummaryContent;

    private DataEditorWidget dataEditorWidget;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeRadioButtons();
        initializeSizeListener();
        initializeToggleListener();
        initializeSummaryListener();
        initializeButtonActions();
        initializeWidget();
    }

    private void initializeRadioButtons() {
        dataClassRadioButton1.setUserData(1);
        dataClassRadioButton2.setUserData(2);
        dataClassRadioButton3.setUserData(3);
        dataClassRadioButton4.setUserData(4);
        dataClassRadioButton5.setUserData(5);
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            titleContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth() - 82);
            dataEditorWidget.setChartSize(ThesisApp.windowControl.getContentPane().getWidth() - 200, ThesisApp.windowControl.getContentPane().getHeight() - 200);
        };

        ThesisApp.windowControl.getContentPane().widthProperty().addListener(stageSizeListener);
        ThesisApp.windowControl.getContentPane().heightProperty().addListener(stageSizeListener);
    }

    private void initializeToggleListener() {
        dataClassToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) ->
                dataEditorWidget.setClassNumber(Integer.valueOf(dataClassToggleGroup.getSelectedToggle().getUserData().toString())));
    }

    private void initializeSummaryListener() {
        textSummaryContent = new Text();
        textSummaryContent.textProperty().addListener((observable, oldValue, newValue) -> {
            textSummary.getChildren().clear();
            textSummary.getChildren().add(new Text(newValue));
        });
    }

    private void initializeButtonActions() {
        buttonClear.setOnAction(event -> dataEditorWidget.clearChart());
        buttonGenerate.setOnAction(event -> dataEditorWidget.setObjects(new DataService().generateObjects()));

        buttonSave.setOnAction(event -> {
            new DataService().saveToFile(dataEditorWidget.getObjects());
            dataEditorWidget.refresh();
        });

        buttonRead.setOnAction(event -> {
            HashMap<Integer, ArrayList<Point2D>> objects = new DataService().readFromFile();
            if (objects != null) {
                dataEditorWidget.setObjects(objects);
                dataEditorWidget.refresh();
            }
        });
    }

    private void initializeWidget() {
        dataEditorWidget = new DataEditorWidget();
        dataEditorWidget.setTextSummary(textSummaryContent);
        chartContainer.getChildren().add(dataEditorWidget.getChart());
        dataEditorWidget.refresh();
    }
}
