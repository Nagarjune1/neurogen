package pl.wozniaktomek.layout.control;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import pl.wozniaktomek.ThesisApp;
import pl.wozniaktomek.layout.widgets.DataEditorWidget;

import java.net.URL;
import java.util.ResourceBundle;

public class EditorControl implements Initializable {
    @FXML private Button buttonGenerate;
    @FXML private Button buttonClear;
    @FXML private Button buttonSave;
    @FXML private Button buttonRead;

    @FXML private HBox chartContainer;

    @FXML private RadioButton dataClassRadioButton1;
    @FXML private RadioButton dataClassRadioButton2;
    @FXML private RadioButton dataClassRadioButton3;
    @FXML private RadioButton dataClassRadioButton4;
    @FXML private RadioButton dataClassRadioButton5;
    private final ToggleGroup dataClassToggleGroup = new ToggleGroup();

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
        dataClassRadioButton1.setToggleGroup(dataClassToggleGroup);
        dataClassRadioButton2.setToggleGroup(dataClassToggleGroup);
        dataClassRadioButton3.setToggleGroup(dataClassToggleGroup);
        dataClassRadioButton4.setToggleGroup(dataClassToggleGroup);
        dataClassRadioButton5.setToggleGroup(dataClassToggleGroup);

        dataClassRadioButton1.setUserData(1);
        dataClassRadioButton2.setUserData(2);
        dataClassRadioButton3.setUserData(3);
        dataClassRadioButton4.setUserData(4);
        dataClassRadioButton5.setUserData(5);
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
            dataEditorWidget.setChartSize(ThesisApp.windowControl.getContentPane().getWidth() - 200, ThesisApp.windowControl.getContentPane().getHeight() - 192);

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
    }

    private void initializeWidget() {
        dataEditorWidget = new DataEditorWidget();
        dataEditorWidget.setTextSummary(textSummaryContent);
        chartContainer.getChildren().add(dataEditorWidget.getChart());
    }
}
