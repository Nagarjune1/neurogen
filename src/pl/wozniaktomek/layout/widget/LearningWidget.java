package pl.wozniaktomek.layout.widget;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.NeuroGenApp;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.service.LayoutService;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class LearningWidget extends Widget {
    private NeuralNetwork neuralNetwork;

    /* Panels */
    private VBox visualizationHelpVBox;

    /* Controls */
    private Button buttonStartLearning;
    private Button buttonStopLearning;

    /* Statistics */
    private Text textTime;
    private Text textIterations;
    private Text textError;
    private Text textoObjectsOutOfTolerance;

    /* Timer */
    private Timer timer;
    private Long startTime;

    /* Text format */
    private DecimalFormat errorDecimalFormat = new DecimalFormat("#.####");
    private DecimalFormat timeDecimalFormat = new DecimalFormat("#.#");

    private LearningVisualizationWidget learningVisualizationWidget;

    public LearningWidget(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        createWidget("Panel kontrolny");
        initialize();
    }

    /* Initialization */
    private void initialize() {
        initializeControlsContainer();
        initializeStatisticsContainer();
        initializeVisualizationContainer();
        initializeSizeListener();
        initializeButtonActions();
        disableControls();

        neuralNetwork.getLearning().setLearningWidget(this);
    }

    private void initializeControlsContainer() {
        VBox vBox = layoutService.getVBox(0d, 0d, 16d);
        vBox.getChildren().add(layoutService.getText("KONTROLA UCZENIA", LayoutService.TextStyle.HEADING));
        contentContainer.getChildren().add(vBox);

        HBox hbox = layoutService.getHBox(0d, 8d, 12d);
        hbox.getChildren().add(layoutService.getText("STEROWANIE", LayoutService.TextStyle.PARAGRAPH));
        buttonStartLearning = layoutService.getButton("Uruchom uczenie");
        buttonStopLearning = layoutService.getButton("Przewij uczenie");
        buttonStopLearning.setDisable(true);
        hbox.getChildren().addAll(buttonStartLearning, buttonStopLearning);

        hbox.getChildren().add(new Separator(Orientation.VERTICAL));
        hbox.getChildren().addAll(getInterfaceUpdateCheckbox(), getLearningVisualizationCheckbox());
        vBox.getChildren().add(hbox);

        visualizationHelpVBox = layoutService.getVBox(0d, 0d, 0d);
        contentContainer.getChildren().add(visualizationHelpVBox);
    }

    private void initializeStatisticsContainer() {
        VBox vBox = layoutService.getVBox(0d, 0d, 12d);
        vBox.getChildren().add(layoutService.getText("STATYSTYKI UCZENIA", LayoutService.TextStyle.HEADING));
        contentContainer.getChildren().add(vBox);

        HBox mainHBox = layoutService.getHBox(0d, 0d, 12d);
        vBox.getChildren().add(mainHBox);

        VBox timeVbox = layoutService.getVBox(8d, 16d, 4d);
        timeVbox.getStyleClass().add("stats-pane");
        timeVbox.setMinWidth(156d);
        textTime = layoutService.getText("N/A", LayoutService.TextStyle.STATUS_WHITE);
        timeVbox.getChildren().addAll(layoutService.getText("Czas uczenia", LayoutService.TextStyle.PARAGRAPH_WHITE), textTime);
        mainHBox.getChildren().add(timeVbox);

        VBox iterationVbox = layoutService.getVBox(8d, 16d, 4d);
        iterationVbox.getStyleClass().add("stats-pane");
        iterationVbox.setMinWidth(156d);
        textIterations = layoutService.getText("N/A", LayoutService.TextStyle.STATUS_WHITE);
        iterationVbox.getChildren().addAll(layoutService.getText("Iteracje", LayoutService.TextStyle.PARAGRAPH_WHITE), textIterations);
        mainHBox.getChildren().add(iterationVbox);

        VBox errorVbox = layoutService.getVBox(8d, 16d, 4d);
        errorVbox.getStyleClass().add("stats-pane");
        errorVbox.setMinWidth(156d);
        textError = layoutService.getText("N/A", LayoutService.TextStyle.STATUS_WHITE);
        errorVbox.getChildren().addAll(layoutService.getText("Błąd całkowity", LayoutService.TextStyle.PARAGRAPH_WHITE), textError);
        mainHBox.getChildren().add(errorVbox);

        VBox objectsVbox = layoutService.getVBox(8d, 16d, 4d);
        objectsVbox.getStyleClass().add("stats-pane");
        objectsVbox.setMinWidth(156d);
        textoObjectsOutOfTolerance = layoutService.getText("N/A", LayoutService.TextStyle.STATUS_WHITE);
        objectsVbox.getChildren().addAll(layoutService.getText("Dane poza tolerancją", LayoutService.TextStyle.PARAGRAPH_WHITE), textoObjectsOutOfTolerance);
        mainHBox.getChildren().add(objectsVbox);
    }

    private void initializeVisualizationContainer() {
        VBox visualizationyContainer = layoutService.getVBox(0d, 0d, 0d);

        learningVisualizationWidget = new LearningVisualizationWidget(neuralNetwork);
        learningVisualizationWidget.minimizeWidget();

        visualizationyContainer.getChildren().add(learningVisualizationWidget.getWidget());
        contentContainer.getChildren().add(visualizationyContainer);
    }

    private void initializeButtonActions() {
        buttonStartLearning.setOnAction(event -> startLearning());
        buttonStopLearning.setOnAction(event -> stopLearning());
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            if (startTime != null) {
                drawLearningVisulization();
            }
        };
        NeuroGenApp.windowControl.getContentPane().widthProperty().addListener(stageSizeListener);
    }

    /* Control */
    private void startLearning() {
        neuralNetwork.startLearning();
        switchButtons(buttonStartLearning);
        startTimer();
    }

    private void stopLearning() {
        neuralNetwork.stopLearning();
        switchButtons(buttonStopLearning);
        stopTimer();
    }

    private void switchButtons(Button button) {
        if (button.equals(buttonStartLearning)) {
            buttonStartLearning.setDisable(true);
            buttonStopLearning.setDisable(false);
        } else {
            buttonStartLearning.setDisable(false);
            buttonStopLearning.setDisable(true);
        }
    }

    /* Interface control */
    public void drawLearningVisulization() {
        Platform.runLater(() -> learningVisualizationWidget.drawNetwork(NeuroGenApp.windowControl.getContentPane().getWidth() - 108));
    }

    public void disableControls() {
        buttonStartLearning.setDisable(true);
        buttonStopLearning.setDisable(true);
    }

    public void enableControls() {
        buttonStartLearning.setDisable(false);
    }

    public void endLearning() {
        switchButtons(buttonStopLearning);
        stopTimer();
    }

    /* Timer */
    private void startTimer() {
        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                textTime.setText(timeDecimalFormat.format((System.currentTimeMillis() - startTime) / 1000d) + " s");
            }
        }, 0, 50);
    }

    private void stopTimer() {
        timer.cancel();
    }

    /* Interface update */
    public void updateInterface(Integer iteration, Double error, String objects) {
        Platform.runLater(() -> {
            this.textIterations.setText(String.valueOf(iteration));
            this.textError.setText(errorDecimalFormat.format(error));
            this.textoObjectsOutOfTolerance.setText(objects);
        });

        neuralNetwork.getLearning().setIsNowInterfaceUpdating(false);
    }

    /* Controls initialization */
    private CheckBox getInterfaceUpdateCheckbox() {
        CheckBox checkBox = layoutService.getCheckBox("Aktualizacja statystyk", null);
        checkBox.setSelected(neuralNetwork.getLearning().getInterfaceUpdating());

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> neuralNetwork.getLearning().setInterfaceUpdating(newValue));
        return checkBox;
    }

    private CheckBox getLearningVisualizationCheckbox() {
        CheckBox checkBox = layoutService.getCheckBox("Wizualizacja uczenia", null);
        checkBox.setSelected(neuralNetwork.getLearning().getLearningVisualization());

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            neuralNetwork.getLearning().setLearningVisualization(newValue);

            if (newValue) {
                learningVisualizationWidget.maximizeWidget();
                visualizationHelpVBox.getChildren().add(layoutService.getText("Wizualizacja: kolor ciemny - waga dodatnia, kolor jasny - waga ujemna", LayoutService.TextStyle.PARAGRAPH));
            } else {
                learningVisualizationWidget.minimizeWidget();
                visualizationHelpVBox.getChildren().clear();
            }
        });
        return checkBox;
    }
}
